package com.synectiks.asset.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.api.controller.InfraDiscoveryApi;
import com.synectiks.asset.api.model.CloudElementDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.domain.ServiceQueue;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.handler.factory.AwsHandlerFactory;
import com.synectiks.asset.service.CloudElementService;
import com.synectiks.asset.service.ConfigService;
import com.synectiks.asset.service.LandingzoneService;
import com.synectiks.asset.service.ServiceQueueService;
import com.synectiks.asset.util.DateFormatUtil;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class InfraDiscoveryController implements InfraDiscoveryApi {

    private final Logger logger = LoggerFactory.getLogger(InfraDiscoveryController.class);
    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private CloudElementService cloudElementService;

    @Autowired
    private ServiceQueueService serviceQueueService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;


    @Override

    public ResponseEntity<Object> discoverCloudElements(Long orgId, String elementType, Long landingZoneId, String query )  {
        logger.debug("REST request to pull aws element details. Org id: {}, landingZoneId: {}, elementType: {}, query: {}", orgId, landingZoneId, elementType, query);
        Optional<Landingzone> oLz = landingzoneService.findOne(landingZoneId);
        if(!oLz.isPresent()){
            logger.error("landingZoneId does not exists");
            return ResponseUtil.wrapOrNotFound(Optional.empty());
        }
        CloudHandler cloudHandler = AwsHandlerFactory.getHandlerByQuery(query);
        Object object = cloudHandler.save(elementType, oLz.get(), query);
        return ResponseEntity.ok(object);
    }

    @Scheduled(cron = "* */1 * * * ?")
    public void pullAwsElementJob(){
        logger.info("calling batch job to pull aws elements");
        List<ServiceQueue> serviceQueueList = serviceQueueService.findByKeyAndStatus(Constants.LANDING_ZONE, Constants.NEW);
        for(ServiceQueue serviceQueue: serviceQueueList){
            serviceQueue.setStatus(Constants.IN_PROCESS);
            serviceQueueService.save(serviceQueue);
            try{
                Long landingZoneId = Long.parseLong(serviceQueue.getValue());
                Optional<Landingzone> oLz = landingzoneService.findOne(landingZoneId);
                if(oLz.isPresent()){
                    logger.info("Pulling all the aws elements for landing-zone:  {}",oLz.get().getLandingZone());
                    String awsQueries[] = Constants.AWS_ELEMENT_QUERY.split(",");
                    for(String query: awsQueries){
                        logger.debug("pulling: {}",query);
                        CloudHandler cloudHandler = AwsHandlerFactory.getHandlerByQuery(query);
                        Object object = cloudHandler.save("landingZone", oLz.get(), query);
                    }
                }
                serviceQueue.setStatus(Constants.COMPLETED);
                serviceQueueService.save(serviceQueue);
            }catch (Exception e){
                logger.error("exception: ",e );
                serviceQueue.setStatus(Constants.COMPLETED);
                serviceQueueService.save(serviceQueue);
            }
        }
    }

    @Scheduled(cron = "* */1 * * * ?")
    public void generateSimulatedCost(){
        logger.info("calling batch job to generate simulated cost");
        List<ServiceQueue> serviceQueueList = serviceQueueService.findByKeyAndStatus(Constants.LANDING_ZONE, Constants.COMPLETED);
        for(ServiceQueue serviceQueue: serviceQueueList){
            serviceQueue.setStatus(Constants.COST_IN_PROCESS);
            serviceQueue = serviceQueueService.save(serviceQueue);
            try{
                Long landingZoneId = Long.parseLong(serviceQueue.getValue());
                Optional<Landingzone> oLz = landingzoneService.findOne(landingZoneId);
                if(oLz.isPresent()){
                    logger.info("Pulling all the aws elements for landing-zone:  {}",oLz.get().getLandingZone());
                    CloudElementDTO cloudElementDTO = new CloudElementDTO();
                    cloudElementDTO.setLandingzoneId(landingZoneId);
                    List<CloudElement> cloudElementList = cloudElementService.search(cloudElementDTO);
                    ObjectMapper objectMapper = Constants.instantiateMapper();
                    for(CloudElement cloudElement: cloudElementList){
                        ObjectNode objectNode = objectMapper.createObjectNode();
                        try{
                            JSONObject obj = DateFormatUtil.generateTestCostData("2023-01-01", "2024-05-31");
                            for(String key: obj.keySet()){
                                objectNode.put(key, objectMapper.readTree(obj.get(key).toString()));
                            }
                            ObjectNode finalNode =  objectMapper.createObjectNode();
                            finalNode.put("cost", objectNode);
                            System.out.println(finalNode.toString());
                            Map map = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, finalNode, Map.class);
                            cloudElement.setCostJson(map);
                            cloudElementService.save(cloudElement);
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                }
                serviceQueue.setStatus(Constants.COST_COMPLETED);
                serviceQueueService.save(serviceQueue);
            }catch (Exception e){
                logger.error("exception: ",e );
                serviceQueue.setStatus(Constants.COST_COMPLETED);
                serviceQueueService.save(serviceQueue);
            }
        }
    }
}
