package com.synectiks.asset.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.*;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BiMappingService {

	private final Logger logger = LoggerFactory.getLogger(BiMappingService.class);


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
	private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;

	@Transactional
	public Map<String, String> save(Organization organization, Department department, Map departmentMap) {
		logger.debug("Request to save bi-mapping");
		Map productMap = (Map)departmentMap.get("product");
		Map<String, String> resp = new HashMap<>();
		if(!Constants.SOA.equalsIgnoreCase(((String) productMap.get("type"))) && !Constants.THREE_TIER.equalsIgnoreCase(((String) productMap.get("type")))){
			logger.error("Product type not supported");
			resp.clear();
			resp.put("status", "1");
			resp.put("message", "error - Product type not supported");
			return resp;
		}

		Map productEnvMap = (Map)productMap.get("productEnv");
		Map moduleMap = (Map)productEnvMap.get("module");
		List serviceList = (List)productEnvMap.get("service");;

		Product  product = saveProduct(organization, department, productMap);
		ProductEnv productEnv = saveProductEnv(product, productEnvMap);
		Module module = null;
		if(Constants.SOA.equalsIgnoreCase(product.getType())){
			module = saveModule(product, productEnv, moduleMap);
			Map serviceMap = (Map)moduleMap.get("service");
			String serviceArray [] = {"business", "common"};
			for(String serviceNature: serviceArray){
				List serviceNatureList = (List)serviceMap.get(serviceNature);
				for(Object obj: serviceNatureList){
					Map serviceTypeMap = (Map)obj;
					logger.debug("SOA - Service name: {}",serviceTypeMap.get("name"));
					BusinessElement businessElement = saveService(product, productEnv, module, serviceNature, serviceTypeMap);
				}
			}
			logger.info("SOA services saved successfully");
			resp.clear();
			resp.put("status", "0");
			resp.put("message", "success - SOA services saved successfully");
			return resp;

		}
		for(Object obj: serviceList){
			Map serviceTypeMap = (Map)obj;
			logger.debug("3 tier - Service name: {}",serviceTypeMap.get("name"));
			BusinessElement businessElement = saveService(product, productEnv, module, null, serviceTypeMap);
		}
		logger.info("3 tier services saved successfully");
		resp.clear();
		resp.put("status", "0");
		resp.put("message", "success - 3 tier services saved successfully");
		return resp;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private Product saveProduct(Organization organization, Department department, Map productMap){
		Product exitstingProduct = productService.getProduct((String) productMap.get("name"), department.getId(), organization.getId());
		Product product = null;
		if(exitstingProduct != null){
			logger.info("1. product already exists in given organization and department. product name: {}, department id: {}, organization id: {} ",(String) productMap.get("name"), department.getId(), organization.getId());
			product = exitstingProduct;
		}else {
			logger.info("1. Saving product");
			product = Product.builder()
					.name((String) productMap.get("name"))
					.type(((String) productMap.get("type")).toUpperCase())
					.status(Constants.ACTIVE)
					.organization(organization)
					.department(department)
					.build();
			product = productService.save(product);
		}
		return product;
	}
	@Transactional(propagation = Propagation.REQUIRED)
	private ProductEnv saveProductEnv(Product product, Map productEnvMap){
		ProductEnv exitstingProductEnv = productEnvService.getProductEnv((String) productEnvMap.get("name"), product.getId());
		ProductEnv productEnv = null;
		if(exitstingProductEnv != null){
			logger.info("2. product-env already exists in given organization, department and product. product-env name: {}, product id: {}, department id: {}, organization id: {} ",(String)productEnvMap.get("name"), product.getId(), product.getDepartment().getId(), product.getDepartment().getOrganization().getId());
			productEnv = exitstingProductEnv;
		}else {
			logger.info("2. Saving product-env");
			productEnv = ProductEnv.builder()
					.name((String) productEnvMap.get("name"))
					.status(Constants.ACTIVE)
					.product(product)
					.build();
			productEnv = productEnvService.save(productEnv);
		}
		return productEnv;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private Module saveModule(Product product, ProductEnv productEnv, Map moduleMap){
		Module exitstingModule = moduleService.getModule((String) moduleMap.get("name"), product.getId(), productEnv.getId());
		Module module = null;
		if(exitstingModule != null){
			logger.info("3. module already exists in given organization, department, product and product-env. module: {}, product-env-id: {}, product id: {}, department id: {}, organization id: {} ",(String)moduleMap.get("name"), productEnv.getId(), product.getId(), product.getDepartment().getId(), product.getDepartment().getOrganization().getId());
			module = exitstingModule;
		}else {
			logger.info("3. Saving module");
			module = Module.builder()
					.name((String) moduleMap.get("name"))
					.status(Constants.ACTIVE)
					.product(product)
					.productEnv(productEnv)
					.build();
			module = moduleService.save(module);
		}
		return module;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private BusinessElement saveService(Product product, ProductEnv productEnv, Module module, String serviceNature, Map serviceMap){
		Map cloudElementMap = (Map)serviceMap.get("cloudElementMapping");
		Long cloudElementId = ((Integer)cloudElementMap.get("id")).longValue();
		CloudElement cloudElement = cloudElementService.findOne(cloudElementId).orElse(null);

		BusinessElement exitstingBusinessElement = businessElementService.getThreeTierService((String) serviceMap.get("name"), (String) serviceMap.get("type"), product.getId(), productEnv.getId(), cloudElementId);
		if(Constants.SOA.equalsIgnoreCase(product.getType())){
			exitstingBusinessElement = businessElementService.getSoaService((String) serviceMap.get("name"), serviceNature, (String) serviceMap.get("type"), product.getId(), productEnv.getId(), module.getId(), cloudElementId);
		}

		BusinessElement businessElement = null;
		if(exitstingBusinessElement != null){
			if(Constants.SOA.equalsIgnoreCase(product.getType())){
				logger.info("4. business-element already exists in given organization, department, product, product-env and module. business-element: {}, module: {}, product-env-id: {}, product id: {}, department id: {}, organization id: {} ",(String)serviceMap.get("name"), module.getId(), productEnv.getId(), product.getId(), product.getDepartment().getId(), product.getDepartment().getOrganization().getId());
			}else {
				logger.info("4. business-element already exists in given organization, department, product, product-env. business-element: {}, product-env-id: {}, product id: {}, department id: {}, organization id: {} ",(String)serviceMap.get("name"), productEnv.getId(), product.getId(), product.getDepartment().getId(), product.getDepartment().getOrganization().getId());
			}
			businessElement = exitstingBusinessElement;
			Map<String, Object> mgmtInfo = new HashMap<>();
			mgmtInfo.put("managementInfo", (List)cloudElementMap.get("managementInfo"));
			businessElement.setMetadata(mgmtInfo);
			Map<String, Object> cfgInfo = new HashMap<>();
			cfgInfo.put("configInfo", (List)cloudElementMap.get("configInfo"));
			businessElement.setConfigJson(cfgInfo);
			businessElement.setStatus(Constants.ACTIVE);
		}else{
			businessElement = BusinessElement.builder()
					.serviceName((String) serviceMap.get("name"))
					.serviceNature(Constants.SOA.equalsIgnoreCase(product.getType()) ? serviceNature.toUpperCase() : null)
					.serviceType(((String) serviceMap.get("type")).toUpperCase())
					.status(Constants.ACTIVE)
					.department(product.getDepartment())
					.product(product)
					.productEnv(productEnv)
					.module(module)
					.cloudElement(cloudElement)
					.build();
			Map<String, Object> mgmtInfo = new HashMap<>();
			mgmtInfo.put("managementInfo", (List)cloudElementMap.get("managementInfo"));
			businessElement.setMetadata(mgmtInfo);
			Map<String, Object> cfgInfo = new HashMap<>();
			cfgInfo.put("configInfo", (List)cloudElementMap.get("configInfo"));
			businessElement.setConfigJson(cfgInfo);
		}
		logger.info("4. Saving business-element");
		businessElement = businessElementService.save(businessElement);

		logger.info("5. Saving cloud-element");
		ObjectMapper objectMapper = Constants.instantiateMapper();
		ObjectNode objectNode = null;
		try {
			ArrayNode arrayNode = cloudElementService.getArrayNodeFromHostedService(cloudElement, objectMapper);
			if(Constants.SOA.equalsIgnoreCase(product.getType())){
				objectNode = (ObjectNode)objectMapper.readTree(Constants.SOA_TAG_TEMPLATE);
				createSoaTag(objectNode, product, productEnv, module, cloudElement, businessElement);
			}else{
				objectNode = (ObjectNode)objectMapper.readTree(Constants.THREE_TIER_TAG_TEMPLATE);
				create3TierTag(objectNode, product, productEnv, cloudElement, businessElement);
			}

			boolean isTagFound = cloudElementService.replaceIfTagFound(arrayNode, objectNode);
			if(!isTagFound){
				arrayNode.add(objectNode);
			}

			ObjectNode finalNode =  objectMapper.createObjectNode();
			finalNode.put(Constants.HOSTEDSERVICES, arrayNode);
			Map updatedHostedService = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, finalNode, Map.class);
			cloudElement.setHostedServices(updatedHostedService);
			cloudElement = cloudElementService.save(cloudElement);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return businessElement;
	}


	private void createSoaTag(ObjectNode objectNode, Product product, ProductEnv productEnv, Module module, CloudElement cloudElement, BusinessElement businessElement){
		ObjectNode tagNode = (ObjectNode)objectNode.get("tag");

		ObjectNode orgNode = (ObjectNode)tagNode.get("org");
		orgNode.put("id",product.getDepartment().getOrganization().getId());
		orgNode.put("name",product.getDepartment().getOrganization().getName());

		ObjectNode depNode = (ObjectNode)orgNode.get("dep");
		depNode.put("id",product.getDepartment().getId());
		depNode.put("name",product.getDepartment().getName());

		ObjectNode productNode = (ObjectNode)depNode.get("product");
		productNode.put("id",product.getId());
		productNode.put("name",product.getName());

		ObjectNode productEnvNode = (ObjectNode)productNode.get("productEnv");
		productEnvNode.put("id",productEnv.getId());
		productEnvNode.put("name",productEnv.getName().toUpperCase());

		ObjectNode typeNode = (ObjectNode)productEnvNode.get("type");
		typeNode.put("name",businessElement.getServiceNature());

		ObjectNode moduleNode = (ObjectNode)typeNode.get("module");
		moduleNode.put("id",module.getId());
		moduleNode.put("name",module.getName());

		ObjectNode serviceNode = (ObjectNode)moduleNode.get("service");
		serviceNode.put("id",businessElement.getId());
		serviceNode.put("name",businessElement.getServiceName());

		moduleNode.put("service",serviceNode);
		typeNode.put("module",moduleNode);
		productEnvNode.put("type",typeNode);
		productNode.put("productEnv",productEnvNode);
		depNode.put("product",productNode);
		orgNode.put("dep",depNode);
		tagNode.put("org",orgNode);
		objectNode.put("tag",tagNode);
		objectNode.put("serviceId",businessElement.getId());
		objectNode.put("instanceId",cloudElement.getInstanceId());
		logger.info("New soa tag: {}",objectNode);
	}

	private void create3TierTag(ObjectNode objectNode, Product product, ProductEnv productEnv, CloudElement cloudElement, BusinessElement businessElement){
		ObjectNode tagNode = (ObjectNode)objectNode.get("tag");

		ObjectNode orgNode = (ObjectNode)tagNode.get("org");
		orgNode.put("id",product.getDepartment().getOrganization().getId());
		orgNode.put("name",product.getDepartment().getOrganization().getName());

		ObjectNode depNode = (ObjectNode)orgNode.get("dep");
		depNode.put("id",product.getDepartment().getId());
		depNode.put("name",product.getDepartment().getName());

		ObjectNode productNode = (ObjectNode)depNode.get("product");
		productNode.put("id",product.getId());
		productNode.put("name",product.getName());

		ObjectNode productEnvNode = (ObjectNode)productNode.get("productEnv");
		productEnvNode.put("id",productEnv.getId());
		productEnvNode.put("name",productEnv.getName().toUpperCase());

		ObjectNode typeNode = (ObjectNode)productEnvNode.get("type");
		typeNode.put("name",businessElement.getServiceType().toUpperCase());

		ObjectNode serviceNode = (ObjectNode)typeNode.get("service");
		serviceNode.put("id",businessElement.getId());
		serviceNode.put("name",businessElement.getServiceName());

		typeNode.put("service",serviceNode);
		productEnvNode.put("type",typeNode);
		productNode.put("productEnv",productEnvNode);
		depNode.put("product",productNode);
		orgNode.put("dep",depNode);
		tagNode.put("org",orgNode);
		objectNode.put("tag",tagNode);
		objectNode.put("serviceId",businessElement.getId());
		objectNode.put("instanceId",cloudElement.getInstanceId());

		logger.info("New 3 tier tag: {}",objectNode);

	}
}
