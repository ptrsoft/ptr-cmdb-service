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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LambdaHandler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(LambdaHandler.class);

    private final Environment env;

    public LambdaHandler(Environment env){
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
        String vaultAccountKey =  vaultService.resolveVaultKey(organization.getName(), department.getName(), Constants.AWS, landingZone.getLandingZone());
        String params = "?zone="+awsRegion+"&vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey;
        if(StringUtils.isBlank(awsRegion)){
            params = "?vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey;
        }

        String awsxUrl = getUrl()+params;
        Object lambdaResponse = this.restTemplate.getForObject(awsxUrl, Object.class);
        if(lambdaResponse != null && lambdaResponse.getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
            List lambdaList = (ArrayList)lambdaResponse;
            for(Object lambdaObj: lambdaList){
                Map lambdaMap = (Map)lambdaObj;
                addUpdate(landingZone, lambdaMap);
            }
        }else if(lambdaResponse != null && lambdaResponse.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map lambdaMap = (LinkedHashMap)lambdaResponse;
            addUpdate(landingZone, lambdaMap);
        }
    }

    private void addUpdate(Landingzone landingZone, Map lambdaMap) {
        CloudElement cloudElement =  cloudElementService.getCloudElementByArn(landingZone.getId(), (String)lambdaMap.get("FunctionArn"), Constants.LAMBDA);
        if(cloudElement != null){
                logger.debug("Updating lambda {} cloud-element for existing landing-zone: {}", (String)lambdaMap.get("FunctionName"), landingZone.getLandingZone());
                cloudElement.setConfigJson(lambdaMap);
                cloudElement.setInstanceId((String)lambdaMap.get("FunctionName"));
                cloudElement.setInstanceName((String)lambdaMap.get("FunctionName"));
                cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding lambda {} cloud-element for existing landing-zone: {}", (String)lambdaMap.get("FunctionName"), landingZone.getLandingZone());
            CloudElement cloudElementObj = CloudElement.builder()
                .elementType(Constants.LAMBDA)
                .arn((String) lambdaMap.get("FunctionArn"))
                .instanceId((String) lambdaMap.get("FunctionName"))
                .instanceName((String) lambdaMap.get("FunctionName"))
                .category(Constants.APP_SERVICES)
                .landingzone(landingZone)
                .configJson(lambdaMap)
                .build();
            cloudElementService.save(cloudElementObj);
        }
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.lambda-api");
    }

}
