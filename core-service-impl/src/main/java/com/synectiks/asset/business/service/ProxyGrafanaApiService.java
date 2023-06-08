package com.synectiks.asset.business.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class ProxyGrafanaApiService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProxyGrafanaApiService.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	public ObjectNode importDashboardInGrafana(ObjectNode obj) throws JsonMappingException, JsonProcessingException {
		logger.info("Import dashboard in proxy grafana");
		ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = createHttpHeaders();
		HttpEntity<String> request = new HttpEntity<String>(obj.toString(), headers);
	    String theUrl = Constants.PROXY_GRAFANA_BASE_API+"/dashboards/importAssets";
		String resp = restTemplate.postForObject(theUrl, request, String.class);
		return (ObjectNode)mapper.readTree(resp);
	}
	
	public JsonNode getGrafanaDatasource(Map<String, String> obj) throws JsonMappingException, JsonProcessingException {
		logger.info("Search proxy grafana data-source");
		ObjectMapper mapper = new ObjectMapper();
	    String theUrl = Constants.PROXY_GRAFANA_BASE_API+"/datasources/accountid/"+obj.get("accountId");
	    HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(theUrl, HttpMethod.GET, entity, String.class);
        return mapper.readTree(response.getBody());
	}
	
	public JsonNode getGrafanaMasterDatasource(Map<String, String> obj) throws JsonMappingException, JsonProcessingException {
		logger.info("Search proxy grafana master-data-source");
		ObjectMapper mapper = new ObjectMapper();
	    String theUrl = Constants.PROXY_GRAFANA_BASE_API+"/datasources/master-datasources";
	    HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(theUrl, HttpMethod.GET, entity, String.class);
        return mapper.readTree(response.getBody());
	}
	
	public JsonNode getGrafanaDatasourceByAccountIdAndInputType(String accountId, String inputType) throws JsonMappingException, JsonProcessingException {
		logger.info("Search grafana data-source by account id {} and input type {}", accountId, inputType);
		ObjectMapper mapper = new ObjectMapper();
	    String theUrl = Constants.PROXY_GRAFANA_BASE_API+"/datasources/accountid/"+accountId+"/inputType/"+inputType;
	    HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(theUrl, HttpMethod.GET, entity, String.class);
        return mapper.readTree(response.getBody());
	}
	
	private HttpHeaders createHttpHeaders()	{
		String notEncoded = Constants.PROXY_GRAFANA_USER + ":" + Constants.PROXY_GRAFANA_PASSWORD;
	    String encodedAuth = "Basic " + Base64.getEncoder().encodeToString(notEncoded.getBytes());
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.add("Authorization", encodedAuth);
	    return headers;
	}
}
