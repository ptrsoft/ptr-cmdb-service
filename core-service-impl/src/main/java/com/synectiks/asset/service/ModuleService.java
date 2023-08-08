package com.synectiks.asset.service;

import com.synectiks.asset.domain.Module;
import com.synectiks.asset.repository.ModuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Module}.
 */
@Service
public class ModuleService {

	private final Logger logger = LoggerFactory.getLogger(ModuleService.class);

	@Autowired
	private ModuleRepository moduleRepository;

	public Module save(Module module) {
        logger.debug("Request to save module : {}", module);
        return moduleRepository.save(module);
    }

    @Transactional(readOnly = true)
    public List<Module> findAll() {
        logger.debug("Request to get all modules");
        return moduleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Module> findOne(Long id) {
        logger.debug("Request to get a module : {}", id);
        return moduleRepository.findById(id);
    }

    public void delete(Long id) {
        logger.debug("Request to delete a module : {}", id);
        moduleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
	public List<Module> search(Module module) {
		logger.debug("Get all modules on given filters");
        return moduleRepository.findAll(Example.of(module), Sort.by(Sort.Direction.DESC, "id"));
	}


}
