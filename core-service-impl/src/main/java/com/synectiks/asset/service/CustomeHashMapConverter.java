package com.synectiks.asset.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomeHashMapConverter implements AttributeConverter<Map<String, Object>, String> {
	private static final Logger logger = LoggerFactory.getLogger(CustomeHashMapConverter.class);
	private ObjectMapper objectMapper = Constants.instantiateMapper();

    private JsonAndObjectConverterUtil jsonAndObjectConverterUtil = new JsonAndObjectConverterUtil();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> map) {

        String strJson = null;
        try {
        	strJson = objectMapper.writeValueAsString(map);
        } catch (final JsonProcessingException e) {
            logger.error("JSON writing error", e);
        }

        return strJson;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String strJson) {
    	Map<String, Object> map = null;
    	if(StringUtils.isBlank(strJson) || (!StringUtils.isBlank(strJson) && "null".equalsIgnoreCase(strJson))) {
    		return map;
    	}
        try {
//            map = objectMapper.readValue(strJson, Map.class);
            Object object = objectMapper.readTree(strJson);
            if(object.getClass().getName().equalsIgnoreCase("com.fasterxml.jackson.databind.node.ArrayNode")){
                logger.debug("json being read is array of nodes");
                ArrayNode objectNode = (ArrayNode)objectMapper.readTree(strJson);
                map = new HashMap<>();
                map.put("data",objectNode);
                return map;
            }
            ObjectNode objectNode = (ObjectNode)objectMapper.readTree(strJson);
            map = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, objectNode, Map.class);
        } catch (final IOException e) {
            logger.error("JSON reading error", e);
        }

        return map;
    }

    public Map<String, Object> convertObjectToMap(Object json) {
        Map<String, Object> map = null;
        if(json == null) {
            return map;
        }
        try {
            if(json.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
                return objectMapper.convertValue(json, Map.class);
            }
            if(json.getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
                logger.debug("class of json being converted is ArrayList");
                List list = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, json, List.class);
                map = new HashMap<>();
                map.put("data",list);
                return map;
            }

            Object object = objectMapper.readTree(json.toString());
            if(object.getClass().getName().equalsIgnoreCase("com.fasterxml.jackson.databind.node.ArrayNode")){
                 ArrayNode objectNode = (ArrayNode)objectMapper.readTree(json.toString());
                 logger.debug("class of json being converted is ArrayNode");
                 map = new HashMap<>();
                 map.put("data",objectNode);
                 return map;
            }
            ObjectNode objectNode = (ObjectNode)objectMapper.readTree(json.toString());
            map = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, objectNode, Map.class);
        } catch (final IOException e) {
            logger.error("IOException in convertObjectToMap: ", e);
        }
        return map; //objectMapper.convertValue(json, Map.class);
    }
}