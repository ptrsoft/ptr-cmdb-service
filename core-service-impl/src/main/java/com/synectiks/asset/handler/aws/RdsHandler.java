package com.synectiks.asset.handler.aws;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.Department;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.domain.Organization;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.service.CloudElementService;
import com.synectiks.asset.service.LandingzoneService;
import com.synectiks.asset.service.VaultService;
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
                    addUpdate(landingZone, (Map) obj);
                }
            }
        }
    }

    private void addUpdate(Landingzone landingZone, Map configMap) {
        String instanceId = (String)configMap.get("DbiResourceId");
        CloudElement cloudElement =  cloudElementService.getCloudElementByInstanceId(landingZone.getId(), instanceId, Constants.RDS);
        if(cloudElement != null ){
            logger.debug("Updating rds: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            cloudElement.setConfigJson(configMap);
            cloudElement.setInstanceId(instanceId);
            cloudElement.setInstanceName(instanceId);
            cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding rds: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            CloudElement cloudElementObj = CloudElement.builder()
                .elementType(Constants.RDS)
                .arn((String)configMap.get("DBInstanceArn"))
                .instanceId(instanceId)
                .instanceName(instanceId)
                .category(Constants.DATA_SERVICES)
                .landingzone(landingZone)
                .configJson(configMap)
                .build();
            cloudElementService.save(cloudElementObj);
        }
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.rds-api");
    }

}
