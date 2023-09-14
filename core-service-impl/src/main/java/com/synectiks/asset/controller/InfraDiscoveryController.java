package com.synectiks.asset.controller;

import com.synectiks.asset.api.model.LandingzoneDTO;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.handler.AwsHandlerFactory;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.mapper.LandingzoneMapper;
import com.synectiks.asset.service.LandingzoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InfraDiscoveryController {

    private final Logger logger = LoggerFactory.getLogger(InfraDiscoveryController.class);
    @Autowired
    private LandingzoneService landingzoneService;

    @RequestMapping(method = RequestMethod.GET, path = "/infra-discovery/aws")
    public void discoverAwsCloudElements(
            @RequestParam(name = "landingZone", required = true) String landingZone,
            @RequestParam(name = "elementType", required = true) String elementType,
            @RequestParam(name = "awsRegion", required = false) String awsRegion )  {

        List<Landingzone> landingzoneList = getLandingzones(landingZone);
        for(Landingzone obj: landingzoneList){
            logger.debug("Getting data for element: {} and landing-zone: {} ", elementType, landingZone);
            CloudHandler cloudHandler = AwsHandlerFactory.getHandler(elementType);
            cloudHandler.save(obj.getDepartment().getOrganization().getName(), obj.getDepartment().getName(), landingZone, awsRegion);
        }
    }

    private List<Landingzone> getLandingzones(String landingZone) {
        LandingzoneDTO landingzoneDTO = new LandingzoneDTO();
        landingzoneDTO.setLandingZone(landingZone);
        Landingzone landingzone = LandingzoneMapper.INSTANCE.dtoToEntityForSearch(landingzoneDTO);
        logger.debug("Searching landing-zones by given landing-zone : {} ", landingZone);
        List<Landingzone> landingzoneList = landingzoneService.search(landingzone);
        return landingzoneList;
    }
}
