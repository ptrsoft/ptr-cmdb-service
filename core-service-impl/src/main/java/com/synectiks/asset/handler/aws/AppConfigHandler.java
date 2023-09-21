package com.synectiks.asset.handler.aws;

import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElementSummary;
import com.synectiks.asset.domain.Department;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.domain.Organization;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.service.CloudElementSummaryService;
import com.synectiks.asset.service.LandingzoneService;
import com.synectiks.asset.service.VaultService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AppConfigHandler implements CloudHandler {

    private final Logger logger = LoggerFactory.getLogger(AppConfigHandler.class);
    private final Environment env;

    public AppConfigHandler(Environment env){
        this.env = env;
    }
    @Autowired
    private CloudElementSummaryService cloudElementSummaryService;

    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VaultService vaultService;

    @Override
    public void save(Organization organization, Department department, Landingzone landingZone, String awsRegion) {
        Object response = getResponse(vaultService, restTemplate, getUrl(), organization, department, landingZone, awsRegion);

        Map appConfigSummaryResponse = (Map)response;

        List<CloudElementSummary> cloudElementSummaryList =  cloudElementSummaryService.getCloudElementSummary(landingZone.getId());
        if(cloudElementSummaryList != null && cloudElementSummaryList.size() > 0){
            logger.info("Updating cloud-element-summary for existing landing-zone: {}", landingZone);
            for(CloudElementSummary cloudElementSummary: cloudElementSummaryList){
                cloudElementSummary.setSummaryJson(appConfigSummaryResponse);
                cloudElementSummaryService.save(cloudElementSummary);
            }
        }else{
            logger.info("Adding cloud-element-summary for landing-zone: {}", landingZone);
            CloudElementSummary cloudElementSummary = CloudElementSummary.builder()
                .summaryJson(appConfigSummaryResponse)
                .landingzone(landingZone)
                .build();
            cloudElementSummaryService.save(cloudElementSummary);
        }
    }

    @Override
    public String getUrl(){
        return env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.appconfig-api");
    }
}
