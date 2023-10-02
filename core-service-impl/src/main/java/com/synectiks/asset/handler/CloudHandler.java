package com.synectiks.asset.handler;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.Department;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.domain.Organization;
import com.synectiks.asset.handler.aws.TagProcessor;
import com.synectiks.asset.service.VaultService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CloudHandler {

    public void save(Organization organization, Department department, Landingzone landingZone, String awsRegion);
    default String getUrl() {
        return null;
    }

    default Object getResponse(VaultService vaultService, RestTemplate restTemplate, String url, Organization organization, Department department, Landingzone landingZone, String awsRegion) {
        String vaultAccountKey =  vaultService.resolveVaultKey(organization.getName(), department.getName(), Constants.AWS, landingZone.getLandingZone());
        String params = "?zone="+ awsRegion +"&vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey;
        if(StringUtils.isBlank(awsRegion)){
            params = "?vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey;
        }
        String awsxUrl = url+params;
        Object response = restTemplate.getForObject(awsxUrl, Object.class);
        return response;
    }

    default Map<String, List<Object>> processTag(CloudElement cloudElement){
        return null;
    }

    default Map<String, Object> validate(String data[], CloudElement cloudElement, TagProcessor tagProcessor){
        Map<String, Object> errorMap = new HashMap<>();
        Department department = cloudElement.getLandingzone().getDepartment();
        Organization organization = cloudElement.getLandingzone().getDepartment().getOrganization();
        Organization tagOrganization = tagProcessor.getOrganization(data[0]);
        if(tagOrganization == null || (tagOrganization != null && tagOrganization.getId() != organization.getId())) {
            errorMap.put("errorCode", 1);
            errorMap.put("errorMsg",String.format("Tagging failed. Tag's organization does not match with landing-zone's organization. Landing-zone org: %s, Tag org: %s", cloudElement.getLandingzone().getDepartment().getOrganization().getName(), data[0]));
            return errorMap;
        }
        Department tagDepartment = tagProcessor.getDepartment(data[1], tagOrganization.getId());
        if(tagDepartment == null || (tagDepartment != null && tagDepartment.getId() != department.getId())){
            errorMap.put("errorCode", 2);
            errorMap.put("errorMsg",String.format("Tagging failed. Tag's department does not match with landing-zone's department. Landing-zone dep: %s, Tag dep: %s", cloudElement.getLandingzone().getDepartment().getName(), data[1]));
            return errorMap;
        }
        return errorMap;
    }
}
