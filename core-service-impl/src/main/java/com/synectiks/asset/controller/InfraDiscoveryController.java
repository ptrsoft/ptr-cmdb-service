package com.synectiks.asset.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.api.controller.InfraDiscoveryApi;
import com.synectiks.asset.api.model.CloudElementDTO;
import com.synectiks.asset.api.model.LandingzoneDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.*;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.handler.factory.AwsHandlerFactory;
import com.synectiks.asset.service.*;
import com.synectiks.asset.util.DateFormatUtil;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class InfraDiscoveryController implements InfraDiscoveryApi {

    private final Logger logger = LoggerFactory.getLogger(InfraDiscoveryController.class);
    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private CloudElementService cloudElementService;

    @Autowired
    private CloudElementCostService cloudElementCostService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;

    @Autowired
    private CloudService cloudService;

    private static ConcurrentMap<Long, String> ELEMENT_DISCOVERY_REQUEST_QUEUE = new ConcurrentHashMap<>();

    @Override
    public ResponseEntity<Object> discoverCloudElements(Long orgId, String elementType, Long landingZoneId, String query )  {
        logger.debug("REST request to pull aws element details. Org id: {}, landingZoneId: {}, elementType: {}, query: {}", orgId, landingZoneId, elementType, query);
        Optional<Landingzone> oLz = landingzoneService.findOne(landingZoneId);
        if(!oLz.isPresent()){
            logger.error("landingZoneId does not exists");
            return ResponseUtil.wrapOrNotFound(Optional.ofNullable(oLz));
        }
        if(!StringUtils.isBlank(query)){
            logger.info("REST request to pull aws elements for landingZoneId: {}, elementType: {}, query: {}", landingZoneId, elementType, query);
            CloudHandler cloudHandler = AwsHandlerFactory.getHandlerByQuery(query);
            Object object = cloudHandler.save(elementType, oLz.get(), query);
            return ResponseEntity.ok(object);
        }

        logger.info("REST request to pull all aws elements for landingZoneId: {}", landingZoneId);
        ELEMENT_DISCOVERY_REQUEST_QUEUE.put(landingZoneId, Constants.ALL);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> processAwsTags(Long orgId, Long landingZoneId, String elementType )  {
        logger.debug("REST request to process aws tags. Org id: {}, landingZoneId: {}, elementType: {}", orgId, landingZoneId, elementType);
        Optional<Landingzone> oLz = landingzoneService.findOne(landingZoneId);
        if(!oLz.isPresent()){
            logger.error("landingZoneId does not exists");
            return ResponseUtil.wrapOrNotFound(Optional.empty());
        }
        logger.info("Processing aws tags for landing-zone:  {}",oLz.get().getLandingZone());
        CloudElementDTO cloudElementDTO = new CloudElementDTO();
        cloudElementDTO.setLandingzoneId(landingZoneId);
        cloudElementDTO.setElementType(elementType);
        List<CloudElement> cloudElementList = cloudElementService.search(cloudElementDTO);
        for(CloudElement cloudElement: cloudElementList){
            logger.debug("process tag: {}",elementType);
            CloudHandler cloudHandler = AwsHandlerFactory.getHandler(elementType);
            if(cloudHandler == null){
                logger.warn("Handler not found to process element-type: {}",elementType);
                continue;
            }
            cloudHandler.processTag(cloudElement);
        }
        return ResponseEntity.ok("done");
    }

    @Scheduled(cron = "0 */2 * * * *") // Every 2 minutes
    public void processDiscoverElementsRequest(){
        for (Long landingZoneId: ELEMENT_DISCOVERY_REQUEST_QUEUE.keySet()) {
            if(ELEMENT_DISCOVERY_REQUEST_QUEUE.get(landingZoneId).equalsIgnoreCase(Constants.ALL)){
                Optional<Landingzone> ol = landingzoneService.findOne(landingZoneId);
                if(ol.isPresent() && ol.get().getCloud().equalsIgnoreCase(Constants.AWS)){
                    Cloud vpc = cloudService.findByNameAndElementType(ol.get().getCloud().toUpperCase(), Constants.VPC);
                    if(vpc != null){
                        CloudHandler vpcHandler = AwsHandlerFactory.getHandlerByQuery(vpc.getListQuery());
                        if(vpcHandler != null){
                            vpcHandler.save("landingZone", ol.get(), vpc.getListQuery());
                        }
                    }
                    List<Cloud> cloudList = cloudService.findByName(ol.get().getCloud().toUpperCase())
                            .stream().filter(obj -> !obj.getElementType().equalsIgnoreCase(Constants.VPC)).collect(Collectors.toList());

                    for(Cloud cloud: cloudList){
                        if(!StringUtils.isBlank(cloud.getListQuery())){
                            try{
                                logger.debug("Fetching elements of: {}",cloud.getElementType());
                                CloudHandler cloudHandler = AwsHandlerFactory.getHandlerByQuery(cloud.getListQuery());
                                if(cloudHandler == null){
                                    logger.warn("Handler not found to process element-type: {}",cloud.getListQuery());
                                    continue;
                                }
                                cloudHandler.save("landingZone", ol.get(), cloud.getListQuery());
                            }catch (Exception e){
                                logger.error("Element discovery failed for landing-zone: {} and element-type: {}. Exception: {}",ol.get().getLandingZone(), cloud.getListQuery(),e.getMessage() );
                            }
                        }
                    }
                }
                ELEMENT_DISCOVERY_REQUEST_QUEUE.put(landingZoneId, Constants.COMPLETED);
            }
        }
        ELEMENT_DISCOVERY_REQUEST_QUEUE.keySet().removeIf(key -> ELEMENT_DISCOVERY_REQUEST_QUEUE.get(key).equalsIgnoreCase(Constants.COMPLETED));
    }

    @Scheduled(cron = "0 0 1 * * *") // At 01:00 AM
    public void pullAwsElementJob(){
        logger.info("Calling batch job to pull aws elements");
        Config config = configService.findByKey(Constants.BATCH_JOB_PULL_AWS_ELEMENTS);
        if(!Constants.ACTIVE.equalsIgnoreCase(config.getValue())){
            logger.warn("Batch job to pull aws elements not active. Process skipped");
            return;
        }
        LandingzoneDTO landingzoneDTO = new LandingzoneDTO();
        landingzoneDTO.setCloud(Constants.AWS);
        landingzoneDTO.setStatus(Constants.ACTIVE);
        List<Landingzone> landingzoneList = landingzoneService.search(landingzoneDTO);
        for (Landingzone landingzone : landingzoneList) {
            logger.debug("Pulling all the aws elements for landing-zone:  {}", landingzone);
            for (String query : Constants.AWS_ELEMENT_QUERY) {
                logger.debug("pulling aws elements for : {}", query);
                try{
                    CloudHandler cloudHandler = AwsHandlerFactory.getHandlerByQuery(query);
                    if(cloudHandler == null){
                        logger.warn("Handler not found to process element-type: {}",query);
                        continue;
                    }
                    cloudHandler.save("landingZone", landingzone, query);
                }catch (Exception e){
                    logger.error("Getting aws elements for landing-zone: {} and element-type: {} failed. Exception: ",landingzone.getLandingZone(), query,e );
                }
            }
        }
    }

    @Scheduled(cron = "0 0 3 * * *") //At 03:00 AM
    public void generateSimulatedCost(){
        logger.info("Calling batch job to pull aws cost");
        Config config = configService.findByKey(Constants.BATCH_JOB_PULL_AWS_ELEMENTS_COST);
        if(!Constants.ACTIVE.equalsIgnoreCase(config.getValue())){
            logger.warn("Batch job to pull aws elements cost not active. Process skipped");
            return;
        }
        LandingzoneDTO landingzoneDTO = new LandingzoneDTO();
        landingzoneDTO.setCloud(Constants.AWS);
        landingzoneDTO.setStatus(Constants.ACTIVE);
        List<Landingzone> landingzoneList = landingzoneService.search(landingzoneDTO);
        try{
            for (Landingzone landingzone : landingzoneList) {
                logger.debug("Pulling all the aws elements for landing-zone:  {}",landingzone.getLandingZone());
                CloudElementDTO cloudElementDTO = new CloudElementDTO();
                cloudElementDTO.setLandingzoneId(landingzone.getId());
                List<CloudElement> cloudElementList = cloudElementService.search(cloudElementDTO);
                ObjectMapper objectMapper = Constants.instantiateMapper();
                for(CloudElement cloudElement: cloudElementList){
                    CloudElementCost cloudElementCost = cloudElementCostService.findByCloudElementId(cloudElement.getId());
                    if(cloudElementCost != null){
                        ObjectNode objectNode = objectMapper.createObjectNode();
                        try{
                            JSONObject obj = DateFormatUtil.generateTestCostData("2023-01-01", DateFormatUtil.convertLocalDateToString(LocalDate.now(), Constants.DEFAULT_DATE_FORMAT));
                            for(String key: obj.keySet()){
                                objectNode.put(key, objectMapper.readTree(obj.get(key).toString()));
                            }
                            ObjectNode finalNode =  objectMapper.createObjectNode();
                            finalNode.put("cost", objectNode);
                            Map map = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, finalNode, Map.class);
                            cloudElementCost.setCostJson(map);
                            cloudElementCostService.save(cloudElementCost);
                        }catch(Exception e){
                            logger.error("Cost pull failed for landing-zone: {} and element-type: {} failed. Exception: ",landingzone.getLandingZone(), cloudElement.getElementType(), e  );
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("exception: {}",e.getMessage() );
        }
    }

    @Scheduled(cron = "0 0 6 * * *") //At 06:00 AM
    public void processTag(){
        logger.info("Calling batch job to process appkube-tag");
        Config config = configService.findByKey(Constants.BATCH_JOB_AWS_ELEMENTS_AUTO_TAG_PROCESS);
        if(!Constants.ACTIVE.equalsIgnoreCase(config.getValue())){
            logger.warn("Batch job to process aws tags not active. Process skipped");
            return;
        }
        LandingzoneDTO landingzoneDTO = new LandingzoneDTO();
        landingzoneDTO.setCloud(Constants.AWS);
        landingzoneDTO.setStatus(Constants.ACTIVE);
        List<Landingzone> landingzoneList = landingzoneService.search(landingzoneDTO);
        for (Landingzone landingzone : landingzoneList) {
            logger.debug("Pulling all the aws elements for landing-zone: {}", landingzone.getLandingZone());
            CloudElementDTO cloudElementDTO = new CloudElementDTO();
            cloudElementDTO.setLandingzoneId(landingzone.getId());
            List<CloudElement> cloudElementList = cloudElementService.search(cloudElementDTO);
            for(CloudElement cloudElement: cloudElementList){
                for(String query: Constants.AWS_ELEMENT_QUERY){
                    logger.debug("Processing tag for element-type: {}",query);
                    try{
                        CloudHandler cloudHandler = AwsHandlerFactory.getHandlerByQuery(query);
                        if(cloudHandler == null){
                            logger.warn("Handler not found to process tag for element-type: {}",query);
                            continue;
                        }
                        cloudHandler.processTag(cloudElement);
                    }catch (Exception e){
                        logger.error("Auto tag processing failed for landing-zone: {} and element-type: {}. Exception: ",landingzone.getLandingZone(), query,e );
                    }
                }
            }
        }
    }
}
