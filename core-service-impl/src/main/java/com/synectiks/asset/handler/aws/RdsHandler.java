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

    @Autowired
    private TagProcessor tagProcessor;

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

    @Override
    public Object save(String elementType, Landingzone landingzone, String query) {
        Object response = getResponse(restTemplate, getUrl(elementType, String.valueOf(landingzone.getId()), query));
        List<CloudElement> cloudElementList = new ArrayList<>();
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
            List responseList = (ArrayList)response;
            for(Object respObj: responseList){
                Map responseMap = (Map)respObj;
                List dbInstanceList = (List)responseMap.get("DBInstances");
                for(Object obj: dbInstanceList) {
                    cloudElementList.add(addUpdate(landingzone, (Map) obj));
                }
            }
        }
        return cloudElementList;
    }

    private CloudElement addUpdate(Landingzone landingZone, Map configMap) {
        ProductEnclave productEnclave = null;
        if(configMap.containsKey("DBSubnetGroup")){
            if(((Map)configMap.get("DBSubnetGroup")).containsKey("VpcId")) {
                String vpcId = (String)((Map)configMap.get("DBSubnetGroup")).get("VpcId");
                productEnclave = productEnclaveService.findProductEnclave(vpcId, landingZone.getDepartment().getId(), landingZone.getId());
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
            cloudElement = cloudElementService.save(cloudElement);
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
                .cloud(landingZone.getCloud().toUpperCase())
                .build();
            cloudElement = cloudElementService.save(cloudElementObj);
        }
        return cloudElement;
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.rds-api");
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
            List tagList = (List)((Map)cloudElement.getConfigJson()).get("TagList");
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
