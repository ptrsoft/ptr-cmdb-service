package com.synectiks.asset.business.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.business.domain.Dashboard;
import com.synectiks.asset.business.domain.DashboardMeta;
import com.synectiks.asset.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class AwsService {
	
	private static final Logger logger = LoggerFactory.getLogger(AwsService.class);
	
//	@Autowired
//	private ServiceProviderCloudAccountService serviceProviderCloudAccountService;
	
	public Dashboard getDashboardFromAwsS3(Map<String, String> object) throws IOException {
		logger.info("Downloading dashboard json from AWS");
		
		if (StringUtils.isBlank(object.get("dataSourceName")) || StringUtils.isBlank(object.get("associatedCloudElementType")) ||
				StringUtils.isBlank(object.get("associatedSLAType")) || StringUtils.isBlank(object.get("jsonLocation")) ||
				StringUtils.isBlank(object.get("associatedCloud")) || StringUtils.isBlank(object.get("accountId")) || 
				StringUtils.isBlank(object.get("associatedCloudElementId"))) {
			logger.error("Mandatory fields missing");
			throw new BadRequestAlertException("Mandatory fields missing", "Dashboard", "mandatory.field.missing");
		}
		
		String dataSourceName = object.get("dataSourceName");
		String associatedCloudElementType = object.get("associatedCloudElementType");
		String associatedSLAType = object.get("associatedSLAType");
		String jsonLocation = object.get("jsonLocation");
		String associatedCloud = object.get("associatedCloud");
		String accountId = object.get("accountId");
		String associatedCloudElementId = object.get("associatedCloudElementId");
		
		String bucket = getBucket(jsonLocation);
		String fileName = getFileName(jsonLocation);
		
		Map<String, String> searchMap = new HashMap<>();
//		ServiceProviderCloudAccount spca = serviceProviderCloudAccountService.searchAllServiceProviderCloudAccount(searchMap).get(0);
		
		Dashboard dashboard = new Dashboard();
		AmazonS3 s3Client = null; //Utils.getAmazonS3Client(spca.getAccessKey(), spca.getSecretKey(), spca.getRegion());
		if(s3Client == null) {
			throw new BadRequestAlertException("AWS S3 client connection could not be establised", "Dashboard", "aws.s3.connection.failed");
		}
		
		S3Object file = s3Client.getObject(bucket, fileName);
		
		dashboard.setCloudName(associatedCloud);
		dashboard.setElementType(associatedCloudElementType);
		dashboard.setAccountId(accountId);
		dashboard.setInputType(associatedSLAType);
		dashboard.setFileName(fileName);
		dashboard.setInputSourceId(dataSourceName);
		String dName = associatedCloud+"_"+associatedCloudElementType+"_"+dataSourceName;
		dashboard.setTitle(dName);
		dashboard.setSlug(dName);
		dashboard.setCloudElementId(associatedCloudElementId);
		
		
		String data = displayTextInputStream(file.getObjectContent());
		
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();
		ObjectNode dataNode = (ObjectNode)mapper.readTree(data);
		for(JsonNode panel : dataNode.get("panels")) {
			ObjectNode oPanel = (ObjectNode)panel;
			oPanel.put("datasource", dataSourceName);
			arrayNode.add(oPanel);
			
			if(associatedCloudElementType.equalsIgnoreCase("API-Gateway")) {
				fillApiGatewayKey(associatedCloudElementId, mapper, oPanel);
			}else if(associatedCloudElementType.equalsIgnoreCase("Dynamodb")) {
				fillDynamoDbKey(associatedCloudElementId, mapper, oPanel);
			}
			
        }
		dataNode.put("panels", arrayNode);
		dataNode.put("id", 0);
		dataNode.put("uid", "");
		data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataNode);
		logger.debug("Datasource updated. Json : "+ data);
		
		String uid = RandomStringUtils.random(8, true, true);
		dashboard.setUid(uid);
		dashboard.setData(data);
		
		DashboardMeta meta = new DashboardMeta();
		meta.setSlug(dashboard.getSlug());
		
		dashboard.setDashboardMeta(meta);
		return dashboard;
	}

	private void fillDynamoDbKey(String associatedCloudElementId, ObjectMapper mapper, ObjectNode oPanel) {
		if(oPanel.get("targets") != null) {
			ArrayNode targetArray = mapper.createArrayNode();
			for(JsonNode targetNode: oPanel.get("targets")) {
				ObjectNode oTn = (ObjectNode)targetNode;
				
				ObjectNode oDimension = (ObjectNode)oTn.get("dimensions");
				if(oDimension != null && oDimension.get("TableName") != null) {
					oDimension.put("TableName", associatedCloudElementId);
				}
				if(oDimension != null && oDimension.get("Operation") != null) {
					oDimension.put("Operation", "Query");
				}
				oTn.put("dimensions", oDimension);
				
				targetArray.add(oTn);
			}
			oPanel.put("targets",targetArray);
		}
	}

	private void fillApiGatewayKey(String associatedCloudElementId, ObjectMapper mapper, ObjectNode oPanel) {
		if(oPanel.get("targets") != null) {
			ArrayNode targetArray = mapper.createArrayNode();
			for(JsonNode targetNode: oPanel.get("targets")) {
				ObjectNode oTn = (ObjectNode)targetNode;
				ObjectNode oDimension = (ObjectNode)oTn.get("dimensions");
				if(oDimension.get("ApiId") != null) {
					oDimension.put("ApiId", associatedCloudElementId);
				}
				oTn.put("dimensions", oDimension);
				targetArray.add(oTn);
			}
			oPanel.put("targets",targetArray);
		}
	}
	
	public Dashboard getDashboardFromAwsS3(Map<String, String> object, AmazonS3 s3Client, JsonNode dsInstanceJson) throws Exception {
		logger.info("Downloading dashboard json from AWS");
		
		if (StringUtils.isBlank(object.get("dataSourceName")) || StringUtils.isBlank(object.get("associatedCloudElementType")) ||
				StringUtils.isBlank(object.get("associatedSLAType")) || StringUtils.isBlank(object.get("jsonLocation")) ||
				StringUtils.isBlank(object.get("associatedCloud")) || StringUtils.isBlank(object.get("accountId")) || 
				StringUtils.isBlank(object.get("associatedCloudElementId"))) {
			logger.error("Mandatory fields missing");
			throw new BadRequestAlertException("Mandatory fields missing", "Dashboard", "mandatory.field.missing");
		}
		
		String dataSourceName = object.get("dataSourceName");
		String associatedCloudElementType = object.get("associatedCloudElementType");
		String associatedSLAType = object.get("associatedSLAType");
		String jsonLocation = object.get("jsonLocation");
		String associatedCloud = object.get("associatedCloud");
		String accountId = object.get("accountId");
		String associatedCloudElementId = object.get("associatedCloudElementId");
		
		String bucket = getBucket(jsonLocation);
		String fileName = getFileName(jsonLocation);
		
		Map<String, String> searchMap = new HashMap<>();
//		ServiceProviderCloudAccount spca = serviceProviderCloudAccountService.searchAllServiceProviderCloudAccount(searchMap).get(0);
		
		Dashboard dashboard = new Dashboard();
		try {
			S3Object file = s3Client.getObject(bucket, fileName);
			
			dashboard.setCloudName(associatedCloud);
			dashboard.setElementType(associatedCloudElementType);
			dashboard.setAccountId(accountId);
			dashboard.setInputType(associatedSLAType);
			dashboard.setFileName(fileName);
			dashboard.setInputSourceId(dataSourceName);
			String dName = associatedCloud+"_"+associatedCloudElementType+"_"+dataSourceName;
			dashboard.setTitle(dName);
			dashboard.setSlug(dName);
			String data = displayTextInputStream(file.getObjectContent());
			
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode arrayNode = mapper.createArrayNode();
			ObjectNode dataNode = (ObjectNode)mapper.readTree(data);
			for(JsonNode panel : dataNode.get("panels")) {
				ObjectNode oPanel = (ObjectNode)panel;
				for(JsonNode dsNode: dsInstanceJson) {
					oPanel.put("datasource", dsNode.get("name").asText());
				}
				arrayNode.add(oPanel);
				
				if(associatedCloudElementType.equalsIgnoreCase("API-Gateway")) {
					fillApiGatewayKey(associatedCloudElementId, mapper, oPanel);
				}else if(associatedCloudElementType.equalsIgnoreCase("Dynamodb")) {
					fillDynamoDbKey(associatedCloudElementId, mapper, oPanel);
				}
	        }
			
			dataNode.put("panels", arrayNode);
			dataNode.put("id", 0);
			dataNode.put("uid", "");
			data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataNode);
			logger.debug("Datasource updated. Json : "+ data);
			
			String uid = RandomStringUtils.random(8, true, true);
			dashboard.setUid(uid);
			dashboard.setData(data);
			
			DashboardMeta meta = new DashboardMeta();
			meta.setSlug(dashboard.getSlug());
			
			dashboard.setDashboardMeta(meta);
		}catch(Exception e) {
			throw e;
		}
		
		return dashboard;
	}
	
	private String displayTextInputStream(InputStream input) throws IOException {
        // Read the text input stream one line at a time and display each line.
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuffer sb = new StringBuffer();
		String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append(" ");
        }
        return sb.toString();
	}
	

	
	private String getBucket(String jsonLocation) {
		String f = jsonLocation.substring(jsonLocation.indexOf("//")+2);
		return f.substring(0,f.lastIndexOf("/"));
	}
	
	private String getFileName(String jsonLocation) {
		String f = jsonLocation.substring(jsonLocation.indexOf("//")+2);
		return f.substring(f.lastIndexOf("/")+1);
	}
}
