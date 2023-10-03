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

import java.util.*;

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

    @Autowired
    private TagProcessor tagProcessor;


    @Override
    public void save(Organization organization, Department department, Landingzone landingZone, String awsRegion) {
        Object response = getResponse(vaultService, restTemplate, getUrl(), organization, department, landingZone, awsRegion);
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
            List list = (ArrayList)response;
            for(Object cdnObj: list){
                Map cdnMap = (Map)cdnObj;
                addUpdate(department, landingZone, cdnMap);
            }
        }
    }

    private void addUpdate(Department department, Landingzone landingZone, Map configMap) {
        String instanceId = (String) ((Map)configMap.get("bucket")).get("Name");
        CloudElement cloudElement =  cloudElementService.getCloudElementByInstanceId(landingZone.getId(), instanceId, Constants.S3);
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

    @Override
    public Map<String, List<Object>> processTag(CloudElement cloudElement){
        List<Object> successTagging = new ArrayList<>();
        List<Object> failureTagging = new ArrayList<>();

        if(cloudElement.getConfigJson() != null ){
            List tagList = (List)((Map)((Map)cloudElement.getConfigJson()).get("tags")).get("TagSet");
//            List tagList = (List)((Map)((Map)((Map)cloudElement.getConfigJson()).get("tags")).get("Tags")).get("Items");
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
