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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DynamodbHandler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(DynamodbHandler.class);

    private final Environment env;

    public DynamodbHandler(Environment env){
        this.env = env;
    }
    @Autowired
    private CloudElementService cloudElementService;

    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private DbCategoryService dbCategoryService;

    @Autowired
    private ProductEnclaveService productEnclaveService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VaultService vaultService;

    @Override
    public void save(Organization organization, Department department, Landingzone landingZone, String awsRegion) {
        Object response = getResponse(vaultService, restTemplate, getUrl(), organization, department, landingZone, awsRegion);
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
            List responseList = (ArrayList)response;
            for(Object respObj: responseList){
                Map responseMap = (Map)respObj;
                Map configMap = (Map)responseMap.get("Table");
                addUpdate(department, landingZone, configMap);
            }
        }
    }

    private void addUpdate(Department department, Landingzone landingZone, Map configMap) {
        String instanceId = (String)configMap.get("TableId");
        CloudElement cloudElement =  cloudElementService.getCloudElementByInstanceId(landingZone.getId(), instanceId, Constants.DYNAMODB);
        if(cloudElement != null ){
            logger.debug("Updating dynamodb: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            cloudElement.setConfigJson(configMap);
            cloudElement.setInstanceId(instanceId);
            cloudElement.setInstanceName((String)configMap.get("TableName"));
            cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding dynamodb: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            DbCategory dbCategory = dbCategoryService.findByName(Constants.NO_SQL_DB);
            CloudElement cloudElementObj = CloudElement.builder()
                .elementType(Constants.DYNAMODB)
                .arn((String)configMap.get("TableArn"))
                .instanceId(instanceId)
                .instanceName((String)configMap.get("TableName"))
                .category(Constants.DATA_SERVICES)
                .landingzone(landingZone)
                .configJson(configMap)
                .dbCategory(dbCategory)
                .build();
            cloudElementService.save(cloudElementObj);
        }
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.dynamodb-api");
    }

}
