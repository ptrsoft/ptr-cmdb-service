package com.synectiks.asset.controller;

import com.synectiks.asset.api.model.LandingzoneDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.CloudElementSummary;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.mapper.LandingzoneMapper;
import com.synectiks.asset.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api")
public class InfraDiscoveryController {

    private final Logger logger = LoggerFactory.getLogger(InfraDiscoveryController.class);
    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CloudElementService cloudElementService;

    @Autowired
    private CloudElementSummaryService cloudElementSummaryService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VaultService vaultService;

    @RequestMapping(method = RequestMethod.GET, path = "/infra-discovery/aws")
    public void discoverAwsCloudElements(
            @RequestParam(name = "landingZone", required = true) String landingZone,
            @RequestParam(name = "elementType", required = true) String elementType,
            @RequestParam(name = "organization", required = false) String organization ,
            @RequestParam(name = "department", required = false) String department ,
            @RequestParam(name = "awsRegion", required = false) String awsRegion )  {

        String vaultAccountKey = null;
        if(!StringUtils.isBlank(organization) && !StringUtils.isBlank(department) && !StringUtils.isBlank(landingZone)){
            vaultAccountKey =  vaultService.resolveVaultKey(organization, department, Constants.AWS, landingZone);
        }else{
            LandingzoneDTO landingzoneDTO = new LandingzoneDTO();
            landingzoneDTO.setLandingZone(landingZone);
            Landingzone landingzone = LandingzoneMapper.INSTANCE.dtoToEntityForSearch(landingzoneDTO);
            logger.debug("Searching landing-zones by given landing-zone : {} ", landingZone);
            List<Landingzone> landingzoneList = landingzoneService.search(landingzone);

            for(Landingzone obj: landingzoneList){
                vaultAccountKey =  vaultService.resolveVaultKey(obj.getDepartment().getOrganization().getName(), obj.getDepartment().getName(), Constants.AWS, landingZone);
            }
        }




        String appConfigUrl = Constants.AWSX_API_APPCONFIG_URL+"?zone="+awsRegion+"&vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey;
        Map configSummary = this.restTemplate.getForObject(appConfigUrl, Map.class);
        saveAwsAppConfigSummary(organization, department, landingZone, configSummary);

        List resourceList = configSummary.get("ResourceCounts") != null ? (ArrayList)configSummary.get("ResourceCounts") : Collections.emptyList();
        for(Object obj: resourceList){
            Map awsResource = (Map)obj;
            String value = (String)awsResource.get("ResourceType");
            if(Constants.AWS_LAMBDA_FUNCTION_KEY.equalsIgnoreCase(value)){
                String lambdaUrl = Constants.AWSX_API_LAMBDA_URL+"?zone="+awsRegion+"&vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey;
                Object awsLambda = this.restTemplate.getForObject(lambdaUrl, Object.class);
                System.out.println("Lambda Map "+awsLambda.getClass().getName());
                if(awsLambda != null && awsLambda.getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
                    List lambdaList = (ArrayList)awsLambda;
                    for(Object lambdaObj: lambdaList){
                        Map lambdaMap = (Map)lambdaObj;
                        saveAwsLambda(organization, department, landingZone, lambdaMap);
                    }

                }else if(awsLambda != null && awsLambda.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
                    Map lambdaMap = (LinkedHashMap)awsLambda;
                    saveAwsLambda(organization, department, landingZone, lambdaMap);
                }
            }
        }
    }


    private void saveAwsAppConfigSummary(String organization, String department, String landingZone, Map configSummary) {
        List<CloudElementSummary> cloudElementSummaryList =  cloudElementSummaryService.getCloudElementSummary(organization, department, Constants.AWS, landingZone);
        if(cloudElementSummaryList != null && cloudElementSummaryList.size() > 0){
            logger.info("Updating cloud-element-summary for existing landing-zone: {}", landingZone);
            for(CloudElementSummary cloudElementSummary: cloudElementSummaryList){
                cloudElementSummary.setSummaryJson(configSummary);
                cloudElementSummaryService.save(cloudElementSummary);
            }
        }else{
            List<Landingzone> landingzoneList =  landingzoneService.getLandingZone(organization, department, Constants.AWS, landingZone);
            for(Landingzone landingzone: landingzoneList){
                logger.info("Adding cloud-element-summary for landing-zone: {}", landingZone);
                CloudElementSummary cloudElementSummary = CloudElementSummary.builder()
                        .summaryJson(configSummary)
                        .landingzone(landingzone)
                        .build();
                cloudElementSummaryService.save(cloudElementSummary);
            }
        }
    }

    private void saveAwsLambda(String organization, String department, String landingZone, Map lambdaMap) {
        List<CloudElement> cloudElementList =  cloudElementService.getCloudElement(organization, department, Constants.AWS, landingZone, (String)lambdaMap.get("FunctionArn"));
        if(cloudElementList != null && cloudElementList.size() > 0){
            logger.info("Updating cloud-element for existing landing-zone: {}", landingZone);
            for(CloudElement cloudElement: cloudElementList){
                if(((String)lambdaMap.get("FunctionArn")).equalsIgnoreCase(cloudElement.getArn())){
                    cloudElement.setConfigJson(lambdaMap);
                    cloudElement.setInstanceId((String)lambdaMap.get("FunctionName"));
                    cloudElement.setInstanceName((String)lambdaMap.get("FunctionName"));
                    cloudElementService.save(cloudElement);
                }
            }
        }else{
            addLambdaInDb(organization, department, landingZone, lambdaMap);
        }
    }

    private void addLambdaInDb(String organization, String department, String landingZone, Map lambdaMap) {
        List<Landingzone> landingzoneList =  landingzoneService.getLandingZone(organization, department, Constants.AWS, landingZone);
        for(Landingzone landingzone: landingzoneList){
            logger.info("Adding cloud-element for landing-zone: {}", landingZone);
            CloudElement cloudElementObj = CloudElement.builder()
                    .elementType(Constants.LAMBDA)
                    .arn((String) lambdaMap.get("FunctionArn"))
                    .instanceId((String) lambdaMap.get("FunctionName"))
                    .instanceName((String) lambdaMap.get("FunctionName"))
                    .category(Constants.APP_SERVICES)
                    .landingzone(landingzone)
                    .configJson(lambdaMap)
                    .build();
            cloudElementService.save(cloudElementObj);
        }
    }

}
