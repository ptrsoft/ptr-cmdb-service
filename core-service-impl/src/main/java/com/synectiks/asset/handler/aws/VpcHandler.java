package com.synectiks.asset.handler.aws;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Department;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.domain.Organization;
import com.synectiks.asset.domain.ProductEnclave;
import com.synectiks.asset.handler.CloudHandler;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VpcHandler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(VpcHandler.class);

    private final Environment env;

    public VpcHandler(Environment env){
        this.env = env;
    }

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
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map responseMap = (LinkedHashMap)response;
            List responseList = (List)responseMap.get("Vpcs");
            for(Object obj: responseList){
                Map configMap = (Map)obj;
                addUpdate(department, landingZone, configMap);
            }
        }
    }

    private void addUpdate(Department department, Landingzone landingZone, Map configMap) {
        ProductEnclave productEnclave = productEnclaveService.findProductEnclave((String)configMap.get("VpcId"), department.getId(), landingZone.getId());
        if(productEnclave != null){
            logger.debug("Updating product-enclave: {} for landing-zone: {}",(String)configMap.get("VpcId"), landingZone.getLandingZone());
            productEnclave.setMetadata(configMap);
            productEnclaveService.save(productEnclave);
        }else{
            logger.debug("Adding product-enclave: {} for landing-zone: {}",(String)configMap.get("VpcId"), landingZone.getLandingZone());
            productEnclave = ProductEnclave.builder()
                        .instanceId((String)configMap.get("VpcId"))
                        .instanceName((String)configMap.get("VpcId"))
                        .metadata(configMap)
                        .department(department)
                        .landingzone(landingZone)
                        .build();
            productEnclaveService.save(productEnclave);
        }
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.vpc-api");
    }

}
