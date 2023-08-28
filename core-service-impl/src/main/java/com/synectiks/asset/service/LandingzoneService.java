package com.synectiks.asset.service;

import com.synectiks.asset.domain.CloudElementSummary;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.repository.LandingzoneRepository;
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
 * Service Implementation for managing {@link Landingzone}.
 */
@Service
public class LandingzoneService {

	private final Logger logger = LoggerFactory.getLogger(LandingzoneService.class);

	@Autowired
	private LandingzoneRepository landingzoneRepository;

	public Landingzone save(Landingzone landingzone) {
        logger.debug("Request to save landing-zone : {}", landingzone);
        return landingzoneRepository.save(landingzone);
    }

    @Transactional(readOnly = true)
    public List<Landingzone> findAll() {
        logger.debug("Request to get all landing-zones");
        return landingzoneRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Landingzone> findOne(Long id) {
        logger.debug("Request to get a landing-zone : {}", id);
        return landingzoneRepository.findById(id);
    }

    public void delete(Long id) {
        logger.debug("Request to delete a landing-zone : {}", id);
        landingzoneRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
	public List<Landingzone> search(Landingzone landingzone) {
		logger.debug("Get all landing-zones on given filters");
        return landingzoneRepository.findAll(Example.of(landingzone), Sort.by(Sort.Direction.DESC, "id"));
	}

    @Transactional(readOnly = true)
    public List<Landingzone> getLandingZone(String organization, String department, String cloud, String landingZone) {
        logger.debug("Get all landingzones on given criteria");
        return landingzoneRepository.getLandingZone(organization, department, cloud, landingZone);
    }
}
