package com.synectiks.asset.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.api.model.LandingzoneDTO;
import com.synectiks.asset.api.model.LandingzoneResponseDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class VaultService {

    private final Logger logger = LoggerFactory.getLogger(VaultService.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DepartmentService departmentService;


    public JsonNode getServiceProviderAwsCreds() throws JsonProcessingException {
        String url = Constants.VAULT_URL + Constants.VAULT_PROVIDER_AWS_CREDS_KEY;
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JsonNode jsonNode = Constants.instantiateMapper().readTree(response.getBody());
        return jsonNode.get("data");
    }



    private HttpHeaders createHttpHeaders()	{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(Constants.VAULT_HEADER, Constants.VAULT_ROOT_TOKEN);
        return headers;
    }
    public String resolveVaultKey(String organization, String department, String cloud, String landingZone){
        String landingZonePath = Constants.VAULT_LANDING_ZONE_PATH
                .replaceAll("_organization_", organization.toUpperCase())
                .replaceAll("_department_",department.toUpperCase())
                .replaceAll("_cloud_",cloud.toUpperCase())
                .replaceAll("_landing-zone_",landingZone);
        return landingZonePath;
    }

    public ObjectNode prepareObjectToSaveInVault(LandingzoneResponseDTO landingzoneResponseDTO) throws JsonProcessingException {
//        Optional<Department> oDept = departmentService.findOne(landingzoneDTO.getDepartmentId());
        ObjectMapper objectMapper = Constants.instantiateMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put(Constants.ORGANIZATION_NAME,landingzoneResponseDTO.getOrganizationName());
        objectNode.put(Constants.DEPARTMENT_NAME,landingzoneResponseDTO.getDepartmentName());
        objectNode.put(Constants.CLOUD, landingzoneResponseDTO.getCloud());
        objectNode.put(Constants.LANDING_ZONE, landingzoneResponseDTO.getLandingZone());


//        objectNode.put(Constants.DISPLAY_NAME, landingzoneDTO.getDisplayName());

        if(Constants.AWS.equalsIgnoreCase(landingzoneResponseDTO.getCloud())){
            JsonNode jsonNode = getServiceProviderAwsCreds();
            objectNode.put(Constants.REGION, Constants.DEFAULT_AWS_REGION);
            objectNode.put(Constants.EXTERNAL_ID, landingzoneResponseDTO.getExternalId());
            objectNode.put(Constants.CROSS_ACCOUNT_ROLE_ARN, landingzoneResponseDTO.getRoleArn());
            objectNode.put(Constants.ACCESS_KEY, jsonNode.get(Constants.ACCESS_KEY));
            objectNode.put(Constants.SECRET_KEY, jsonNode.get(Constants.SECRET_KEY));
        }
        return objectNode;
    }

    public HttpStatus saveLandingZone(ObjectNode obj) throws JsonProcessingException {
        String landingZonePath =  resolveVaultKey(obj.get(Constants.ORGANIZATION_NAME).asText(), obj.get(Constants.DEPARTMENT_NAME).asText(), obj.get(Constants.CLOUD).asText(), obj.get(Constants.LANDING_ZONE).asText());
        String postUrl = Constants.VAULT_URL + landingZonePath;
        logger.info("Saving landing-zone to vault. Vault url : {}",postUrl);

        if(Constants.AWS.equalsIgnoreCase(obj.get(Constants.CLOUD).asText())){
            ObjectMapper objectMapper = Constants.instantiateMapper();
            ObjectNode node = objectMapper.createObjectNode();

            node.put(Constants.REGION, obj.get(Constants.REGION).asText());
            node.put(Constants.EXTERNAL_ID, obj.get(Constants.EXTERNAL_ID).asText());
            node.put(Constants.CROSS_ACCOUNT_ROLE_ARN, obj.get(Constants.CROSS_ACCOUNT_ROLE_ARN).asText());
//        node.put(Constants.DISPLAY_NAME, obj.get(Constants.DISPLAY_NAME).asText());
            node.put(Constants.ACCESS_KEY, obj.get(Constants.ACCESS_KEY).asText());
            node.put(Constants.SECRET_KEY, obj.get(Constants.SECRET_KEY).asText());

            HttpHeaders headers = createHttpHeaders();
            HttpEntity<String> request = new HttpEntity<String>(node.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(postUrl, HttpMethod.POST, request, String.class);
            return response.getStatusCode();
        }
        return HttpStatus.NOT_IMPLEMENTED;

    }
}
