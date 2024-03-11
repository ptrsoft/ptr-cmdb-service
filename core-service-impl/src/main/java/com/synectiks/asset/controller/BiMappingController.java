package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.BiMappingApi;
import com.synectiks.asset.domain.Department;
import com.synectiks.asset.domain.Organization;
import com.synectiks.asset.service.BiMappingService;
import com.synectiks.asset.service.DepartmentService;
import com.synectiks.asset.service.OrganizationService;
import com.synectiks.asset.web.rest.errors.BadRequestAlertException;
import com.synectiks.asset.web.rest.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class BiMappingController implements BiMappingApi {

    private final Logger logger = LoggerFactory.getLogger(BiMappingController.class);

    private static final String ENTITY_NAME = "BiMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private Validator validator;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private BiMappingService biMappingService;

    @Override
    public ResponseEntity<Object> addBiMapping(@RequestBody Object requestObj) {
        logger.info("REST request to complete bi-mapping");
        validator.validateNull(requestObj, ENTITY_NAME);
        Map objectMap = null;
        if(requestObj.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
            objectMap = (LinkedHashMap)requestObj;
        }
        Map org = (Map)objectMap.get("org");
        Optional<Organization> oOrg = organizationService.findOne(((Integer)org.get("id")).longValue());
        if(!oOrg.isPresent()){
            logger.error("Organization not found. Given organization id: {}",org.get("id"));
            throw new BadRequestAlertException(String.format("Null id", "Organization"), "Organization", "idnull");
        }
        Map departmentMap = (Map)org.get("dep");
        Optional<Department> oDep = departmentService.findOne(((Integer)departmentMap.get("id")).longValue());
        if(!oDep.isPresent()){
            logger.error("Department not found. Given department id: {}",departmentMap.get("id"));
            throw new BadRequestAlertException(String.format("Null id", "Department"), "Department", "idnull");
        }
        Map<String, String> status = biMappingService.save(oOrg.get(),oDep.get(),departmentMap);
        if(Integer.parseInt(status.get("status")) == 0){
            return ResponseEntity.ok(status.get("message"));
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Services could not saved due to some error");


    }
}
