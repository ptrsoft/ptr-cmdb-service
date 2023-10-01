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

    private void addUpdate(Department department, Landingzone landingZone, Map configMap) {
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
            cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding ec2: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());

            CloudElement cloudElementObj = CloudElement.builder()
                .elementType(Constants.EC2)
                .instanceId(instanceId)
                .instanceName(instanceId)
                .category(Constants.APP_SERVICES)
                .landingzone(landingZone)
                .configJson(configMap)
                .productEnclave(productEnclave)
                .build();
            cloudElementService.save(cloudElementObj);
        }
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.ec2-api");
    }

    @Override
    public Map<String, List<Object>> processTag(CloudElement cloudElement){
        List<Object> successTagging = new ArrayList<>();
        List<Object> failureTagging = new ArrayList<>();

        if(cloudElement.getConfigJson() != null ){
            List tagList = (List)((Map)cloudElement.getConfigJson()).get("Tags");
            for(Object object: tagList){
                Map tag = (Map)object;
                if(tag.get("Key") != null &&  ((String)tag.get("Key")).toLowerCase().contains("appkube_tag")){
                    String tagValue[] = ((String)tag.get("Value")).split("/");
                    int errorCode = validate(tagValue, cloudElement, tagProcessor);
                    if(errorCode == 1) {
                        String msg = String.format("Tagging failed. Tag's organization does not match with landing-zone's organization. Landing-zone org: %s, Tag org: %s", cloudElement.getLandingzone().getDepartment().getOrganization().getName(), tagValue[0]);
                        logger.error(msg);
                        tag.put("failure_reason",msg);
                        tag.put("error_code",1);
                        failureTagging.add(tag);
                        continue;
                    }
                    if(errorCode == 2){
                        String msg = String.format("Tagging failed. Tag's department does not match with landing-zone's department. Landing-zone dep: %s, Tag dep: %s", cloudElement.getLandingzone().getDepartment().getName(), tagValue[1]);
                        logger.error(msg);
                        tag.put("failure_reason",msg);
                        tag.put("error_code",2);
                        failureTagging.add(tag);
                        continue;
                    }
                    if(errorCode == 0){
                        errorCode = tagProcessor.process(tagValue, cloudElement);
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
