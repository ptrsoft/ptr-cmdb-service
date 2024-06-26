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
    @Mapping(target = "landingZone", source = "landingzone.landingZone")
    @Mapping(target = "dbCategoryName", source = "dbCategory.name")
    @Mapping(target = "productEnclaveInstanceId", source = "productEnclave.instanceId")
    CloudElementDTO toDto(CloudElement cloudElement);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudElement toEntity(CloudElementDTO cloudElementDTO){
        CloudElement cloudElement = toEntityConvertObjectToMap(cloudElementDTO);
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        cloudElement.setHostedServices(converter.convertObjectToMap(cloudElementDTO.getHostedServices()));
        cloudElement.setViewJson(converter.convertObjectToMap(cloudElementDTO.getViewJson()));
        cloudElement.setConfigJson(converter.convertObjectToMap(cloudElementDTO.getConfigJson()));
        return cloudElement;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target="hostedServices", ignore = true)
    @Mapping(target="viewJson", ignore = true)
    @Mapping(target="configJson", ignore = true)
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
        temp.setViewJson(converter.convertObjectToMap(cloudElementDTO.getViewJson()));
        temp.setConfigJson(converter.convertObjectToMap(cloudElementDTO.getConfigJson()));
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target="hostedServices", ignore = true)
    @Mapping(target="viewJson", ignore = true)
    @Mapping(target="configJson", ignore = true)
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

        if(cloudElement.getLandingzone() != null){
            cloudElement.getLandingzone().setCreatedOn(null);
            cloudElement.getLandingzone().setUpdatedOn(null);
        }

        if(cloudElement.getDbCategory() != null){
            cloudElement.getDbCategory().setCreatedOn(null);
            cloudElement.getDbCategory().setUpdatedOn(null);
        }

        if(cloudElement.getProductEnclave() != null){
            cloudElement.getProductEnclave().setCreatedOn(null);
            cloudElement.getProductEnclave().setUpdatedOn(null);
        }

        return cloudElement;
    }


}
