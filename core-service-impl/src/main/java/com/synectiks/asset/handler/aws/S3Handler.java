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
public class S3Handler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(S3Handler.class);

    private final Environment env;

    public S3Handler(Environment env){
        this.env = env;
    }

    @Autowired
    private CloudElementService cloudElementService;

    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private ProductEnclaveService productEnclaveService;

    @Autowired
    private DbCategoryService dbCategoryService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VaultService vaultService;

    @Override
    public void save(Organization organization, Department department, Landingzone landingZone, String awsRegion) {
        Object response = getResponse(vaultService, restTemplate, getUrl(), organization, department, landingZone, awsRegion);
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map responseMap = (LinkedHashMap)response;
            List responseList = (List)responseMap.get("Buckets");
            for(Object obj: responseList){
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
            logger.debug("Updating s3: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            cloudElement.setConfigJson(configMap);
            cloudElement.setInstanceId(instanceId);
            cloudElement.setInstanceName(instanceId);
            cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding s3: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            DbCategory dbCategory = dbCategoryService.findByName(Constants.OBJECT_DB);
            CloudElement cloudElementObj = CloudElement.builder()
                    .elementType(Constants.S3)
                    .instanceId(instanceId)
                    .instanceName(instanceId)
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
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.s3-api");
    }

    // TODO: static values to be changed with actual values
    private void setAdditionalConfig(Map configMap){
        configMap.put("bucketName", (String)configMap.get("Name"));
        configMap.put("replication", "replicated-files");
        configMap.put("objects", "136k");
        configMap.put("dataTransfer", "125mb");
        configMap.put("responseTime", "3.5ms");
        configMap.put("errors", "95");
        configMap.put("latency", "22");
        configMap.put("totalStorage", "200mb");
        configMap.put("requests", "230");
        configMap.put("product", "Procurement");
        configMap.put("environment", "PROD");
    }
}
