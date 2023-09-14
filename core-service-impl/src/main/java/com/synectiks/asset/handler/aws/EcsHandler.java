package com.synectiks.asset.handler.aws;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.service.CloudElementService;
import com.synectiks.asset.service.LandingzoneService;
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
public class EcsHandler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(EcsHandler.class);

    private final Environment env;

    public EcsHandler(Environment env){
        this.env = env;
    }
    @Autowired
    private CloudElementService cloudElementService;

    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VaultService vaultService;

    @Override
    public void save(String organization, String department, String landingZone, String awsRegion) {
        String vaultAccountKey =  vaultService.resolveVaultKey(organization, department, Constants.AWS, landingZone);
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
                addUpdate(organization, department, landingZone, configMap);
            }
        }
    }

    private void addUpdate(String organization, String department, String landingZone, Map clusterMap) {
        List configList = (List)clusterMap.get("Clusters");
        for(Object obj: configList){
            Map configMap = (Map)obj;
            List<CloudElement> cloudElementList =  cloudElementService.getCloudElement(organization, department, Constants.AWS, landingZone, (String)configMap.get("ClusterArn"), Constants.ECS);
            if(cloudElementList != null && cloudElementList.size() > 0){
                logger.debug("Updating ecs: {} for landing-zone: {}",(String)configMap.get("ClusterName"), landingZone);
                for(CloudElement cloudElement: cloudElementList){
                    if(((String)configMap.get("ClusterArn")).equalsIgnoreCase(cloudElement.getArn())){
                        cloudElement.setConfigJson(configMap);
                        cloudElement.setInstanceId((String)configMap.get("ClusterName"));
                        cloudElement.setInstanceName((String)configMap.get("ClusterName"));
                        cloudElementService.save(cloudElement);
                    }
                }
            }else{
                logger.debug("Adding ecs {}",(String)configMap.get("ClusterName"));
                List<Landingzone> landingzoneList =  landingzoneService.getLandingZone(organization, department, Constants.AWS, landingZone);
                for(Landingzone landingzone: landingzoneList){
                    logger.debug("Adding ecs cloud-element for landing-zone: {}", landingZone);
                    String instanceId = (String)configMap.get("ClusterName");

                    CloudElement cloudElementObj = CloudElement.builder()
                            .elementType(Constants.ECS)
                            .arn((String)configMap.get("ClusterArn"))
                            .instanceId(instanceId)
                            .instanceName((String)configMap.get("ClusterName"))
                            .category(Constants.APP_SERVICES)
                            .landingzone(landingzone)
                            .configJson(configMap)
                            .build();
                    cloudElementService.save(cloudElementObj);
                }
            }
        }

    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.ecs-api");
    }

}
