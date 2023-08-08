package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.CloudElementDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.DbCategory;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.domain.ProductEnclave;
import com.synectiks.asset.service.CustomeHashMapConverter;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CloudElementMapper {
    CloudElementMapper INSTANCE = Mappers.getMapper(CloudElementMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "landingzoneId", source = "landingzone.id")
    @Mapping(target = "dbCategoryId", source = "dbCategory.id")
    @Mapping(target = "productEnclaveId", source = "productEnclave.id")
    CloudElementDTO toDto(CloudElement cloudElement);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudElement toEntity(CloudElementDTO cloudElementDTO){
        CloudElement cloudElement = toEntityConvertObjectToMap(cloudElementDTO);
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        cloudElement.setHostedServices(converter.convertObjectToMap(cloudElementDTO.getHostedServices()));
        cloudElement.setSlaJson(converter.convertObjectToMap(cloudElementDTO.getSlaJson()));
        cloudElement.setCostJson(converter.convertObjectToMap(cloudElementDTO.getCostJson()));
        cloudElement.setViewJson(converter.convertObjectToMap(cloudElementDTO.getViewJson()));
        cloudElement.setConfigJson(converter.convertObjectToMap(cloudElementDTO.getConfigJson()));
        cloudElement.setComplianceJson(converter.convertObjectToMap(cloudElementDTO.getComplianceJson()));
        return cloudElement;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target="hostedServices", ignore = true)
    @Mapping(target="slaJson", ignore = true)
    @Mapping(target="costJson", ignore = true)
    @Mapping(target="viewJson", ignore = true)
    @Mapping(target="configJson", ignore = true)
    @Mapping(target="complianceJson", ignore = true)
    @Mapping(target = "landingzone.id", source = "landingzoneId")
    @Mapping(target = "dbCategory.id", source = "dbCategoryId")
    @Mapping(target = "productEnclave.id", source = "productEnclaveId")
    CloudElement toEntityConvertObjectToMap(CloudElementDTO cloudElementDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudElementDTO entityToDto(CloudElement cloudElement){
        CloudElementDTO dto = toDto(cloudElement);
        dto.setId(cloudElement.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudElement dtoToEntity(CloudElementDTO cloudElementDTO){
        CloudElement cloudElement = toEntity(cloudElementDTO);
        cloudElement.setId(cloudElementDTO.getId());
        return cloudElement;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<CloudElementDTO> entityToDtoList(List<CloudElement> cloudElementList) {
        List<CloudElementDTO> cloudElementDTOList = cloudElementList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return cloudElementDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<CloudElement> dtoToEntityList(List<CloudElementDTO> cloudElementDTOList){
        List<CloudElement> cloudElementList = cloudElementDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return cloudElementList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default CloudElement dtoToEntityForUpdate(CloudElementDTO cloudElementDTO, CloudElement cloudElement){
        CloudElement temp = copyDtoToEntity(cloudElementDTO, cloudElement);
        if(cloudElementDTO.getLandingzoneId() != null){
            temp.setLandingzone(Landingzone.builder().id(cloudElementDTO.getLandingzoneId()).build());
        }
        if(cloudElementDTO.getDbCategoryId() != null){
            temp.setDbCategory(DbCategory.builder().id(cloudElementDTO.getDbCategoryId()).build());
        }
        if(cloudElementDTO.getProductEnclaveId() != null){
            temp.setProductEnclave(ProductEnclave.builder().id(cloudElementDTO.getProductEnclaveId()).build());
        }
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        temp.setHostedServices(converter.convertObjectToMap(cloudElementDTO.getHostedServices()));
        temp.setSlaJson(converter.convertObjectToMap(cloudElementDTO.getSlaJson()));
        temp.setCostJson(converter.convertObjectToMap(cloudElementDTO.getCostJson()));
        temp.setViewJson(converter.convertObjectToMap(cloudElementDTO.getViewJson()));
        temp.setConfigJson(converter.convertObjectToMap(cloudElementDTO.getConfigJson()));
        temp.setComplianceJson(converter.convertObjectToMap(cloudElementDTO.getComplianceJson()));
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target="hostedServices", ignore = true)
    @Mapping(target="slaJson", ignore = true)
    @Mapping(target="costJson", ignore = true)
    @Mapping(target="viewJson", ignore = true)
    @Mapping(target="configJson", ignore = true)
    @Mapping(target="complianceJson", ignore = true)
    CloudElement copyDtoToEntity(CloudElementDTO cloudElementDTO, @MappingTarget CloudElement cloudElement);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudElement dtoToEntityForSearch(CloudElementDTO cloudElementDTO){
        CloudElement cloudElement = toEntity(cloudElementDTO);
        cloudElement.setId(cloudElementDTO.getId());
        if(StringUtils.isBlank(cloudElementDTO.getCreatedOn())){
            cloudElement.setCreatedOn(null);
        }
        if(StringUtils.isBlank(cloudElementDTO.getUpdatedOn())){
            cloudElement.setUpdatedOn(null);
        }
        if(cloudElementDTO.getLandingzoneId() != null){
            cloudElement.setLandingzone(Landingzone.builder().id(cloudElementDTO.getLandingzoneId()).build());
        }
        if(cloudElementDTO.getDbCategoryId() != null){
            cloudElement.setDbCategory(DbCategory.builder().id(cloudElementDTO.getDbCategoryId()).build());
        }
        if(cloudElementDTO.getProductEnclaveId() != null){
            cloudElement.setProductEnclave(ProductEnclave.builder().id(cloudElementDTO.getProductEnclaveId()).build());
        }
        cloudElement.getLandingzone().setCreatedOn(null);
        cloudElement.getLandingzone().setUpdatedOn(null);
        return cloudElement;
    }
}
