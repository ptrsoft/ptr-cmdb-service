package com.synectiks.asset.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.business.service.ProxyGrafanaApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProxyGrafanaApiController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProxyGrafanaApiController.class);
	
	@Autowired
	private ProxyGrafanaApiService proxyGrafanaApiService;
	
	@GetMapping("/proxy-grafana-data-source")
	public ResponseEntity<JsonNode> getGrafanaDataSource(@RequestParam Map<String, String> obj) throws JsonMappingException, JsonProcessingException{
		logger.info("Request to search proxy grafana data-source");
		JsonNode objectNode = proxyGrafanaApiService.getGrafanaDatasource(obj);
		return ResponseEntity.status(HttpStatus.OK).body(objectNode);
	}
	
	@GetMapping("/proxy-grafana-master-data-source")
	public ResponseEntity<JsonNode> getGrafanaMasterDataSource(@RequestParam Map<String, String> obj) throws JsonMappingException, JsonProcessingException{
		logger.info("Request to search proxy grafana master-data-source");
		JsonNode objectNode = proxyGrafanaApiService.getGrafanaMasterDatasource(obj);
		return ResponseEntity.status(HttpStatus.OK).body(objectNode);
	}
	
	@PostMapping("/proxy-grafana-import-dashboard")
	public ResponseEntity<ObjectNode> importDashboardInGrafana(@RequestBody ObjectNode obj) throws JsonMappingException, JsonProcessingException{
		logger.info("Request to import dashboard in proxy grafana");
		ObjectNode spa = proxyGrafanaApiService.importDashboardInGrafana(obj);
		return ResponseEntity.status(HttpStatus.OK).body(spa);
	}
	
	
}
