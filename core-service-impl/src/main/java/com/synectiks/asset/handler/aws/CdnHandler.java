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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void save(Organization organization, Department department, Landingzone landingZone, String awsRegion) {
        Object response = getResponse(vaultService, restTemplate, getUrl(), organization, department, landingZone, awsRegion);
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map responseMap = (LinkedHashMap)response;
            Map functionList = (Map)responseMap.get("FunctionList");
            List itemList = (List)functionList.get("Items");
            for(Object obj: itemList){
                Map configMap = (Map)obj;
                addUpdate(department, landingZone, configMap);
            }
        }
    }

    private void addUpdate(Department department, Landingzone landingZone, Map configMap) {
        String instanceId = (String)configMap.get("Name");
        CloudElement cloudElement =  cloudElementService.getCloudElementByInstanceId(landingZone.getId(), instanceId, Constants.RDS);
        setAdditionalConfig(configMap);
        if(cloudElement != null ){
            logger.debug("Updating cdn: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            cloudElement.setConfigJson(configMap);
            cloudElement.setInstanceId(instanceId);
            cloudElement.setInstanceName(instanceId);
            cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding cdn: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            CloudElement cloudElementObj = CloudElement.builder()
                    .elementType(Constants.CDN)
                    .arn((String) ((Map)configMap.get("FunctionMetadata")).get("FunctionARN"))
                    .instanceId(instanceId)
                    .instanceName(instanceId)
                    .category(Constants.APP_SERVICES)
                    .landingzone(landingZone)
                    .configJson(configMap)
                    .build();
            cloudElementService.save(cloudElementObj);
        }
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.cdn-api");
    }

    // TODO: static values to be changed with actual values
    private void setAdditionalConfig(Map configMap){
        configMap.put("originName", (String)configMap.get("Name"));
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
}
