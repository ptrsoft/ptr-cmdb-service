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
public class EksHandler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(EksHandler.class);

    private final Environment env;

    public EksHandler(Environment env){
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
            List responseList = (ArrayList)response;
            for(Object obj: responseList){
                Map configMap = (Map)obj;
                addUpdate(landingZone, configMap);
            }
        }else if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map configMap = (LinkedHashMap)response;
            addUpdate(landingZone, configMap);
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
                cloudElementList.add(addUpdate(landingzone, configMap));
            }
        }else if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map configMap = (LinkedHashMap)response;
            cloudElementList.add(addUpdate(landingzone, configMap));
        }
        return cloudElementList;
    }

    private CloudElement addUpdate(Landingzone landingZone, Map configMap) {
        CloudElement cloudElement =  cloudElementService.getCloudElementByArn(landingZone.getId(), (String)((Map)configMap.get("Cluster")).get("Arn"), Constants.EKS);
        ProductEnclave productEnclave = null;
        if(((Map)configMap.get("Cluster")).containsKey("ResourcesVpcConfig")){
            if(((Map)((Map)configMap.get("Cluster")).get("ResourcesVpcConfig")).containsKey("VpcId")){
                String vpcId = (String)((Map)((Map)configMap.get("Cluster")).get("ResourcesVpcConfig")).get("VpcId");
                productEnclave = productEnclaveService.findProductEnclave(vpcId, landingZone.getDepartment().getId(), landingZone.getId());
            }
        }

        if(cloudElement != null ){
            logger.debug("Updating eks: {} for landing-zone: {}",(String)((Map)configMap.get("Cluster")).get("Name"), landingZone.getLandingZone());
            cloudElement.setConfigJson(configMap);
            cloudElement.setInstanceId((String)((Map)configMap.get("Cluster")).get("Name"));
            cloudElement.setInstanceName((String)((Map)configMap.get("Cluster")).get("Name"));
            cloudElement.setProductEnclave(productEnclave);
            cloudElement = cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding eks: {} for landing-zone: {}",(String)((Map)configMap.get("Cluster")).get("Name"), landingZone.getLandingZone());
            String instanceId = (String)((Map) configMap.get("Cluster")).get("Name");
            if(((Map) configMap.get("Cluster")).get("Id") != null){
                instanceId = (String)((Map) configMap.get("Cluster")).get("Id");
            }

            cloudElement = CloudElement.builder()
                .elementType(Constants.EKS)
                .arn((String)((Map) configMap.get("Cluster")).get("Arn"))
                .instanceId(instanceId)
                .instanceName((String)((Map) configMap.get("Cluster")).get("Name"))
                .category(Constants.APP_SERVICES)
                .landingzone(landingZone)
                .configJson(configMap)
                .productEnclave(productEnclave)
                .cloud(landingZone.getCloud().toUpperCase())
                .build();
            cloudElement = cloudElementService.save(cloudElement);
        }
        return cloudElement;
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.eks-api");
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
            Map tag = (Map)((Map)((Map)cloudElement.getConfigJson()).get("Cluster")).get("Tags");
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
            }
        }
        Map<String, List<Object>> statusMap = new HashMap<>();
        statusMap.put("success", successTagging);
        statusMap.put("failure",failureTagging);
        return statusMap;
    }
}
