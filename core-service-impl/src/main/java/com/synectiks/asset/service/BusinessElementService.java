package com.synectiks.asset.service;

import com.synectiks.asset.api.model.BusinessElementDTO;
import com.synectiks.asset.domain.BusinessElement;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.repository.BusinessElementRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link BusinessElement}.
 */
@Service
@Transactional
public class BusinessElementService {

    private final Logger logger = LoggerFactory.getLogger(BusinessElementService.class);

    @Autowired
    private BusinessElementRepository businessElementRepository;

    @Autowired
    private EntityManager entityManager;

	public BusinessElement save(BusinessElement businessElement) {
        logger.debug("Request to save business element: {}", businessElement);
        return businessElementRepository.save(businessElement);
    }

    @Transactional(readOnly = true)
    public List<BusinessElement> findAll() {
        logger.debug("Request to get all businessElement");
        return businessElementRepository.findAll(Sort.by(Direction.DESC, "id"));
    }

    @Transactional(readOnly = true)
    public Optional<BusinessElement> findOne(Long id) {
        logger.debug("Request to get business element: {}", id);
        return businessElementRepository.findById(id);
    }

    public void delete(Long id) {
        logger.debug("Request to delete business element : {}", id);
        businessElementRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
	public List<BusinessElement> search(BusinessElementDTO businessElementDTO) {
		logger.info("Search business element");
        StringBuilder primarySql = new StringBuilder("select be.* from business_element be \n" +
                " left join cloud_element ce on ce.id = be.cloud_element_id \n" +
                " left join department d on d.id = be.department_id \n" +
                " left join product p on p.id = be.product_id \n" +
                " left join \"module\" m on m.id = be.module_id \n" +
                " left join product_env pe on pe.id = be.product_env_id " +
                " where 1 = 1 ");

        if(businessElementDTO.getId() != null){
            primarySql.append(" and be.id = ? ");
        }
        if(!StringUtils.isBlank(businessElementDTO.getServiceName())){
            primarySql.append(" and upper(be.service_name) = upper(?) ");
        }
        if(!StringUtils.isBlank(businessElementDTO.getServiceNature())){
            primarySql.append(" and upper(be.service_nature) = upper(?) ");
        }
        if(!StringUtils.isBlank(businessElementDTO.getServiceType())){
            primarySql.append(" and upper(be.service_type) = upper(?) ");
        }
        if(!StringUtils.isBlank(businessElementDTO.getStatus())){
            primarySql.append(" and upper(be.status) = upper(?) ");
        }
        if(!StringUtils.isBlank(businessElementDTO.getCreatedBy())){
            primarySql.append(" and upper(be.created_by) = upper(?) ");
        }
        if(!StringUtils.isBlank(businessElementDTO.getUpdatedBy())){
            primarySql.append(" and upper(be.updated_by) = upper(?) ");
        }
        if(businessElementDTO.getCloudElementId() != null){
            primarySql.append(" and be.cloud_element_id = ? ");
        }
        if(businessElementDTO.getDepartmentId() != null){
            primarySql.append(" and be.department_id = ? ");
        }
        if(businessElementDTO.getProductId() != null){
            primarySql.append(" and be.product_id = ? ");
        }
        if(businessElementDTO.getModuleId() != null){
            primarySql.append(" and be.module_id = ? ");
        }
        if(businessElementDTO.getProductEnvId() != null){
            primarySql.append(" and be.product_env_id = ? ");
        }

        Query query = entityManager.createNativeQuery(primarySql.toString(), BusinessElement.class);
        int index = 0;
        if(businessElementDTO.getId() != null){
            query.setParameter(++index, businessElementDTO.getId());
        }
        if(!StringUtils.isBlank(businessElementDTO.getServiceName())){
            query.setParameter(++index, businessElementDTO.getServiceName());
        }
        if(!StringUtils.isBlank(businessElementDTO.getServiceNature())){
            query.setParameter(++index, businessElementDTO.getServiceNature());
        }
        if(!StringUtils.isBlank(businessElementDTO.getServiceType())){
            query.setParameter(++index, businessElementDTO.getServiceType());
        }
        if(!StringUtils.isBlank(businessElementDTO.getStatus())){
            query.setParameter(++index, businessElementDTO.getStatus());
        }
        if(!StringUtils.isBlank(businessElementDTO.getCreatedBy())){
            query.setParameter(++index, businessElementDTO.getCreatedBy());
        }
        if(!StringUtils.isBlank(businessElementDTO.getUpdatedBy())){
            query.setParameter(++index, businessElementDTO.getUpdatedBy());
        }
        if(businessElementDTO.getCloudElementId() != null){
            query.setParameter(++index, businessElementDTO.getCloudElementId());
        }
        if(businessElementDTO.getDepartmentId() != null){
            query.setParameter(++index, businessElementDTO.getDepartmentId());
        }
        if(businessElementDTO.getProductId() != null){
            query.setParameter(++index, businessElementDTO.getProductId());
        }
        if(businessElementDTO.getModuleId() != null){
            query.setParameter(++index, businessElementDTO.getModuleId());
        }
        if(businessElementDTO.getProductEnvId() != null){
            query.setParameter(++index, businessElementDTO.getProductEnvId());
        }
        List<BusinessElement> list = query.getResultList();
        return list;
	}

    @Transactional(readOnly = true)
    public List<BusinessElement> searchByFilter(Long departmentId, Long productId, Long productEnvId, Long moduleId, String serviceNature, String serviceType) {
        if(moduleId != null && !StringUtils.isBlank(serviceNature)){
            logger.debug("Get all business elements on the filters provided. Department Id: {}, Product Id: {}, Product Env Id: {}, Module Id: {}, Service Nature: {}", departmentId, productId, productEnvId, moduleId, serviceNature);
            return businessElementRepository.searchByFiltersForSoaAssociation(departmentId, productId, productEnvId, moduleId, serviceNature);
        }
        logger.debug("Get all business elements on the filters provided. Department Id: {}, Product Id: {}, Product Env Id: {}, Service Type: {}", departmentId, productId, productEnvId, serviceType);
        return businessElementRepository.searchByFiltersFor3TierAssociation(departmentId, productId, productEnvId, serviceType);
    }

    public BusinessElement getSoaService(String serviceName, String serviceNature, Long productId, Long productEnvId, Long moduleId){
        logger.debug("Get business-element by service name: {}, service nature: {}, product id: {}, environment id: {}, module id: {} ",serviceName, serviceNature, productId, productEnvId, moduleId);
        return businessElementRepository.getSoaService(serviceName, serviceNature, productId, productEnvId, moduleId);
    }
    public BusinessElement getThreeTierService(String serviceName, String serviceType,  Long productId, Long productEnvId){
        logger.debug("Get business-element by service name: {}, service type: {}, product id: {}, environment id: {} ",serviceName, serviceType,  productId, productEnvId);
        return businessElementRepository.getThreeTierService(serviceName, serviceType,  productId, productEnvId);
    }

    @Transactional
    public BusinessElement updateService(Long serviceId, CloudElement cloudElement){
        Optional<BusinessElement> optionalBusinessElement = findOne(serviceId);
        BusinessElement businessElement = null;
        if(optionalBusinessElement.isPresent()){
            businessElement = optionalBusinessElement.get();
            businessElement.setCloudElement(cloudElement);
            businessElement = save(businessElement);
        }
        return businessElement;
    }

    public List<BusinessElement> getServiceViewTopology(Long landingZoneId, String productName, String deptName, String env, String productType, String serviceNature) {
        logger.debug("Get service view topology: ");
        return businessElementRepository.getServiceViewTopology(landingZoneId, productName,deptName, env, productType, serviceNature);
    }
}
