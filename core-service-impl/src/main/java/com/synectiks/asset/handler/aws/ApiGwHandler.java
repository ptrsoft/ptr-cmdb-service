package com.synectiks.asset.handler.aws;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.*;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.service.CloudElementService;
import com.synectiks.asset.service.LandingzoneService;
import com.synectiks.asset.service.ProductEnclaveService;
import com.synectiks.asset.service.VaultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ApiGwHandler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(ApiGwHandler.class);

    private final Environment env;

    public ApiGwHandler(Environment env){
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
    public Object save(String elementType, Landingzone landingzone, String query) {
        Object response = getResponse(restTemplate, getUrl(elementType, String.valueOf(landingzone.getId()), query));
        List<CloudElement> cloudElementList = new ArrayList<>();
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map responseMap = (LinkedHashMap)response;
            List responseList = (List)responseMap.get("Items");
            for(Object obj: responseList){
                Map configMap = (Map)obj;
                cloudElementList.add(addUpdate(landingzone, configMap));
            }
        }
        return cloudElementList;
    }

    private CloudElement addUpdate(Landingzone landingZone, Map configMap) {
        String instanceId = (String)configMap.get("Id");
        CloudElement cloudElement =  cloudElementService.getCloudElementByInstanceId(landingZone.getId(), instanceId, Constants.APIGATEWAY);
        if(cloudElement != null ){
            logger.debug("Updating apigw: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            cloudElement.setConfigJson(configMap);
            cloudElement.setInstanceId(instanceId);
            cloudElement.setInstanceName((String)configMap.get("Name"));
            cloudElement = cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding apigw: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            cloudElement = CloudElement.builder()
                    .elementType(Constants.APIGATEWAY)
                    .instanceId(instanceId)
                    .instanceName((String)configMap.get("Name"))
                    .category(Constants.PERIPHERAL_SERVICES)
                    .landingzone(landingZone)
                    .configJson(configMap)
                    .cloud(landingZone.getCloud().toUpperCase())
                    .serviceCategory(Constants.OTHER)
                    .build();
            cloudElementService.save(cloudElement);
        }
        return cloudElement;
    }


    @Override
    public String getUrl(String elementType, String landingZoneId, String query){
        String baseUrl = env.getProperty("awsx-api.base-url");
        String param = "?elementType=landingZone&landingZoneId="+landingZoneId+"&query="+query;
        return baseUrl+param;
    }

//    @Override
//    public Map<String, List<Object>> processTag(CloudElement cloudElement){
//        List<Object> successTagging = new ArrayList<>();
//        List<Object> failureTagging = new ArrayList<>();
//
//        if(cloudElement.getConfigJson() != null ){
//            List tagList = (List)((Map)((Map)((Map)cloudElement.getConfigJson()).get("tags")).get("Tags")).get("Items");
//            if(tagList != null){
//                for(Object object: tagList){
//                    Map tag = (Map)object;
//                    if(tag.get("Key") != null &&  ((String)tag.get("Key")).toLowerCase().contains("appkube_tag")){
//                        String tagValue[] = ((String)tag.get("Value")).split("/");
//                        Map<String, Object> erroMap = validate(tagValue, cloudElement, tagProcessor);
//                        if(erroMap.size() > 0){
//                            logger.error("Validation error: ",erroMap.get("errorMsg"));
//                            tag.put("failure_reason",erroMap.get("errorMsg"));
//                            tag.put("error_code",erroMap.get("errorCode"));
//                            failureTagging.add(tag);
//                            continue;
//                        }
//                        int errorCode = tagProcessor.process(tagValue, cloudElement);
//                        if(errorCode == 0){
//                            successTagging.add(tag);
//                        }else{
//                            failureTagging.add(tag);
//                        }
//                    }
//                }
//            }
//        }
//        Map<String, List<Object>> statusMap = new HashMap<>();
//        statusMap.put("success", successTagging);
//        statusMap.put("failure",failureTagging);
//        return statusMap;
//    }
}
