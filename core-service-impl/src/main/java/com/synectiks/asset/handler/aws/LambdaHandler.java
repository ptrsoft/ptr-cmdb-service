package com.synectiks.asset.handler.aws;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.*;
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

import java.util.*;

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

    @Autowired
    private TagProcessor tagProcessor;

    @Override
    public void save(Organization organization, Department department, Landingzone landingZone, String awsRegion) {
        Object response = getResponse(vaultService, restTemplate, getUrl(), organization, department, landingZone, awsRegion);
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
            List lambdaList = (ArrayList)response;
            for(Object lambdaObj: lambdaList){
                Map lambdaMap = (Map)lambdaObj;
                addUpdate(landingZone, lambdaMap);
            }
        }else if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map lambdaMap = (LinkedHashMap)response;
            addUpdate(landingZone, lambdaMap);
        }
    }

    @Override
    public Object save(String elementType, Landingzone landingzone, String query) {
        Object response = getResponse(restTemplate, getUrl(elementType, String.valueOf(landingzone.getId()), query));
        List<CloudElement> cloudElementList = new ArrayList<>();
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
            List responseList = (ArrayList)response;
            for(Object obj: responseList){
                Map configMap = (Map)obj;
                cloudElementList.addAll(addUpdate(landingzone, configMap));
            }
        }else if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map configMap = (LinkedHashMap)response;
            cloudElementList.addAll(addUpdate(landingzone, configMap));
        }
        return cloudElementList;
    }
    private  List<CloudElement> addUpdate(Landingzone landingZone, Map configMap) {
        List functionList = (List)configMap.get("Functions");
        List<CloudElement> cloudElementList = new ArrayList<>();
        ProductEnclave productEnclave = null;
        for(Object obj: functionList){
            Map lambdaMap = (Map)obj;
            if(lambdaMap.containsKey("VpcConfig")){
                if(lambdaMap.get("VpcConfig") != null && ((Map)lambdaMap.get("VpcConfig")).containsKey("VpcId")
                        && ((Map)lambdaMap.get("VpcConfig")).get("VpcId") != null) {
                    String vpcId = (String)((Map)lambdaMap.get("VpcConfig")).get("VpcId");
                    productEnclave = productEnclaveService.findProductEnclave(vpcId, landingZone.getDepartment().getId(), landingZone.getId());
                    if (productEnclave == null){
                        logger.warn("product enclave (vpc) is null for lambda: {}",(String)lambdaMap.get("FunctionName"));
                    }
                }
            }

            CloudElement cloudElement =  cloudElementService.getCloudElementByArn(landingZone.getId(), (String)lambdaMap.get("FunctionArn"), Constants.LAMBDA);
            Map<String, Object> functionMap = new HashMap();
            functionMap.put("function", lambdaMap);
            if(cloudElement != null){
                logger.debug("Updating lambda {} cloud-element for existing landing-zone: {}", (String)lambdaMap.get("FunctionName"), landingZone.getLandingZone());
                cloudElement.setConfigJson(functionMap);
                cloudElement.setInstanceId((String)lambdaMap.get("FunctionName"));
                cloudElement.setInstanceName((String)lambdaMap.get("FunctionName"));
                cloudElement.setProductEnclave(productEnclave);
                cloudElement = cloudElementService.save(cloudElement);
            }else{
                logger.debug("Adding lambda {} cloud-element for existing landing-zone: {}", (String)lambdaMap.get("FunctionName"), landingZone.getLandingZone());
                CloudElement cloudElementObj = CloudElement.builder()
                        .elementType(Constants.LAMBDA)
                        .arn((String) lambdaMap.get("FunctionArn"))
                        .instanceId((String) lambdaMap.get("FunctionName"))
                        .instanceName((String) lambdaMap.get("FunctionName"))
                        .category(Constants.APP_SERVICES)
                        .landingzone(landingZone)
                        .configJson(functionMap)
                        .productEnclave(productEnclave)
                        .cloud(landingZone.getCloud().toUpperCase())
                        .build();
                cloudElement = cloudElementService.save(cloudElementObj);
            }
            cloudElementList.add(cloudElement);
        }

        return cloudElementList;
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.lambda-api");
    }
    @Override
    public String getUrl(String elementType, String landingZoneId, String query){
        String baseUrl = env.getProperty("awsx-api.base-url");
        String param = "?elementType=landingZone&landingZoneId="+landingZoneId+"&query="+query;
        return baseUrl+param;
    }
    @Override
    public Map<String, List<Object>> processTag(CloudElement cloudElement){
        // first get tags from aws

        Object resp = getTags(cloudElement.getLandingzone(), null, (String)((Map)((Map)cloudElement.getConfigJson()).get("function")).get("FunctionName"));
        List<Object> successTagging = new ArrayList<>();
        List<Object> failureTagging = new ArrayList<>();

        if(resp != null ){
            Map tag = (Map)((Map)resp).get("tags");
            if(tag != null){
                for (Object key : tag.keySet()){
                    String tagKey = (String)key;
                    if(tagKey.toLowerCase().contains("appkube_tag")){
                        String tagValue[] = ((String)tag.get(tagKey)).split("/");
                        Map<String, Object> erroMap = validate(tagValue, cloudElement, tagProcessor);
                        if(erroMap.size() > 0){
                            logger.error("Validation error: ",erroMap.get("errorMsg"));
                            failureTagging.add(tagKey+" - "+(String)tag.get(tagKey));
                        }else{
                            int errorCode = tagProcessor.process(tagValue, cloudElement);
                            if(errorCode == 0){
                                successTagging.add(tagKey+" - "+(String)tag.get(tagKey));
                            }else{
                                failureTagging.add(tagKey+" - "+(String)tag.get(tagKey));
                            }
                        }
                    }
                }
                cloudElement.setConfigJson((Map)resp);
                cloudElementService.save(cloudElement);
            }
        }
        Map<String, List<Object>> statusMap = new HashMap<>();
        statusMap.put("success", successTagging);
        statusMap.put("failure",failureTagging);
        return statusMap;
    }

    public Object getTags(Landingzone landingZone, String awsRegion, String functionName) {
        logger.info("Getting tags for lambda function: {}",functionName);
        String url = env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.lambda-tag-api");
        String vaultAccountKey =  vaultService.resolveVaultKey(landingZone.getDepartment().getOrganization().getName(), landingZone.getDepartment().getName(), Constants.AWS, landingZone.getLandingZone());
        String params = "?zone="+ awsRegion +"&vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey+"&functionName="+functionName;
        if(StringUtils.isBlank(awsRegion)){
            params = "?vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey+"&functionName="+functionName;
        }
        String awsxUrl = url+params;
        Object response = restTemplate.getForObject(awsxUrl, Object.class);
        return response;
    }
}
