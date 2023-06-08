package com.synectiks.asset.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.config.Converter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JacsonNodeAndMapConvertUtil {

	private final static ObjectMapper mapper = Constants.instantiateMapper();
	
	public Map<String, Object> convertObjectNodeToMap(ObjectNode node) throws JsonParseException, JsonMappingException, IOException {
	  return mapper.readValue(node.toString().getBytes(), new TypeReference<HashMap<String,Object>>() {});
	}
	
	public Map<String, Object> convertJsonNodeToMap(JsonNode node) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(node.toString().getBytes(), new TypeReference<HashMap<String,Object>>() {});
	}
	
	public ObjectNode convertMapToObjectNode(Map<String,Object> map) throws IOException  {
		String strJson = mapper.writeValueAsString(map);
		return Converter.fromJsonString(strJson, ObjectNode.class);
	}
	
	public static JsonNode convertMapToJsonNode(Map<String,Object> map) throws IOException  {
		String strJson = mapper.writeValueAsString(map);
		return Converter.fromJsonString(strJson, JsonNode.class);
	}
	
}
