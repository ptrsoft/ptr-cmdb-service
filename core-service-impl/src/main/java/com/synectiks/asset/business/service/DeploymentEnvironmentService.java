package com.synectiks.asset.business.service;

import com.synectiks.asset.business.domain.DeploymentEnvironment;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.repository.DeploymentEnvironmentRepository;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeploymentEnvironmentService {

	private final Logger log = LoggerFactory.getLogger(DeploymentEnvironmentService.class);

	@Autowired
	private DeploymentEnvironmentRepository deploymentEnvironmentRepository;

	@Autowired
	private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;

	public DeploymentEnvironment save(DeploymentEnvironment deploymentEnvironment) {
		log.debug("Request to save DeploymentEnvironment : {}", deploymentEnvironment);
		DeploymentEnvironment de = deploymentEnvironmentRepository.save(deploymentEnvironment);

//		if (de != null) {
//			Map<String, String> filter = new HashMap<>();
//			filter.put("deploymentEnvironmentId", String.valueOf(de.getId()));
//			try {
//				List<Module> moduleList = serviceAllocationService.getModules(filter);
//				de.setModules(moduleList);
//			} catch (IOException e) {
//				log.error("Error in getting modules: ", e.getMessage());
//			}
//		}

		return de;
	}

	public Optional<DeploymentEnvironment> partialUpdate(DeploymentEnvironment deploymentEnvironment) {
		log.debug("Request to partially update DeploymentEnvironment : {}", deploymentEnvironment);

		return deploymentEnvironmentRepository.findById(deploymentEnvironment.getId())
				.map(existingDeploymentEnvironment -> {
					if (deploymentEnvironment.getName() != null) {
						existingDeploymentEnvironment.setName(deploymentEnvironment.getName());
					}
					if (deploymentEnvironment.getStatus() != null) {
						existingDeploymentEnvironment.setStatus(deploymentEnvironment.getStatus());
					}

//					Map<String, String> filter = new HashMap<>();
//					filter.put("deploymentEnvironmentId", String.valueOf(existingDeploymentEnvironment.getId()));
//					try {
//						List<Module> moduleList = serviceAllocationService.getModules(filter);
//						existingDeploymentEnvironment.setModules(moduleList);
//					} catch (IOException e) {
//						log.error("Error in getting modules: ", e.getMessage());
//					}

					return existingDeploymentEnvironment;
				}).map(deploymentEnvironmentRepository::save);
	}

	@Transactional(readOnly = true)
	public List<DeploymentEnvironment> findAll() {
		log.debug("Request to get all DeploymentEnvironments");
		List<DeploymentEnvironment> list = deploymentEnvironmentRepository.findAll();
//		Map<String, String> filter = new HashMap<>();
//		for (DeploymentEnvironment depEnv : list) {
//			filter.clear();
//			filter.put("deploymentEnvironmentId", String.valueOf(depEnv.getId()));
//			try {
//				List<Module> deList = serviceAllocationService.getModules(filter);
//				depEnv.setModules(deList);
//			} catch (IOException e) {
//				log.error("Error in getting modules: ", e.getMessage());
//			}
//		}
		return list;

	}

	@Transactional(readOnly = true)
	public Optional<DeploymentEnvironment> findOne(Long id) {
		log.debug("Request to get DeploymentEnvironment : {}", id);
		Optional<DeploymentEnvironment> ode = deploymentEnvironmentRepository.findById(id);
//		if (ode.isPresent()) {
//			Map<String, String> filter = new HashMap<>();
//			filter.put("deploymentEnvironmentId", String.valueOf(ode.get().getId()));
//			try {
//				List<Module> deList = serviceAllocationService.getModules(filter);
//				ode.get().setModules(deList);
//			} catch (IOException e) {
//				log.error("Error in getting modules: ", e.getMessage());
//			}
//		}
		return ode;
	}

	public void delete(Long id) {
		log.debug("Request to delete DeploymentEnvironment : {}", id);
		deploymentEnvironmentRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<DeploymentEnvironment> search(Map<String, String> filter) throws IOException {
		log.debug("Request to get all DeploymentEnvironment on given filters");

		DeploymentEnvironment deploymentEnvironment = jsonAndObjectConverterUtil
				.convertSourceObjectToTarget(Constants.instantiateMapper(), filter, DeploymentEnvironment.class);

		// unset default value if createdOn is not coming in filter
		if (StringUtils.isBlank(filter.get(Constants.CREATED_ON))) {
			deploymentEnvironment.setCreatedOn(null);
		}
		// unset default value if updatedOn is not coming in filter
		if (StringUtils.isBlank(filter.get(Constants.UPDATED_ON))) {
			deploymentEnvironment.setUpdatedOn(null);
		}
		List<DeploymentEnvironment> list = deploymentEnvironmentRepository.findAll(Example.of(deploymentEnvironment),
				Sort.by(Sort.Direction.DESC, "name"));

//		Map<String, String> modulefilter = new HashMap<>();
//		for (DeploymentEnvironment de : list) {
//			modulefilter.clear();
//			modulefilter.put("deploymentEnvironmentId", String.valueOf(de.getId()));
//			try {
//				List<Module> deList = serviceAllocationService.getModules(modulefilter);
//				de.setModules(deList);
//			} catch (IOException e) {
//				log.error("Error in getting modules: ", e.getMessage());
//			}
//		}
		return list;
	}


}
