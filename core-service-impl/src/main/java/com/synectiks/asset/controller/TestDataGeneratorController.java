package com.synectiks.asset.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.service.CloudElementService;
import com.synectiks.asset.util.DateFormatUtil;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestDataGeneratorController {

	private final Logger logger = LoggerFactory.getLogger(TestDataGeneratorController.class);

	private static final String ENTITY_NAME = "CloudEnvironment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CloudElementService cloudElementService;

    @Autowired
    private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;

    @RequestMapping(value = "/cloud-element/generate/cost-data", method = RequestMethod.GET)
    public void generateCostData(@RequestParam String startDate, @RequestParam String endDate) {
        logger.debug("REST request to generate cost data ");
        List<CloudElement> cloudElementList = cloudElementService.findAll();
        ObjectMapper objectMapper = Constants.instantiateMapper();
        for(CloudElement cloudElement: cloudElementList){

            ObjectNode objectNode = null;
            ArrayNode arrayNode = objectMapper.createArrayNode();
            try{
                String js = jsonAndObjectConverterUtil.convertObjectToJsonString(objectMapper, cloudElement.getCloudIdentity(), Map.class);
                JsonNode rootNode = objectMapper.readTree(js);
                if(rootNode != null){
                    JsonNode elmJson = rootNode.get("elementLists");
                    if(elmJson != null && elmJson.isArray()){
                        Iterator<JsonNode> iterator = elmJson.iterator();
                        while (iterator.hasNext()) {
                            objectNode = (ObjectNode)iterator.next();
                            JSONObject obj = DateFormatUtil.generateTestCostData(startDate, endDate);
                            for(String key: obj.keySet()){
                                objectNode.put(key, objectMapper.readTree(obj.get(key).toString()));
                            }
                            arrayNode.add(objectNode);
                            System.out.println(objectNode.toString());
                        }
                    }

                }
                if(arrayNode != null){
                    ObjectNode finalNode =  objectMapper.createObjectNode();
                    finalNode.put("elementLists", arrayNode);
                    Map map = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, finalNode, Map.class);
                    cloudElement.setCostJson(map);
                    cloudElementService.save(cloudElement);
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }
    
    
    
    
    @RequestMapping(value = "/cloud-element/generate/sla-data", method = RequestMethod.GET)
    public void generateSlaData(@RequestParam String startDate, @RequestParam String endDate) {
        logger.debug("REST request to generate sla data ");
        List<CloudElement> cloudElementList = cloudElementService.findAll();
        ObjectMapper objectMapper = Constants.instantiateMapper();
        String slaType[] = {"performance", "availability","reliability","security","end usage"};
        for(CloudElement cloudElement: cloudElementList){

            ObjectNode objectNode = null;
            ArrayNode arrayNode = objectMapper.createArrayNode();
            try{
                String js = jsonAndObjectConverterUtil.convertObjectToJsonString(objectMapper, cloudElement.getHostedServices(), Map.class);
                JsonNode rootNode = objectMapper.readTree(js);
                if(rootNode != null){
                    JsonNode elmJson = rootNode.get("HOSTEDSERVICES");
                    if(elmJson != null && elmJson.isArray()){
                        Iterator<JsonNode> iterator = elmJson.iterator();
                        while (iterator.hasNext()) {
                            objectNode = (ObjectNode)iterator.next();
//                            JSONArray slaArray = new JSONArray();
                            ArrayNode an = objectMapper.createArrayNode();
                            for(String type: slaType) {
                            	
//                            	JSONObject slaTypeNode = new JSONObject();
                            	ObjectNode on = objectMapper.createObjectNode();
//                            	slaTypeNode.put("type","performance");
                            	on.put("type",type);
                            	JSONObject obj = DateFormatUtil.generateTestSlaData(startDate, endDate);
                                for(String key: obj.keySet()){
//                                	slaTypeNode.put(key, objectMapper.readTree(obj.get(key).toString()));
//                                    objectNode.put("sla", slaArray);
                                	on.put(key, objectMapper.readTree(obj.get(key).toString()));
                                }
//                                slaArray.put(slaTypeNode);
                                an.add(on);
                            }
                            
                            
//                          jsonObject.put("type", "performance");
//                    		System.out.println(jsonObject.toString());
//                    		JSONArray slaArray = new JSONArray();
//                    		 
//                    		slaArray.put(jsonObject);
//                    		JSONObject mainObject = new JSONObject();
//                    		 
                    		objectNode.put("sla", an);
                    	    
                    	    arrayNode.add(objectNode);
                    		
                            
//                            JSONObject mainObject = new JSONObject();
//                            mainObject.put("sla", arrayNode);
//                            System.out.println(mainObject.toString());
                        }
                    }

                }
                System.out.println(arrayNode);
                if(arrayNode != null){
                    ObjectNode finalNode =  objectMapper.createObjectNode();
                    finalNode.put("HOSTEDSERVICES", arrayNode);
                    
                    Map map = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, finalNode, Map.class);
                    cloudElement.setSlaJson(map);
                    cloudElementService.save(cloudElement);
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }


    
}
