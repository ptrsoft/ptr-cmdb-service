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
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class InfraDiscoveryController implements InfraDiscoveryApi {

    private final Logger logger = LoggerFactory.getLogger(InfraDiscoveryController.class);
    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private CloudElementService cloudElementService;

    @Override
    public ResponseEntity<Object> discoverCloudElements(Long orgId, String elementType, Long landingZoneId, String query )  {
        logger.debug("REST request to pull aws element details. Org id: {}, landingZoneId: {}, elementType: {}, query: {}", orgId, landingZoneId, elementType, query);
        Optional<Landingzone> oLz = landingzoneService.findOne(landingZoneId);
        if(!oLz.isPresent()){
            logger.error("landingZoneId does not exists");
            return ResponseUtil.wrapOrNotFound(Optional.empty());
        }
        CloudHandler cloudHandler = AwsHandlerFactory.getHandlerByQuery(query);
        Object object = cloudHandler.save(elementType, oLz.get(), query);
        return ResponseEntity.ok(object);
    }

    // aws cron job
    // get data from clod table. if it is cron-scheduled, do below steps otherwise skip this element
    // loop landing zone for aws and element type from cloud table
    // pull data-list and add/update cmdb cloud element table

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
