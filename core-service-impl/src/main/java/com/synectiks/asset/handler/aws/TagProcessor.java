package com.synectiks.asset.handler.aws;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.*;
import com.synectiks.asset.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Service
public class TagProcessor {
    private final Logger logger = LoggerFactory.getLogger(TagProcessor.class);

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductEnvService productEnvService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private BusinessElementService businessElementService;

    @Autowired
    private CloudElementService cloudElementService;

    @Autowired
    private BiMappingService biMappingService;

    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private ServiceQueueService serviceQueueService;


    public Organization getOrganization(String organizationName){
        return organizationService.findByName(organizationName);
    }

    public Department getDepartment(String departmentName, Long orgId){
        return departmentService.getDepartment(departmentName, orgId);
    }

    public Product getProduct(String productName, Long departmentId, Long orgId){
        return productService.getProduct(productName, departmentId, orgId);
    }

    public ProductEnv getProductEnv(String envName, Long productId){
        return productEnvService.getProductEnv(envName, productId);
    }

    public Module getModule(String moduleName, Long productId, Long productEnvId){
        return moduleService.getModule(moduleName, productId,productEnvId);
    }

    public BusinessElement getSoaService(String serviceName, String serviceNature, String serviceType, Long productId, Long productEnvId, Long moduleId, Long cloudElementId){
        return businessElementService.getSoaService(serviceName, serviceNature, serviceType, productId, productEnvId, moduleId, cloudElementId);
    }

    public BusinessElement getThreeTierService(String serviceName, String serviceType,  Long productId, Long productEnvId, Long cloudElementId){
        return businessElementService.getThreeTierService(serviceName, serviceType, productId, productEnvId, cloudElementId);
    }

    public int process(String tagValue[], CloudElement cloudElement){

        String productType = tagValue[0].trim(); // product type SOA/3 TIER

        Map<String, Object> productMap = new HashMap<>();
        productMap.put("name",tagValue[1].trim());
        productMap.put("type",productType);
        Product product = biMappingService.saveProduct(cloudElement.getLandingzone().getOrganization(),cloudElement.getLandingzone().getDepartment(), productMap);

        Map<String, Object> productEnvMap = new HashMap<>();
        productEnvMap.put("name",tagValue[2].trim());
        ProductEnv productEnv = biMappingService.saveProductEnv(product, productEnvMap);

        if(Constants.THREE_TIER.equalsIgnoreCase(productType)){
            logger.info("3 Tier product found in tag");
            String serviceType = tagValue[3];
            String serviceName = tagValue[4];

            Map<String, Object> serviceTypeMap = new HashMap<>();
            serviceTypeMap.put("name",serviceName);
            serviceTypeMap.put("type",serviceType);
            Map<String, Object> cloudElementMap = new HashMap<>();
            cloudElementMap.put("id",cloudElement.getId());
            cloudElementMap.put("managementInfo", Collections.emptyList());
            cloudElementMap.put("configInfo",Collections.emptyList());
            serviceTypeMap.put("cloudElementMapping",cloudElementMap);
            biMappingService.saveService(product, productEnv, null, null, serviceTypeMap);
        }else if(Constants.SOA.equalsIgnoreCase(productType)){
            logger.info("SOA product found in tag");
            String moduleName = tagValue[3];
            Map<String, Object> moduleMap = new HashMap<>();
            moduleMap.put("name",moduleName);
            Module module = biMappingService.saveModule(product, productEnv, moduleMap);

            String serviceNature = tagValue[4];
            String serviceType = tagValue[5];
            String serviceName = tagValue[6];
            Map<String, Object> serviceTypeMap = new HashMap<>();
            serviceTypeMap.put("name",serviceName);
            serviceTypeMap.put("type",serviceType);
            Map<String, Object> cloudElementMap = new HashMap<>();
            cloudElementMap.put("id",cloudElement.getId());
            cloudElementMap.put("managementInfo", Collections.emptyList());
            cloudElementMap.put("configInfo",Collections.emptyList());
            serviceTypeMap.put("cloudElementMapping",cloudElementMap);
            biMappingService.saveService(product, productEnv, module, serviceNature, serviceTypeMap);
        }else {
            logger.warn("Product type not supported. Product type: {}",productType);
        }

        return 0;
    }

}
