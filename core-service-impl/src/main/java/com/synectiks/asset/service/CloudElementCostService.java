package com.synectiks.asset.service;

import com.synectiks.asset.domain.CloudElementCost;
import com.synectiks.asset.repository.CloudElementCostRepository;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CloudElementCost}.
 */
@Service
@Transactional
public class CloudElementCostService {

    private final Logger logger = LoggerFactory.getLogger(CloudElementCostService.class);
    @Autowired
    private CloudElementCostRepository cloudElementCostRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;

    public CloudElementCost save(CloudElementCost CloudElementCost) {
        logger.debug("Request to save cloud element cost : {}", CloudElementCost);
        return cloudElementCostRepository.save(CloudElementCost);
    }

    @Transactional(readOnly = true)
    public List<CloudElementCost> findAll() {
        logger.debug("Request to get all cloud element cost");
        return cloudElementCostRepository.findAll(Sort.by(Direction.DESC, "id"));
    }

    @Transactional(readOnly = true)
    public Optional<CloudElementCost> findOne(Long id) {
        logger.debug("Request to get cloud element cost : {}", id);
        return cloudElementCostRepository.findById(id);
    }

    public void delete(Long id) {
        logger.debug("Request to delete cloud element cost : {}", id);
        cloudElementCostRepository.deleteById(id);
    }
    
//    @Transactional(readOnly = true)
//	public List<CloudElementCost> search(CloudElementDTO cloudElementDTO) {
//        logger.info("Search cloud element cost");
//        StringBuilder primarySql = new StringBuilder("select ce.*, l.cloud  \n" +
//                " from cloud_element_cost cec \n" +
//                " inner join cloud_element ce on ce.id = cec.cloud_element_id\n" +
//                " where 1 = 1 ");
//        if(cloudElementDTO.getId() != null){
//            primarySql.append(" and ce.id = ? ");
//        }
//        if(cloudElementDTO.getLandingzoneId() != null){
//            primarySql.append(" and ce.landingzone_id = ? ");
//        }
//        if(!StringUtils.isBlank(cloudElementDTO.getStatus())){
//            primarySql.append(" and upper(ce.status) = upper(?) ");
//        }
//        if(!StringUtils.isBlank(cloudElementDTO.getCreatedBy())){
//            primarySql.append(" and upper(ce.created_by) = upper(?) ");
//        }
//        if(!StringUtils.isBlank(cloudElementDTO.getUpdatedBy())){
//            primarySql.append(" and upper(ce.updated_by) = upper(?) ");
//        }
//
//        Query query = entityManager.createNativeQuery(primarySql.toString(),CloudElementCost.class);
//        int index = 0;
//        if(cloudElementDTO.getId() != null){
//            query.setParameter(++index, cloudElementDTO.getId());
//        }
//        if(cloudElementDTO.getLandingzoneId() != null){
//            query.setParameter(++index, cloudElementDTO.getLandingzoneId());
//        }
//        if(!StringUtils.isBlank(cloudElementDTO.getStatus())){
//            query.setParameter(++index, cloudElementDTO.getStatus());
//        }
//        if(!StringUtils.isBlank(cloudElementDTO.getCreatedBy())){
//            query.setParameter(++index, cloudElementDTO.getCreatedBy());
//        }
//        if(!StringUtils.isBlank(cloudElementDTO.getUpdatedBy())){
//            query.setParameter(++index, cloudElementDTO.getUpdatedBy());
//        }
//        List<CloudElementCost> list = query.getResultList();
//        return list;
//	}

    @Transactional(readOnly = true)
    public CloudElementCost findByCloudElementId(Long cloudElementId){
        return cloudElementCostRepository.findByCloudElementId(cloudElementId);
    }

}
