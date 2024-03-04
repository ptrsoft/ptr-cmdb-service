package com.synectiks.asset.handler.aws;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.Department;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.domain.Organization;
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
public class KinesysHandler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(KinesysHandler.class);

    private final Environment env;

    public KinesysHandler(Environment env){
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
            List responseList = (ArrayList)response;
            for(Object obj: responseList){
                Map configMap = (Map)obj;
                addUpdate(landingZone, configMap);
            }
        }
    }

    @Override
    public Object save(String elementType, Landingzone landingzone, String query) {
        Object response = getResponse(restTemplate, getUrl(elementType, String.valueOf(landingzone.getId()), query));
        List<CloudElement> cloudElementList = new ArrayList<>();
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
            List responseList = (ArrayList)response;
            for(Object obj: responseList){
                Map configMap = (Map)obj;
                cloudElementList.add(addUpdate(landingzone, configMap));
            }
        }
        return cloudElementList;
    }

    private CloudElement addUpdate(Landingzone landingZone, Map configMap) {
        String instanceId = (String)((Map)((Map)configMap.get("stream")).get("StreamDescription")).get("StreamName");
        CloudElement cloudElement =  cloudElementService.getCloudElementByInstanceId(landingZone.getId(), instanceId, Constants.KINESYS);
        if(cloudElement != null ){
            logger.debug("Updating kinesys: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            cloudElement.setConfigJson(configMap);
            cloudElement.setInstanceId(instanceId);
            cloudElement.setInstanceName(instanceId);
            cloudElement = cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding kinesys: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            CloudElement cloudElementObj = CloudElement.builder()
                    .elementType(Constants.KINESYS)
                    .arn((String)((Map)((Map)configMap.get("stream")).get("StreamDescription")).get("StreamARN"))
                    .instanceId(instanceId)
                    .instanceName(instanceId)
                    .category(Constants.APP_SERVICES)
                    .landingzone(landingZone)
                    .configJson(configMap)
                    .cloud(landingZone.getCloud().toUpperCase())
                    .build();
            cloudElement = cloudElementService.save(cloudElementObj);
        }
        return cloudElement;
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.kinesys-api");
    }

    @Override
    public String getUrl(String elementType, String landingZoneId, String query){
        String baseUrl = env.getProperty("awsx-api.base-url");
        String param = "?elementType=landingZone&landingZoneId="+landingZoneId+"&query="+query;
        return baseUrl+param;
    }
    @Override
    public Map<String, List<Object>> processTag(CloudElement cloudElement){
        List<Object> successTagging = new ArrayList<>();
        List<Object> failureTagging = new ArrayList<>();

        if(cloudElement.getConfigJson() != null ){
            List tagList = (List)((Map)(((Map)cloudElement.getConfigJson()).get("tags"))).get("Tags");
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
