package com.synectiks.asset.handler.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
public class Ec2Handler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(Ec2Handler.class);

    private final Environment env;

    public Ec2Handler(Environment env){
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
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map responseMap = (LinkedHashMap)response;
            List reservations = (List)responseMap.get("Reservations");
            for(Object obj: reservations){
                Map configMap = (Map)obj;
                List instances = (List)configMap.get("Instances");
                for(Object ec2Instance: instances){
                    addUpdate(department, landingZone, (Map)ec2Instance);
                }
            }
        }
    }

    @Override
    public Object save(String elementType, Landingzone landingzone, String query) {
        Object response = getResponse(restTemplate, getUrl(elementType, String.valueOf(landingzone.getId()), query));
        List<CloudElement> cloudElementList = new ArrayList<>();
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map responseMap = (LinkedHashMap)response;
            List reservations = (List)responseMap.get("Reservations");
            for(Object obj: reservations){
                Map configMap = (Map)obj;
                List instances = (List)configMap.get("Instances");
                for(Object ec2Instance: instances){
                    cloudElementList.add(addUpdate(landingzone.getDepartment(), landingzone, (Map)ec2Instance));
                }
            }
        }
        return cloudElementList;
    }

    private CloudElement addUpdate(Department department, Landingzone landingZone, Map configMap) {
        String instanceId = (String)configMap.get("InstanceId");
        CloudElement cloudElement =  cloudElementService.getCloudElementByInstanceId(landingZone.getId(), instanceId, Constants.EC2);
        ProductEnclave productEnclave = null;
        if(configMap.containsKey("VpcId")){
            String vpcId = (String)configMap.get("VpcId");
            productEnclave = productEnclaveService.findProductEnclave(vpcId, department.getId(), landingZone.getId());
        }

        if(cloudElement != null ){
            logger.debug("Updating ec2: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());
            cloudElement.setConfigJson(configMap);
            cloudElement.setInstanceId(instanceId);
            cloudElement.setInstanceName(instanceId);
            cloudElement.setProductEnclave(productEnclave);
            cloudElement = cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding ec2: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());

            cloudElement = CloudElement.builder()
                .elementType(Constants.EC2)
                .instanceId(instanceId)
                .instanceName(instanceId)
                .category(Constants.APP_SERVICES)
                .landingzone(landingZone)
                .configJson(configMap)
                .productEnclave(productEnclave)
                .cloud(landingZone.getCloud().toUpperCase())
                .build();
            cloudElementService.save(cloudElement);
        }
        return cloudElement;
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.ec2-api");
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
            List tagList = (List)((Map)cloudElement.getConfigJson()).get("Tags");
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
