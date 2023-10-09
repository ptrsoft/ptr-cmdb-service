package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.InfraDiscoveryApi;
import com.synectiks.asset.api.model.CloudElementDTO;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.handler.factory.AwsHandlerFactory;
import com.synectiks.asset.mapper.CloudElementMapper;
import com.synectiks.asset.service.CloudElementService;
import com.synectiks.asset.service.LandingzoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InfraDiscoveryController implements InfraDiscoveryApi {

    private final Logger logger = LoggerFactory.getLogger(InfraDiscoveryController.class);
    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private CloudElementService cloudElementService;

    @Override
    public ResponseEntity<List<CloudElementDTO>> discoverAwsCloudElements(Long orgId, String landingZone, String elementType, String awsRegion )  {
        logger.debug("REST request to pull aws element details. Org id: {}, landing-zone: {}, element type: {}", orgId, landingZone, elementType);
        List<Landingzone> landingzoneList = landingzoneService.getLandingzones(landingZone);
        for(Landingzone obj: landingzoneList){
            CloudHandler cloudHandler = AwsHandlerFactory.getHandler(elementType);
            cloudHandler.save(obj.getDepartment().getOrganization(), obj.getDepartment(), obj, awsRegion);
        }
        CloudElementDTO cloudElementDTO = new CloudElementDTO();
        cloudElementDTO.setLandingZone(landingZone);
        cloudElementDTO.setElementType(elementType);
        List<CloudElement> cloudElementList = cloudElementService.search(cloudElementDTO);
        List<CloudElementDTO> cloudElementDTOList = CloudElementMapper.INSTANCE.entityToDtoList(cloudElementList);
        return ResponseEntity.ok(cloudElementDTOList);
    }

//    private List<Landingzone> getLandingzones(String landingZone) {
//        LandingzoneDTO landingzoneDTO = new LandingzoneDTO();
//        landingzoneDTO.setLandingZone(landingZone);
//        landingzoneDTO.setCloud(Constants.AWS);
//        Landingzone landingzone = LandingzoneMapper.INSTANCE.dtoToEntityForSearch(landingzoneDTO);
//        logger.debug("Searching landing-zones by given landing-zone : {} ", landingZone);
//        List<Landingzone> landingzoneList = landingzoneService.search(landingzone);
//        return landingzoneList;
//    }
}
