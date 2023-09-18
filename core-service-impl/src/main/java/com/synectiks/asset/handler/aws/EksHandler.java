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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void save(Organization organization, Department department, Landingzone landingZone, String awsRegion) {
        String vaultAccountKey =  vaultService.resolveVaultKey(organization.getName(), department.getName(), Constants.AWS, landingZone.getLandingZone());
        String params = "?zone="+awsRegion+"&vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey;
        if(StringUtils.isBlank(awsRegion)){
            params = "?vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey;
        }
        String awsxUrl = getUrl()+params;
        Object response = this.restTemplate.getForObject(awsxUrl, Object.class);
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
            List responseList = (ArrayList)response;
            for(Object obj: responseList){
                Map configMap = (Map)obj;
                addUpdate(department, landingZone, configMap);
            }
        }else if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map configMap = (LinkedHashMap)response;
            addUpdate(department, landingZone, configMap);
        }
    }

    private void addUpdate(Department department, Landingzone landingZone, Map configMap) {
        CloudElement cloudElement =  cloudElementService.getCloudElementByArn(landingZone.getId(), (String)((Map)configMap.get("Cluster")).get("Arn"), Constants.EKS);
        ProductEnclave productEnclave = null;
        if(((Map)configMap.get("Cluster")).containsKey("ResourcesVpcConfig")){
            if(((Map)((Map)configMap.get("Cluster")).get("ResourcesVpcConfig")).containsKey("VpcId")){
                String vpcId = (String)((Map)((Map)configMap.get("Cluster")).get("ResourcesVpcConfig")).get("VpcId");
                productEnclave = productEnclaveService.findProductEnclave(vpcId, department.getId(), landingZone.getId());
            }
        }

        if(cloudElement != null ){
            logger.debug("Updating eks: {} for landing-zone: {}",(String)((Map)configMap.get("Cluster")).get("Name"), landingZone.getLandingZone());
            cloudElement.setConfigJson(configMap);
            cloudElement.setInstanceId((String)((Map)configMap.get("Cluster")).get("Name"));
            cloudElement.setInstanceName((String)((Map)configMap.get("Cluster")).get("Name"));
            cloudElement.setProductEnclave(productEnclave);
            cloudElementService.save(cloudElement);
        }else{
            logger.debug("Adding eks: {} for landing-zone: {}",(String)((Map)configMap.get("Cluster")).get("Name"), landingZone.getLandingZone());
            String instanceId = (String)((Map) configMap.get("Cluster")).get("Name");
            if(((Map) configMap.get("Cluster")).get("Id") != null){
                instanceId = (String)((Map) configMap.get("Cluster")).get("Id");
            }

            CloudElement cloudElementObj = CloudElement.builder()
                .elementType(Constants.EKS)
                .arn((String)((Map) configMap.get("Cluster")).get("Arn"))
                .instanceId(instanceId)
                .instanceName((String)((Map) configMap.get("Cluster")).get("Name"))
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
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.eks-api");
    }

}
