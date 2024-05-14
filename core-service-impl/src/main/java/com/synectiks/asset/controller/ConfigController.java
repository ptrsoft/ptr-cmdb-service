package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.ConfigApi;
import com.synectiks.asset.api.model.ConfigDTO;
import com.synectiks.asset.domain.Config;
import com.synectiks.asset.mapper.ConfigMapper;
import com.synectiks.asset.repository.ConfigRepository;
import com.synectiks.asset.service.ConfigService;
import com.synectiks.asset.util.CryptoUtil;
import com.synectiks.asset.web.rest.validation.Validator;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ConfigController implements ConfigApi {
	
    private final Logger logger = LoggerFactory.getLogger(ConfigController.class);

    private static final String ENTITY_NAME = "Config";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private Validator validator;

    @Override
    public ResponseEntity<ConfigDTO> getConfig(@PathVariable("id") Long id) {
        logger.debug("REST request to get a config : ID: {}", id);
        Optional<Config> oObj = configService.findOne(id);
        ConfigDTO result = null;
        if(oObj.isPresent()){
            Config config = oObj.get();
            if(config.isEncrypted()){
                config.setValue(CryptoUtil.decrypt(config.getValue()));
            }
            result = ConfigMapper.INSTANCE.entityToDto(config);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    @Override
    public ResponseEntity<List<ConfigDTO>> getConfigList(){
        logger.debug("REST request to get all configs");
        List<Config> configList = configService.findAll();
        for(Config config: configList){
            if(config.isEncrypted()){
                config.setValue(CryptoUtil.decrypt(config.getValue()));
            }
        }
        List<ConfigDTO> configDTOList = ConfigMapper.INSTANCE.entityToDtoList(configList);
        return ResponseEntity.ok(configDTOList);
    }

    @Override
    public ResponseEntity<ConfigDTO> addConfig(ConfigDTO configDTO){
        logger.debug("REST request to add a config : {}", configDTO);
        validator.validateNotNull(configDTO.getId(), ENTITY_NAME);
        Config config = ConfigMapper.INSTANCE.dtoToEntity(configDTO);
        if(config.isEncrypted()){
            config.setValue(CryptoUtil.encrypt(config.getValue()));
        }
        config = configService.save(config);
        if(config.isEncrypted()){
            config.setValue(CryptoUtil.decrypt(config.getValue()));
        }
        ConfigDTO result = ConfigMapper.INSTANCE.entityToDto(config);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ConfigDTO> updateConfig(ConfigDTO configDTO) {
        logger.debug("REST request to update a config : {}", configDTO);
        validator.validateNull(configDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(configDTO.getId(), ENTITY_NAME, configRepository);
        Config existingConfig = configRepository.findById(configDTO.getId()).get();
        Config tempConfig = ConfigMapper.INSTANCE.dtoToEntityForUpdate(configDTO,existingConfig);
        if(tempConfig.isEncrypted()){
            tempConfig.setValue(CryptoUtil.encrypt(tempConfig.getValue()));
        }
        Config config = configService.save(tempConfig);
        if(config.isEncrypted()){
            config.setValue(CryptoUtil.decrypt(config.getValue()));
        }
        ConfigDTO result = ConfigMapper.INSTANCE.entityToDto(config);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ConfigDTO> getConfigByKey(@PathVariable("key") String key) {
        logger.debug("REST request to get config on a given key : {} ", key);
        Config config = configService.findByKey(key);
        if(config.isEncrypted()){
            config.setValue(CryptoUtil.decrypt(config.getValue()));
        }
        ConfigDTO configDTO = ConfigMapper.INSTANCE.entityToDto(config);
        return ResponseUtil.wrapOrNotFound(Optional.of(configDTO));
    }

    @Override
    public ResponseEntity<List<ConfigDTO>> searchConfig(ConfigDTO configDTO) {
        Config config = ConfigMapper.INSTANCE.dtoToEntityForSearch(configDTO);
        logger.debug("REST request to get all configs on given filters : {} ", config);
        List<Config> configList = configService.search(configDTO);
        for(Config temp: configList){
            if(temp.isEncrypted()){
                temp.setValue(CryptoUtil.decrypt(temp.getValue()));
            }
        }
        List<ConfigDTO> configDTOList = ConfigMapper.INSTANCE.entityToDtoList(configList);
        return ResponseEntity.ok(configDTOList);
    }

    @Override
    public ResponseEntity<ConfigDTO> addConfigWithEncryption(@RequestBody ConfigDTO configDTO){
        logger.debug("REST request to add a config with encryption");
        validator.validateNotNull(configDTO.getId(), ENTITY_NAME);
        configDTO.setValue(CryptoUtil.encrypt(configDTO.getValue()));
        Config config = ConfigMapper.INSTANCE.dtoToEntity(configDTO);
        config = configService.save(config);
        ConfigDTO result = ConfigMapper.INSTANCE.entityToDto(config);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ConfigDTO> updateConfigWithEncryption(ConfigDTO configDTO) {
        logger.debug("REST request to update a config with encrypted value : {}", configDTO);
        validator.validateNull(configDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(configDTO.getId(), ENTITY_NAME, configRepository);
        Config existingConfig = configRepository.findById(configDTO.getId()).get();
        configDTO.setValue(CryptoUtil.encrypt(configDTO.getValue()));
        Config tempConfig = ConfigMapper.INSTANCE.dtoToEntityForUpdate(configDTO,existingConfig);
        Config config = configService.save(tempConfig);
        ConfigDTO result = ConfigMapper.INSTANCE.entityToDto(config);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ConfigDTO> getDecryptedConfig(@PathVariable("key") String key){
        logger.debug("REST request to get decrypted config on given key : {} ", key);
        Config config = configService.findByKey(key);
        config.setValue(CryptoUtil.decrypt(config.getValue()));
        ConfigDTO configDTO = ConfigMapper.INSTANCE.entityToDto(config);
        return ResponseUtil.wrapOrNotFound(Optional.of(configDTO));
    }



}
