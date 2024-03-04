package com.synectiks.asset.handler.aws;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.*;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CdnHandler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(CdnHandler.class);

    private final Environment env;

    public CdnHandler(Environment env){
        this.env = env;
    }

    @Autowired
    private CloudElementService cloudElementService;

    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private ProductEnclaveService productEnclaveService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VaultService vaultService;

    @Autowired
    private TagProcessor tagProcessor;

    @Override
    public void save(Organization organization, Department department, Landingzone landingZone, String awsRegion) {
        Object response = getResponse(vaultService, restTemplate, getUrl(), organization, department, landingZone, awsRegion);
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
            List list = (ArrayList)response;
            for(Object cdnObj: list){
                Map cdnMap = (Map)cdnObj;
                addUpdate(landingZone, cdnMap);
            }
        }
    }

    @Override
    public Object save(String elementType, Landingzone landingzone, String query) {
        Object response = getResponse(restTemplate, getUrl(elementType, String.valueOf(landingzone.getId()), query));
        List<CloudElement> cloudElementList = new ArrayList<>();
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
            List list = (ArrayList)response;
            for(Object cdnObj: list){
                Map cdnMap = (Map)cdnObj;
                cloudElementList.add(addUpdate(landingzone, cdnMap));
            }
        }
        return cloudElementList;
    }

    private CloudElement addUpdate(Landingzone landingZone, Map configMap) {
        String instanceId = (String)((Map)configMap.get("distribution")).get("Id");
        CloudElement cloudElement =  cloudElementService.getCloudElementByInstanceId(landingZone.getId(), instanceId, Constants.CDN);
        setAdditionalConfig(configMap);
        if(cloudElement != null ){
            logger.debug("Updating cdn: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            cloudElement.setConfigJson(configMap);
            cloudElement.setInstanceId(instanceId);
            cloudElement.setInstanceName(instanceId);
            cloudElement = cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding cdn: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            cloudElement = CloudElement.builder()
                    .elementType(Constants.CDN)
                    .arn((String)((Map)configMap.get("distribution")).get("ARN"))
                    .instanceId(instanceId)
                    .instanceName(instanceId)
                    .category(Constants.APP_SERVICES)
                    .landingzone(landingZone)
                    .configJson(configMap)
                    .cloud(landingZone.getCloud().toUpperCase())
                    .build();
            cloudElementService.save(cloudElement);
        }
        return cloudElement;
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.cdn-api");
    }

    @Override
    public String getUrl(String elementType, String landingZoneId, String query){
        String baseUrl = env.getProperty("awsx-api.base-url");
        String param = "?elementType=landingZone&landingZoneId="+landingZoneId+"&query="+query;
        return baseUrl+param;
    }

    // TODO: static values to be changed with actual values
    private void setAdditionalConfig(Map configMap){
        configMap.put("originName", (String)((Map)configMap.get("distribution")).get("Id"));
        configMap.put("edges", "NorthStar");
        configMap.put("request", "136k");
        configMap.put("byteTransfer", "125mb");
        configMap.put("cacheHit", "58%");
        configMap.put("errors", "95");
        configMap.put("latency", "150ms");
        configMap.put("invalidation", "52");
        configMap.put("byteHitRate", "58%");
        configMap.put("product", "Procurement");
        configMap.put("environment", "PROD");
    }

    @Override
    public Map<String, List<Object>> processTag(CloudElement cloudElement){
        List<Object> successTagging = new ArrayList<>();
        List<Object> failureTagging = new ArrayList<>();

        if(cloudElement.getConfigJson() != null ){
            List tagList = (List)((Map)((Map)((Map)cloudElement.getConfigJson()).get("tags")).get("Tags")).get("Items");
            if(tagList != null){
                for(Object object: tagList){
                    Map tag = (Map)object;
                    if(tag.get("Key") != null &&  ((String)tag.get("Key")).toLowerCase().contains("appkube_tag")){
                        String tagValue[] = ((String)tag.get("Value")).split("/");
                        Map<String, Object> erroMap = validate(tagValue, cloudElement, tagProcessor);
                        if(erroMap.size() > 0){
                            logger.error("Validation error: ",erroMap.get("errorMsg"));
                            tag.put("failure_reason",erroMap.get("errorMsg"));
                            tag.put("error_code",erroMap.get("errorCode"));
                            failureTagging.add(tag);
                            continue;
                        }
                        int errorCode = tagProcessor.process(tagValue, cloudElement);
                        if(errorCode == 0){
                            successTagging.add(tag);
                        }else{
                            failureTagging.add(tag);
                        }
                    }
                }
            }
        }
        Map<String, List<Object>> statusMap = new HashMap<>();
        statusMap.put("success", successTagging);
        statusMap.put("failure",failureTagging);
        return statusMap;
    }
}
