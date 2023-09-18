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
public class RdsHandler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(RdsHandler.class);

    private final Environment env;

    public RdsHandler(Environment env){
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
                List dbInstanceList = (List)responseMap.get("DBInstances");
                for(Object obj: dbInstanceList) {
                    addUpdate(department, landingZone, (Map) obj);
                }
            }
        }
    }

    private void addUpdate(Department department, Landingzone landingZone, Map configMap) {
        ProductEnclave productEnclave = null;
        if(configMap.containsKey("DBSubnetGroup")){
            if(((Map)configMap.get("DBSubnetGroup")).containsKey("VpcId")) {
                String vpcId = (String)((Map)configMap.get("DBSubnetGroup")).get("VpcId");
                productEnclave = productEnclaveService.findProductEnclave(vpcId, department.getId(), landingZone.getId());
            }
        }

        String instanceId = (String)configMap.get("DbiResourceId");
        CloudElement cloudElement =  cloudElementService.getCloudElementByInstanceId(landingZone.getId(), instanceId, Constants.RDS);
        if(cloudElement != null ){
            logger.debug("Updating rds: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            cloudElement.setConfigJson(configMap);
            cloudElement.setInstanceId(instanceId);
            cloudElement.setInstanceName(instanceId);
            cloudElement.setProductEnclave(productEnclave);
            cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding rds: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            DbCategory dbCategory = dbCategoryService.findByName(Constants.SQL_DB);
            CloudElement cloudElementObj = CloudElement.builder()
                .elementType(Constants.RDS)
                .arn((String)configMap.get("DBInstanceArn"))
                .instanceId(instanceId)
                .instanceName(instanceId)
                .category(Constants.DATA_SERVICES)
                .landingzone(landingZone)
                .configJson(configMap)
                .dbCategory(dbCategory)
                .productEnclave(productEnclave)
                .build();
            cloudElementService.save(cloudElementObj);
        }
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.rds-api");
    }

}
