package com.synectiks.asset.handler.aws;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.*;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.service.CloudElementService;
import com.synectiks.asset.service.LandingzoneService;
import com.synectiks.asset.service.ProductEnclaveService;
import com.synectiks.asset.service.VaultService;
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
            logger.debug("Adding eks: {} for landing-zone: {}",instanceId, landingZone.getLandingZone());

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

}
