package com.synectiks.asset.service;

import com.synectiks.asset.api.model.LandingzoneDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElementSummary;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.mapper.LandingzoneMapper;
import com.synectiks.asset.repository.LandingzoneRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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

    @Autowired
    private EntityManager entityManager;

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
	public List<Landingzone> search(LandingzoneDTO landingzoneDTO) {
		logger.debug("Search landing-zone on given filters");
        StringBuilder primarySql = new StringBuilder("select l.*  from landingzone l left join organization o on o.id = l.organization_id\n" +
                "left join department d on d.id = l.department_id\n" +
                "left join cloud_element_summary ces on ces.landingzone_id = l.id \n" +
                "where 1 = 1 ");
        if(landingzoneDTO.getId() != null){
            primarySql.append(" and l.id = ? ");
        }
        if(!StringUtils.isBlank(landingzoneDTO.getDescription())){
            primarySql.append(" and l.description = ? ");
        }
        if(!StringUtils.isBlank(landingzoneDTO.getLandingZone())){
            primarySql.append(" and l.landing_zone = ? ");
        }
        if(landingzoneDTO.getDepartmentId() != null){
            primarySql.append(" and l.department_id = ? ");
        }
        if(!StringUtils.isBlank(landingzoneDTO.getDepartmentName())){
            primarySql.append(" and upper(d.name) = upper(?) ");
        }
        if(!StringUtils.isBlank(landingzoneDTO.getCloud())){
            primarySql.append(" and upper(l.cloud) = upper(?) ");
        }
        if(!StringUtils.isBlank(landingzoneDTO.getDisplayName())){
            primarySql.append(" and l.display_name = ? ");
        }
        if(!StringUtils.isBlank(landingzoneDTO.getRoleArn())){
            primarySql.append(" and l.role_arn = ? ");
        }
        if(!StringUtils.isBlank(landingzoneDTO.getExternalId())){
            primarySql.append(" and l.external_id = ? ");
        }
        if(!StringUtils.isBlank(landingzoneDTO.getStatus())){
            primarySql.append(" and upper(l.status) = upper(?) ");
        }
        if(!StringUtils.isBlank(landingzoneDTO.getCreatedBy())){
            primarySql.append(" and l.created_by = ? ");
        }
        if(!StringUtils.isBlank(landingzoneDTO.getUpdatedBy())){
            primarySql.append(" and l.updated_by = ? ");
        }
        if(landingzoneDTO.getOrganizationId() != null){
            primarySql.append(" and l.organization_id = ? ");
        }
        if(!StringUtils.isBlank(landingzoneDTO.getOrganizationName())){
            primarySql.append(" and upper(o.name) = upper(?) ");
        }
        Query query = entityManager.createNativeQuery(primarySql.toString(), Landingzone.class);
        int index = 0;
        if(landingzoneDTO.getId() != null){
            query.setParameter(++index, landingzoneDTO.getId());
        }
        if(!StringUtils.isBlank(landingzoneDTO.getDescription())){
            query.setParameter(++index, landingzoneDTO.getDescription());
        }
        if(!StringUtils.isBlank(landingzoneDTO.getLandingZone())){
            query.setParameter(++index, landingzoneDTO.getLandingZone());
        }
        if(landingzoneDTO.getDepartmentId() != null){
            query.setParameter(++index, landingzoneDTO.getDepartmentId());
        }
        if(!StringUtils.isBlank(landingzoneDTO.getDepartmentName())){
            query.setParameter(++index, landingzoneDTO.getDepartmentName());
        }
        if(!StringUtils.isBlank(landingzoneDTO.getCloud())){
            query.setParameter(++index, landingzoneDTO.getCloud());
        }
        if(!StringUtils.isBlank(landingzoneDTO.getDisplayName())){
            query.setParameter(++index, landingzoneDTO.getDisplayName());
        }
        if(!StringUtils.isBlank(landingzoneDTO.getRoleArn())){
            query.setParameter(++index, landingzoneDTO.getRoleArn());
        }
        if(!StringUtils.isBlank(landingzoneDTO.getExternalId())){
            query.setParameter(++index, landingzoneDTO.getExternalId());
        }
        if(!StringUtils.isBlank(landingzoneDTO.getStatus())){
            query.setParameter(++index, landingzoneDTO.getStatus());
        }
        if(!StringUtils.isBlank(landingzoneDTO.getCreatedBy())){
            query.setParameter(++index, landingzoneDTO.getCreatedBy());
        }
        if(!StringUtils.isBlank(landingzoneDTO.getUpdatedBy())){
            query.setParameter(++index, landingzoneDTO.getUpdatedBy());
        }
        if(landingzoneDTO.getOrganizationId() != null){
            query.setParameter(++index, landingzoneDTO.getOrganizationId());
        }
        if(!StringUtils.isBlank(landingzoneDTO.getOrganizationName())){
            query.setParameter(++index, landingzoneDTO.getOrganizationName());
        }
        return query.getResultList();
	}

    @Transactional(readOnly = true)
    public List<Landingzone> getLandingZone(String organization, String department, String cloud, String landingZone) {
        logger.debug("Get all landing-zones on given criteria");
        return landingzoneRepository.getLandingZone(organization, department, cloud, landingZone);
    }

    public List<Landingzone> getLandingZoneByOrgId(Long orgId, String cloud){
        logger.debug("Getting landing-zones by given org id : {} ", orgId);
        return landingzoneRepository.findByOrganizationIdAndCloud(orgId, cloud);
    }
}
