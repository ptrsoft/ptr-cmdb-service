package com.synectiks.asset.handler.aws;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.*;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.service.*;
import org.apache.commons.lang3.StringUtils;
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
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map map = (Map)response;
            if(map != null && map.size() > 0){
                List list = (ArrayList)map.get("Buckets");
                for(Object s3Obj: list){
                    Map configMap = (Map)s3Obj;
                    addUpdate(landingZone, configMap);
                }
            }
        }
    }

    @Override
    public Object save(String elementType, Landingzone landingzone, String query) {
        Object response = getResponse(restTemplate, getUrl(elementType, String.valueOf(landingzone.getId()), query));
        List<CloudElement> cloudElementList = new ArrayList<>();
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map map = (Map)response;
            if(map != null && map.size() > 0){
                List list = (ArrayList)map.get("Buckets");
                for(Object s3Obj: list){
                    Map configMap = (Map)s3Obj;
                    cloudElementList.add(addUpdate(landingzone, configMap));
                }
            }
        }
        return cloudElementList;
    }

    private CloudElement addUpdate(Landingzone landingZone, Map configMap) {
        String instanceId = (String) configMap.get("Name");
        CloudElement cloudElement =  cloudElementService.getCloudElementByInstanceId(landingZone.getId(), instanceId, Constants.S3);
        setAdditionalConfig(configMap);
        Map<String, Object> bucketMap = new HashMap();
        bucketMap.put("bucket", configMap);
        if(cloudElement != null ){
            logger.debug("Updating s3: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            cloudElement.setConfigJson(bucketMap);
            cloudElement.setInstanceId(instanceId);
            cloudElement.setInstanceName(instanceId);
            cloudElement = cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding s3: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            DbCategory dbCategory = dbCategoryService.findByName(Constants.OBJECT_DB);
            CloudElement cloudElementObj = CloudElement.builder()
                    .elementType(Constants.S3)
                    .instanceId(instanceId)
                    .instanceName(instanceId)
                    .category(Constants.DATA_SERVICES)
                    .landingzone(landingZone)
                    .configJson(bucketMap)
                    .dbCategory(dbCategory)
                    .cloud(landingZone.getCloud().toUpperCase())
                    .serviceCategory(Constants.STORAGE)
                    .build();
            cloudElement = cloudElementService.save(cloudElementObj);
        }
        return cloudElement;
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.s3-api");
    }

    @Override
    public String getUrl(String elementType, String landingZoneId, String query){
        String baseUrl = env.getProperty("awsx-api.base-url");
        String param = "?elementType=landingZone&landingZoneId="+landingZoneId+"&query="+query;
        return baseUrl+param;
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
        Object resp = getTags(cloudElement.getLandingzone(), null, (String)((Map)((Map)cloudElement.getConfigJson()).get("bucket")).get("Name"));
        List<Object> successTagging = new ArrayList<>();
        List<Object> failureTagging = new ArrayList<>();

        if(resp != null ){
            Map tags = (Map)((Map)resp).get("tags");
            if(tags != null){
                List tagList = (List)tags.get("TagSet");
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
                Map configutation = (Map)((Map)resp).get("bucket");
                setAdditionalConfig(configutation);
                ((Map)resp).put("bucket", configutation);
                cloudElement.setConfigJson((Map)resp);
                cloudElementService.save(cloudElement);
            }

        }
        Map<String, List<Object>> statusMap = new HashMap<>();
        statusMap.put("success", successTagging);
        statusMap.put("failure",failureTagging);
        return statusMap;
    }

    public Object getTags(Landingzone landingZone, String awsRegion, String bucketName) {
        logger.info("Getting tags for s3 bucket: {}",bucketName);
        String url = env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.s3-tag-api");
        String vaultAccountKey =  vaultService.resolveVaultKey(landingZone.getDepartment().getOrganization().getName(), landingZone.getDepartment().getName(), Constants.AWS, landingZone.getLandingZone());
        String params = "?zone="+ awsRegion +"&vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey+"&bucketName="+bucketName;
        if(StringUtils.isBlank(awsRegion)){
            params = "?vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey+"&bucketName="+bucketName;
        }
        String awsxUrl = url+params;
        Object response = restTemplate.getForObject(awsxUrl, Object.class);
        return response;
    }
}
