package com.synectiks.asset.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synectiks.asset.api.controller.ModuleApi;
import com.synectiks.asset.api.model.ModuleDTO;
import com.synectiks.asset.api.model.ProductEnvDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Module;
import com.synectiks.asset.domain.Product;
import com.synectiks.asset.domain.ProductEnv;
import com.synectiks.asset.mapper.ModuleMapper;
import com.synectiks.asset.mapper.ProductEnvMapper;
import com.synectiks.asset.repository.ModuleRepository;
import com.synectiks.asset.service.ModuleService;
import com.synectiks.asset.util.AppkubeStringUtil;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import com.synectiks.asset.web.rest.validation.Validator;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ModuleController implements ModuleApi {

	private final Logger logger = LoggerFactory.getLogger(ModuleController.class);

	private static final String ENTITY_NAME = "Module";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	@Autowired
	private ModuleService moduleService;

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private Validator validator;

	@Autowired
	private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;

	@Override
	public ResponseEntity<ModuleDTO> getModule(Long id) {
		logger.debug("REST request to get a module : ID: {}", id);
		Optional<Module> oObj = moduleService.findOne(id);
		ModuleDTO moduleDTO = ModuleMapper.INSTANCE.entityToDto(oObj.orElse(null));
		return ResponseUtil.wrapOrNotFound(Optional.of(moduleDTO));
	}

	@Override
	public ResponseEntity<List<ModuleDTO>> getModuleList(){
		logger.debug("REST request to get all modules");
		List<Module> moduleList = moduleService.findAll();
		List<ModuleDTO> moduleDTOList = ModuleMapper.INSTANCE.entityToDtoList(moduleList);
		return ResponseEntity.ok(moduleDTOList);
	}

	@Override
	public ResponseEntity<ModuleDTO> addModule(ModuleDTO moduleDTO){
		logger.debug("REST request to add a module : {}", moduleDTO);
		validator.validateNotNull(moduleDTO.getId(), ENTITY_NAME);

		Module org = ModuleMapper.INSTANCE.dtoToEntityForSearch(moduleDTO);
		List<Module> moduleList = moduleService.search(org);
		if(moduleList.size() > 0){
			logger.warn("Module already exists");
			return ResponseEntity.ok(ModuleMapper.INSTANCE.entityToDto(moduleList.get(0)));
		}
		Module module = ModuleMapper.INSTANCE.dtoToEntity(moduleDTO);
		module = moduleService.save(module);
		ModuleDTO result = ModuleMapper.INSTANCE.entityToDto(module);
		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<List<ModuleDTO>> bulkAddModule(@RequestBody Object obj)  {
		logger.debug("REST request to bulk-add all the modules of a given product and product environments in module");
		ObjectMapper objectMapper = Constants.instantiateMapper();
		List<ModuleDTO> moduleDTOList = new ArrayList<>();
		try{
			String jsonString = jsonAndObjectConverterUtil.convertObjectToJsonString(objectMapper, obj, Map.class);
			JsonNode rootNode = objectMapper.readTree(jsonString);
			final JsonNode envArray = rootNode.get("envs");
			for (final JsonNode env : envArray) {
				final JsonNode moduleArray = env.get("modules");
				for(final JsonNode modules: moduleArray){
					Module module = Module.builder()
							.product(Product.builder().id(rootNode.get("productId").asLong()).build())
							.productEnv(ProductEnv.builder().id(env.get("id").asLong()).build())
							.name(AppkubeStringUtil.removeLeadingAndTrailingDoubleQuotes(modules.get("name").asText()))
							.moduleNature(AppkubeStringUtil.removeLeadingAndTrailingDoubleQuotes(modules.get("moduleNature").asText()))
							.build();

					module = moduleService.save(module);
					ModuleDTO result = ModuleMapper.INSTANCE.entityToDto(module);
					moduleDTOList.add(result);
				}

			}

			return ResponseEntity.ok(moduleDTOList);
		}catch (JsonProcessingException je){
			logger.error("Exception: ", je);
			return ResponseUtil.wrapOrNotFound(Optional.empty());
		}

	}
	@Override
	public ResponseEntity<ModuleDTO> updateModule(ModuleDTO moduleDTO) {
		logger.debug("REST request to update a module : {}", moduleDTO);
		validator.validateNull(moduleDTO.getId(), ENTITY_NAME);
		validator.validateEntityExistsInDb(moduleDTO.getId(), ENTITY_NAME, moduleRepository);
		Module existingModule = moduleRepository.findById(moduleDTO.getId()).get();
		Module tempModule = ModuleMapper.INSTANCE.dtoToEntityForUpdate(moduleDTO,existingModule);
		Module module = moduleService.save(tempModule);
		ModuleDTO result = ModuleMapper.INSTANCE.entityToDto(module);
		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<List<ModuleDTO>> searchModule(ModuleDTO moduleDTO) {
		Module module = ModuleMapper.INSTANCE.dtoToEntityForSearch(moduleDTO);
		logger.debug("REST request to get all modules on given filters : {} ", module);
		List<Module> moduleList = moduleService.search(module);
		List<ModuleDTO> moduleDTOList = ModuleMapper.INSTANCE.entityToDtoList(moduleList);
		return ResponseEntity.ok(moduleDTOList);
	}

	@Override
	public ResponseEntity<List<ModuleDTO>> searchModuleByFilter(@RequestParam("departmentId") Long departmentId,
																@RequestParam("productId") Long productId,
																@RequestParam("productEnvId") Long productEnvId,
																@RequestParam("serviceNature") String serviceNature) {
		List<Module> moduleList = moduleService.searchByFilter(departmentId,productId, productEnvId, serviceNature);
		List<ModuleDTO> moduleDTOList = ModuleMapper.INSTANCE.entityToDtoList(moduleList);
		return ResponseEntity.ok(moduleDTOList);
	}
}
