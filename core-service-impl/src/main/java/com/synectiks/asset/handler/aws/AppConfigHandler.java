package com.synectiks.asset.handler.aws;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElementSummary;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.service.CloudElementSummaryService;
import com.synectiks.asset.service.LandingzoneService;
import com.synectiks.asset.service.VaultService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AppConfigHandler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(AppConfigHandler.class);
    @Autowired
    private CloudElementSummaryService cloudElementSummaryService;

    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VaultService vaultService;

    @Override
    public void save(String organization, String department, String landingZone, String awsRegion) {
        String vaultAccountKey =  vaultService.resolveVaultKey(organization, department, Constants.AWS, landingZone);
        String params = "?zone="+awsRegion+"&vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey;
        if(StringUtils.isBlank(awsRegion)){
            params = "?vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey;
        }
        String appConfigUrl = Constants.AWSX_API_APPCONFIG_URL+params;
        Map appConfigSummaryResponse = this.restTemplate.getForObject(appConfigUrl, Map.class);

        List<CloudElementSummary> cloudElementSummaryList =  cloudElementSummaryService.getCloudElementSummary(organization, department, Constants.AWS, landingZone);
        if(cloudElementSummaryList != null && cloudElementSummaryList.size() > 0){
            logger.info("Updating cloud-element-summary for existing landing-zone: {}", landingZone);
            for(CloudElementSummary cloudElementSummary: cloudElementSummaryList){
                cloudElementSummary.setSummaryJson(appConfigSummaryResponse);
                cloudElementSummaryService.save(cloudElementSummary);
            }
        }else{
            List<Landingzone> landingzoneList =  landingzoneService.getLandingZone(organization, department, Constants.AWS, landingZone);
            for(Landingzone landingzone: landingzoneList){
                logger.info("Adding cloud-element-summary for landing-zone: {}", landingZone);
                CloudElementSummary cloudElementSummary = CloudElementSummary.builder()
                        .summaryJson(appConfigSummaryResponse)
                        .landingzone(landingzone)
                        .build();
                cloudElementSummaryService.save(cloudElementSummary);
            }
        }
    }
}
