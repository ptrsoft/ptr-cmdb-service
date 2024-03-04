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
public class KmsHandler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(KmsHandler.class);

    private final Environment env;

    public KmsHandler(Environment env){
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
        String instanceId = (String)((Map)configMap.get("KeyMetadata")).get("KeyId");
        CloudElement cloudElement =  cloudElementService.getCloudElementByInstanceId(landingZone.getId(), instanceId, Constants.KMS);
        if(cloudElement != null ){
            logger.debug("Updating kms: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            cloudElement.setConfigJson(configMap);
            cloudElement.setInstanceId(instanceId);
            cloudElement.setInstanceName(instanceId);
            cloudElement = cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding kms: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            cloudElement = CloudElement.builder()
                    .elementType(Constants.KMS)
                    .arn((String)((Map)configMap.get("KeyMetadata")).get("Arn"))
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
    public String getUrl(String elementType, String landingZoneId, String query){
        String baseUrl = env.getProperty("awsx-api.base-url");
        String param = "?elementType=landingZone&landingZoneId="+landingZoneId+"&query="+query;
        return baseUrl+param;
    }
}
