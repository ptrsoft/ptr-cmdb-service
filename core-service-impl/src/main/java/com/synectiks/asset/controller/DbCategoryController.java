package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.DbCategoryApi;
import com.synectiks.asset.api.model.DbCategoryDTO;
import com.synectiks.asset.domain.DbCategory;
import com.synectiks.asset.mapper.DbCategoryMapper;
import com.synectiks.asset.repository.DbCategoryRepository;
import com.synectiks.asset.service.DbCategoryService;
import com.synectiks.asset.web.rest.validation.Validator;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DbCategoryController implements DbCategoryApi {
	
    private final Logger logger = LoggerFactory.getLogger(DbCategoryController.class);

    private static final String ENTITY_NAME = "DbCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private DbCategoryService dbCategoryService;

    @Autowired
    private DbCategoryRepository dbCategoryRepository;

    @Autowired
    private Validator validator;

    @Override
    public ResponseEntity<DbCategoryDTO> getDbCategory(Long id) {
        logger.debug("REST request to get db-category : ID: {}", id);
        Optional<DbCategory> oOrg = dbCategoryService.findOne(id);
        DbCategoryDTO dbCategoryDTO = DbCategoryMapper.INSTANCE.entityToDto(oOrg.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(dbCategoryDTO));
    }

    @Override
    public ResponseEntity<List<DbCategoryDTO>> getDbCategoryList(){
        logger.debug("REST request to get all db-categories");
        List<DbCategory> dbCategoryList = dbCategoryService.findAll();
        List<DbCategoryDTO> dbCategoryDTOList = DbCategoryMapper.INSTANCE.entityToDtoList(dbCategoryList);
        return ResponseEntity.ok(dbCategoryDTOList);
    }

    @Override
    public ResponseEntity<DbCategoryDTO> addDbCategory(DbCategoryDTO dbCategoryDTO){
        logger.debug("REST request to add db-category : {}", dbCategoryDTO);
        validator.validateNotNull(dbCategoryDTO.getId(), ENTITY_NAME);
        DbCategory dbCategory = DbCategoryMapper.INSTANCE.dtoToEntity(dbCategoryDTO);
        dbCategory = dbCategoryService.save(dbCategory);
        DbCategoryDTO result = DbCategoryMapper.INSTANCE.entityToDto(dbCategory);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<DbCategoryDTO> updateDbCategory(DbCategoryDTO dbCategoryDTO) {
        logger.debug("REST request to update db-category : {}", dbCategoryDTO);
        validator.validateNull(dbCategoryDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(dbCategoryDTO.getId(), ENTITY_NAME, dbCategoryRepository);
        DbCategory existingDbCategory = dbCategoryRepository.findById(dbCategoryDTO.getId()).get();
        DbCategory tempDbCategory = DbCategoryMapper.INSTANCE.dtoToEntityForUpdate(dbCategoryDTO,existingDbCategory);
        DbCategory dbCategory = dbCategoryService.save(tempDbCategory);
        DbCategoryDTO result = DbCategoryMapper.INSTANCE.entityToDto(dbCategory);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<DbCategoryDTO>> searchDbCategory(DbCategoryDTO dbCategoryDTO) {
        DbCategory dbCategory = DbCategoryMapper.INSTANCE.dtoToEntityForSearch(dbCategoryDTO);
        logger.debug("REST request to get all db-categories on given filters : {} ", dbCategory);
        List<DbCategory> dbCategoryList = dbCategoryService.search(dbCategory);
        List<DbCategoryDTO> dbCategoryDTOList = DbCategoryMapper.INSTANCE.entityToDtoList(dbCategoryList);
        return ResponseEntity.ok(dbCategoryDTOList);
    }

}
