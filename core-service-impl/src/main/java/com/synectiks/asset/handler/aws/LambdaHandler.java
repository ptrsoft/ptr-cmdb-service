package com.synectiks.asset.handler.aws;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.service.CloudElementService;
import com.synectiks.asset.service.LandingzoneService;
import com.synectiks.asset.service.VaultService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LambdaHandler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(LambdaHandler.class);

    @Autowired
    private CloudElementService cloudElementService;

    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VaultService vaultService;

    @Override
    public void save(String organization, String department, String landingZone, String awsRegion) {
        String vaultAccountKey =  vaultService.resolveVaultKey(organization, department, Constants.AWS, landingZone);
        String params = "?zone="+awsRegion+"&vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey;
        if(StringUtils.isBlank(awsRegion)){
            params = "?vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey;
        }
        String appConfigUrl = Constants.AWSX_API_LAMBDA_URL+params;

        Object lambdaResponse = this.restTemplate.getForObject(appConfigUrl, Object.class);
        if(lambdaResponse != null && lambdaResponse.getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
            List lambdaList = (ArrayList)lambdaResponse;
            for(Object lambdaObj: lambdaList){
                Map lambdaMap = (Map)lambdaObj;
                addUpdate(organization, department, landingZone, lambdaMap);
            }
        }else if(lambdaResponse != null && lambdaResponse.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map lambdaMap = (LinkedHashMap)lambdaResponse;
            addUpdate(organization, department, landingZone, lambdaMap);
        }
    }

    private void addUpdate(String organization, String department, String landingZone, Map lambdaMap) {
        List<CloudElement> cloudElementList =  cloudElementService.getCloudElement(organization, department, Constants.AWS, landingZone, (String)lambdaMap.get("FunctionArn"), Constants.LAMBDA);
        if(cloudElementList != null && cloudElementList.size() > 0){
            logger.debug("Updating lambda {}",(String)lambdaMap.get("FunctionName"));
            for(CloudElement cloudElement: cloudElementList){
                if(((String)lambdaMap.get("FunctionArn")).equalsIgnoreCase(cloudElement.getArn())){
                    logger.debug("Updating lambda cloud-element for existing landing-zone: {}", landingZone);
                    cloudElement.setConfigJson(lambdaMap);
                    cloudElement.setInstanceId((String)lambdaMap.get("FunctionName"));
                    cloudElement.setInstanceName((String)lambdaMap.get("FunctionName"));
                    cloudElementService.save(cloudElement);
                }
            }
        }else{
            logger.debug("Adding lambda {}",(String)lambdaMap.get("FunctionName"));
            List<Landingzone> landingzoneList =  landingzoneService.getLandingZone(organization, department, Constants.AWS, landingZone);
            for(Landingzone landingzone: landingzoneList){
                logger.debug("Adding lambda cloud-element for landing-zone: {}", landingZone);
                CloudElement cloudElementObj = CloudElement.builder()
                        .elementType(Constants.LAMBDA)
                        .arn((String) lambdaMap.get("FunctionArn"))
                        .instanceId((String) lambdaMap.get("FunctionName"))
                        .instanceName((String) lambdaMap.get("FunctionName"))
                        .category(Constants.APP_SERVICES)
                        .landingzone(landingzone)
                        .configJson(lambdaMap)
                        .build();
                cloudElementService.save(cloudElementObj);
            }
        }
    }



}
