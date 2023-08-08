package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.BusinessElementDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.*;
import com.synectiks.asset.service.CustomeHashMapConverter;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface BusinessElementMapper {
    BusinessElementMapper INSTANCE = Mappers.getMapper(BusinessElementMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "cloudElementId", source = "cloudElement.id")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "moduleId", source = "module.id")
    @Mapping(target = "productEnvId", source = "productEnv.id")
    BusinessElementDTO toDto(BusinessElement businessElement);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default BusinessElement toEntity(BusinessElementDTO businessElementDTO){
        BusinessElement businessElement = toEntityConvertObjectToMap(businessElementDTO);
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        businessElement.setMetadata(converter.convertObjectToMap(businessElementDTO.getMetadata()));
        businessElement.setSlaJson(converter.convertObjectToMap(businessElementDTO.getSlaJson()));
        businessElement.setCostJson(converter.convertObjectToMap(businessElementDTO.getCostJson()));
        businessElement.setViewJson(converter.convertObjectToMap(businessElementDTO.getViewJson()));
        businessElement.setConfigJson(converter.convertObjectToMap(businessElementDTO.getConfigJson()));
        businessElement.setComplianceJson(converter.convertObjectToMap(businessElementDTO.getComplianceJson()));
        return businessElement;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target="metadata", ignore = true)
    @Mapping(target="slaJson", ignore = true)
    @Mapping(target="costJson", ignore = true)
    @Mapping(target="viewJson", ignore = true)
    @Mapping(target="configJson", ignore = true)
    @Mapping(target="complianceJson", ignore = true)
    @Mapping(target = "cloudElement.id", source = "cloudElementId")
    @Mapping(target = "department.id", source = "departmentId")
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "module.id", source = "moduleId")
    @Mapping(target = "productEnv.id", source = "productEnvId")
    BusinessElement toEntityConvertObjectToMap(BusinessElementDTO businessElementDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default BusinessElementDTO entityToDto(BusinessElement businessElement){
        BusinessElementDTO dto = toDto(businessElement);
        dto.setId(businessElement.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default BusinessElement dtoToEntity(BusinessElementDTO businessElementDTO){
        BusinessElement businessElement = toEntity(businessElementDTO);
        businessElement.setId(businessElementDTO.getId());
        return businessElement;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<BusinessElementDTO> entityToDtoList(List<BusinessElement> businessElementList) {
        List<BusinessElementDTO> businessElementDTOList = businessElementList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return businessElementDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<BusinessElement> dtoToEntityList(List<BusinessElementDTO> businessElementDTOList){
        List<BusinessElement> businessElementList = businessElementDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return businessElementList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default BusinessElement dtoToEntityForUpdate(BusinessElementDTO businessElementDTO, BusinessElement businessElement){
        BusinessElement temp = copyDtoToEntity(businessElementDTO, businessElement);

        if(businessElementDTO.getCloudElementId() != null){
            temp.setCloudElement(CloudElement.builder().id(businessElementDTO.getCloudElementId()).build());
        }
        if(businessElementDTO.getDepartmentId() != null){
            temp.setDepartment(Department.builder().id(businessElementDTO.getDepartmentId()).build());
        }
        if(businessElementDTO.getProductId() != null){
            temp.setProduct(Product.builder().id(businessElementDTO.getProductId()).build());
        }
        if(businessElementDTO.getModuleId() != null){
            temp.setModule(Module.builder().id(businessElementDTO.getModuleId()).build());
        }
        if(businessElementDTO.getProductEnvId() != null){
            temp.setProductEnv(ProductEnv.builder().id(businessElementDTO.getProductEnvId()).build());
        }
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        temp.setMetadata(converter.convertObjectToMap(businessElementDTO.getMetadata()));
        temp.setSlaJson(converter.convertObjectToMap(businessElementDTO.getSlaJson()));
        temp.setCostJson(converter.convertObjectToMap(businessElementDTO.getCostJson()));
        temp.setViewJson(converter.convertObjectToMap(businessElementDTO.getViewJson()));
        temp.setConfigJson(converter.convertObjectToMap(businessElementDTO.getConfigJson()));
        temp.setComplianceJson(converter.convertObjectToMap(businessElementDTO.getComplianceJson()));

        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target="metadata", ignore = true)
    @Mapping(target="slaJson", ignore = true)
    @Mapping(target="costJson", ignore = true)
    @Mapping(target="viewJson", ignore = true)
    @Mapping(target="configJson", ignore = true)
    @Mapping(target="complianceJson", ignore = true)
    BusinessElement copyDtoToEntity(BusinessElementDTO businessElementDTO, @MappingTarget BusinessElement businessElement);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default BusinessElement dtoToEntityForSearch(BusinessElementDTO businessElementDTO){
        BusinessElement businessElement = toEntity(businessElementDTO);
        businessElement.setId(businessElementDTO.getId());
        if(StringUtils.isBlank(businessElementDTO.getCreatedOn())){
            businessElement.setCreatedOn(null);
        }
        if(StringUtils.isBlank(businessElementDTO.getUpdatedOn())){
            businessElement.setUpdatedOn(null);
        }
        if(businessElementDTO.getCloudElementId() != null){
            businessElement.setCloudElement(CloudElement.builder().id(businessElementDTO.getCloudElementId()).build());
        }
        if(businessElementDTO.getDepartmentId() != null){
            businessElement.setDepartment(Department.builder().id(businessElementDTO.getDepartmentId()).build());
        }
        if(businessElementDTO.getProductId() != null){
            businessElement.setProduct(Product.builder().id(businessElementDTO.getProductId()).build());
        }
        if(businessElementDTO.getModuleId() != null){
            businessElement.setModule(Module.builder().id(businessElementDTO.getModuleId()).build());
        }
        if(businessElementDTO.getProductEnvId() != null){
            businessElement.setProductEnv(ProductEnv.builder().id(businessElementDTO.getProductEnvId()).build());
        }

        return businessElement;
    }
}
