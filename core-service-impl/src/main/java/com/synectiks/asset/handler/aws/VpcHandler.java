package com.synectiks.asset.handler.aws;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.*;
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

import java.util.ArrayList;
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
        Object response = getResponse(vaultService, restTemplate, getUrl(), organization, department, landingZone, awsRegion);
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map responseMap = (LinkedHashMap)response;
            List responseList = (List)responseMap.get("Vpcs");
            for(Object obj: responseList){
                Map configMap = (Map)obj;
                addUpdate(landingZone, configMap);
            }
        }
    }

    @Override
    public Object save(String elementType, Landingzone landingzone, String query) {
        Object response = getResponse(restTemplate, getUrl(elementType, String.valueOf(landingzone.getId()), query));
        List<ProductEnclave> productEnclaveList = new ArrayList<>();
        if(response != null && response.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            Map responseMap = (LinkedHashMap)response;
            List responseList = (List)responseMap.get("Vpcs");
            for(Object obj: responseList){
                Map configMap = (Map)obj;
                productEnclaveList.add(addUpdate(landingzone, configMap));
            }
        }
        return productEnclaveList;
    }

    private ProductEnclave addUpdate(Landingzone landingZone, Map configMap) {
        ProductEnclave productEnclave = productEnclaveService.findProductEnclave((String)configMap.get("VpcId"), landingZone.getDepartment().getId(), landingZone.getId());
        if(productEnclave != null){
            logger.debug("Updating product-enclave: {} for landing-zone: {}",(String)configMap.get("VpcId"), landingZone.getLandingZone());
            productEnclave.setMetadata(configMap);
            productEnclave = productEnclaveService.save(productEnclave);
        }else{
            logger.debug("Adding product-enclave: {} for landing-zone: {}",(String)configMap.get("VpcId"), landingZone.getLandingZone());
            productEnclave = ProductEnclave.builder()
                        .instanceId((String)configMap.get("VpcId"))
                        .instanceName((String)configMap.get("VpcId"))
                        .metadata(configMap)
                        .department(landingZone.getDepartment())
                        .landingzone(landingZone)
                        .build();
            productEnclave = productEnclaveService.save(productEnclave);
        }
        return productEnclave;
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.vpc-api");
    }

    @Override
    public String getUrl(String elementType, String landingZoneId, String query){
        String baseUrl = env.getProperty("awsx-api.base-url");
        String param = "?elementType=landingZone&landingZoneId="+landingZoneId+"&query="+query;
        return baseUrl+param;
    }

}
