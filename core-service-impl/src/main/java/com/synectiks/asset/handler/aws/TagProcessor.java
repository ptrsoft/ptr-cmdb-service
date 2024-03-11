package com.synectiks.asset.handler.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.*;
import com.synectiks.asset.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Organization organization = getOrganization(tagValue[0]);
        if(organization == null){
            logger.error("Organization is null");
            return 7;
        }
        Department department = getDepartment(tagValue[1], organization.getId());
        if(department == null){
            logger.error("Department is null");
            return 8;
        }
        Product product = getProduct(tagValue[2], department.getId(), organization.getId());
        if(product == null){
            logger.error("Product is null");
            return 9;
        }
        ProductEnv productEnv = getProductEnv(tagValue[3], product.getId());
        if(productEnv == null){
            logger.error("ProductEnv is null");
            return 10;
        }
        String serviceNature = tagValue[4]; //for soa product it will a string value, for 3 tier product it will be null/blank string
        String type = tagValue[5];
        String moduleName = tagValue[6]; //for soa product it will a string value, for 3 tier product it will be null/blank string
        String serviceName = tagValue[7];

        if(StringUtils.isBlank(type)){
            logger.error("Type is null");
            return 11;
        }
        Module module = null;

        BusinessElement businessElement = getThreeTierService(serviceName, type, product.getId(), productEnv.getId(), cloudElement.getId());
        if(Constants.SOA.equalsIgnoreCase(product.getType())){
            module = getModule(moduleName, product.getId(), productEnv.getId());
            if(module == null){
                logger.error("Module is null");
                return 12;
            }
            businessElement = getSoaService(serviceName, serviceNature, type, product.getId(), productEnv.getId(), module.getId(), cloudElement.getId());
        }

        if(businessElement == null){
            logger.error("BusinessElement is null");
            return 14;
        }

        ObjectMapper objectMapper = Constants.instantiateMapper();
        ObjectNode tag = createTag(organization, department, product, productEnv, type, module, businessElement, objectMapper);
        Map<String, Object> hostedServiceTag = new HashMap<>();
        hostedServiceTag.put("serviceId", businessElement.getId());
        hostedServiceTag.put("instanceId", cloudElement.getInstanceId());
        hostedServiceTag.put("tag", tag);
        int errorCode = cloudElementService.updateCloudElementAndBusinessElementAssociation(hostedServiceTag,cloudElement, objectMapper);
        return errorCode;
    }

    private ObjectNode createTag(Organization organization, Department department, Product product,
                                 ProductEnv productEnv, String type, Module module, BusinessElement businessElement, ObjectMapper objectMapper){
        if(objectMapper == null){
            objectMapper = Constants.instantiateMapper();
        }
        ObjectNode serviceNod = objectMapper.createObjectNode();
        serviceNod.put("id", businessElement.getId());
        serviceNod.put("name", businessElement.getServiceName());

        ObjectNode typeNod = objectMapper.createObjectNode();
        typeNod.put("name", type);

        if(module != null){
            ObjectNode moduleNod = objectMapper.createObjectNode();
            moduleNod.put("id", module.getId());
            moduleNod.put("name", module.getName());
            moduleNod.put("service", serviceNod);
            typeNod.put("module", moduleNod);
        }else{
            typeNod.put("service", serviceNod);
        }

        ObjectNode productEnvNod = objectMapper.createObjectNode();
        productEnvNod.put("id", productEnv.getId());
        productEnvNod.put("name", productEnv.getName());
        productEnvNod.put("type", typeNod);

        ObjectNode productNod = objectMapper.createObjectNode();
        productNod.put("id", product.getId());
        productNod.put("name", product.getName());
        productNod.put("productEnv", productEnvNod);

        ObjectNode depNod = objectMapper.createObjectNode();
        depNod.put("id", department.getId());
        depNod.put("name", department.getName());
        depNod.put("product", productNod);

        ObjectNode orgNod = objectMapper.createObjectNode();
        orgNod.put("id", organization.getId());
        orgNod.put("name", organization.getName());
        orgNod.put("dep", depNod);

        ObjectNode parentNod = objectMapper.createObjectNode();
        parentNod.put("org", orgNod);
        parentNod.put("type", product.getType());

        return parentNod;
    }
}
