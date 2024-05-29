package com.synectiks.asset.service;

import com.synectiks.asset.api.model.CloudDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Cloud;
import com.synectiks.asset.repository.CloudRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Service
public class CloudService {

	private final Logger logger = LoggerFactory.getLogger(CloudService.class);

	@Autowired
	private CloudRepository cloudRepository;

	@Autowired
	private EntityManager entityManager;

	public Cloud save(Cloud cloud) {
		logger.debug("Request to save cloud : {}", cloud);
		cloud.setElementType(cloud.getElementType().toUpperCase());
		cloud.setName(cloud.getName().toUpperCase());
		return cloudRepository.save(cloud);
	}

	@Transactional(readOnly = true)
	public List<Cloud> findAll() {
		logger.debug("Request to get all clouds");
		return cloudRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Cloud> findOne(Long id) {
		logger.debug("Request to get cloud : {}", id);
		return cloudRepository.findById(id);
	}

	public void delete(Long id) {
		logger.debug("Request to delete cloud : {}", id);
		cloudRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Cloud> search(CloudDTO cloudDTO) {
		logger.info("Search cloud");
		StringBuilder primarySql = new StringBuilder("select c.* from cloud c where 1 = 1 ");
		if(!StringUtils.isBlank(cloudDTO.getProductCategory()) ||
				!StringUtils.isBlank(cloudDTO.getServiceCategory())){
			primarySql = new StringBuilder("select c.*  from cloud c, jsonb_array_elements(c.ui_mapping  -> 'key') with ordinality bg(obj) where 1 = 1 ");
		}
		if(cloudDTO.getId() != null){
			primarySql.append(" and c.id = ? ");
		}
		if(cloudDTO.getElementType() != null){
			primarySql.append(" and upper(c.element_type) = upper(?) ");
		}
		if(cloudDTO.getName() != null){
			primarySql.append(" and upper(c.name) = upper(?) ");
		}
		if(!StringUtils.isBlank(cloudDTO.getStatus())){
			primarySql.append(" and upper(c.status) = upper(?) ");
		}
		if(!StringUtils.isBlank(cloudDTO.getCreatedBy())){
			primarySql.append(" and upper(c.created_by) = upper(?) ");
		}
		if(!StringUtils.isBlank(cloudDTO.getUpdatedBy())){
			primarySql.append(" and upper(c.updated_by) = upper(?) ");
		}
		if(!StringUtils.isBlank(cloudDTO.getProductCategory())){
			primarySql.append(" and upper(bg.obj -> 'productCategory' ->> 'name')  = upper(?) ");
		}
		if(!StringUtils.isBlank(cloudDTO.getServiceCategory())){
			if(Constants.APP_SERVICES.equalsIgnoreCase(cloudDTO.getServiceCategory())){
				primarySql.append(" and bg.obj -> 'productCategory' -> 'serviceCategory' -> 'name'  @> '[\"app\"]' " );
			}else if(Constants.DATA_SERVICES.equalsIgnoreCase(cloudDTO.getServiceCategory())){
				primarySql.append(" and bg.obj -> 'productCategory' -> 'serviceCategory' -> 'name'  @> '[\"data\"]' " );
			}else if(Constants.WEB_SERVICES.equalsIgnoreCase(cloudDTO.getServiceCategory())){
				primarySql.append(" and bg.obj -> 'productCategory' -> 'serviceCategory' -> 'name'  @> '[\"web\"]' " );
			}else if(Constants.AUX_SERVICES.equalsIgnoreCase(cloudDTO.getServiceCategory())){
				primarySql.append(" and bg.obj -> 'productCategory' -> 'serviceCategory' -> 'name'  @> '[\"aux\"]' " );
			}

		}
		Query query = entityManager.createNativeQuery(primarySql.toString(), Cloud.class);
		int index = 0;
		if(cloudDTO.getId() != null){
			query.setParameter(++index, cloudDTO.getId());
		}
		if(cloudDTO.getElementType() != null){
			query.setParameter(++index, cloudDTO.getElementType());
		}
		if(cloudDTO.getName() != null){
			query.setParameter(++index, cloudDTO.getName());
		}
		if(!StringUtils.isBlank(cloudDTO.getStatus())){
			query.setParameter(++index, cloudDTO.getStatus());
		}
		if(!StringUtils.isBlank(cloudDTO.getCreatedBy())){
			query.setParameter(++index, cloudDTO.getCreatedBy());
		}
		if(!StringUtils.isBlank(cloudDTO.getUpdatedBy())){
			query.setParameter(++index, cloudDTO.getUpdatedBy());
		}
		if(!StringUtils.isBlank(cloudDTO.getProductCategory())){
			query.setParameter(++index, cloudDTO.getProductCategory());
		}

		return query.getResultList();
	}

	public List<Cloud> findByElementType(String elementType){
		return cloudRepository.findByElementType(elementType);
	}

	public List<Cloud> findByName(String name){
		return cloudRepository.findByName(name);
	}

	public List<Cloud> findByStatus(String status){
		return cloudRepository.findByStatus(status);
	}

	public List<Cloud> findByNameAndStatus(String name, String status){
		return cloudRepository.findByNameAndStatus(name, status);
	}
	public List<Cloud> findByNameAndIsCronScheduled(String name, boolean isCronScheduled){
		return cloudRepository.findByNameAndIsCronScheduled(name, isCronScheduled);
	}
	public List<Cloud> findByNameAndIsCronScheduledAndStatus(String name, boolean isCronScheduled, String status){
		return cloudRepository.findByNameAndIsCronScheduledAndStatus(name, isCronScheduled, status);
	}
	public Cloud findByNameAndElementType(String name, String elementType){
		return cloudRepository.findByNameAndElementType(name, elementType);
	}
	public Cloud findByNameAndElementTypeAndIsCronScheduled(String name, String elementType, boolean isCronScheduled){
		return cloudRepository.findByNameAndElementTypeAndIsCronScheduled(name, elementType, isCronScheduled);
	}
	public List<Cloud> findByElementTypeAndNameAndStatus(String elementType, String name, String status){
		return cloudRepository.findByElementTypeAndNameAndStatus(elementType, name, status);
	}
	public Cloud findByNameAndListQuery(String name, String listQuery){
		return cloudRepository.findByNameAndListQuery(name, listQuery);
	}
}
