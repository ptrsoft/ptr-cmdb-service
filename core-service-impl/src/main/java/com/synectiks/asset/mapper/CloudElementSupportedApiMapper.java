package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.CloudElementSupportedApiDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElementSupportedApi;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CloudElementSupportedApiMapper {
    CloudElementSupportedApiMapper INSTANCE = Mappers.getMapper(CloudElementSupportedApiMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    CloudElementSupportedApiDTO toDto(CloudElementSupportedApi cloudElementSupportedApi);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudElementSupportedApi toEntity(CloudElementSupportedApiDTO cloudElementSupportedApiDTO){
        CloudElementSupportedApi cloudElementSupportedApi = toEntityConvertObjectToMap(cloudElementSupportedApiDTO);
        return cloudElementSupportedApi;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    CloudElementSupportedApi toEntityConvertObjectToMap(CloudElementSupportedApiDTO cloudElementSupportedApiDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudElementSupportedApiDTO entityToDto(CloudElementSupportedApi cloudElementSupportedApi){
        CloudElementSupportedApiDTO dto = toDto(cloudElementSupportedApi);
        dto.setId(cloudElementSupportedApi.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudElementSupportedApi dtoToEntity(CloudElementSupportedApiDTO cloudElementSupportedApiDTO){
        CloudElementSupportedApi cloudElementSupportedApi = toEntity(cloudElementSupportedApiDTO);
        cloudElementSupportedApi.setId(cloudElementSupportedApiDTO.getId());
        return cloudElementSupportedApi;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<CloudElementSupportedApiDTO> entityToDtoList(List<CloudElementSupportedApi> cloudElementSupportedApiList) {
        List<CloudElementSupportedApiDTO> cloudElementSupportedApiDTOList = cloudElementSupportedApiList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return cloudElementSupportedApiDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<CloudElementSupportedApi> dtoToEntityList(List<CloudElementSupportedApiDTO> cloudElementSupportedApiDTOList){
        List<CloudElementSupportedApi> cloudElementSupportedApiList = cloudElementSupportedApiDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return cloudElementSupportedApiList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default CloudElementSupportedApi dtoToEntityForUpdate(CloudElementSupportedApiDTO cloudElementSupportedApiDTO, CloudElementSupportedApi cloudElementSupportedApi){
        CloudElementSupportedApi temp = copyDtoToEntity(cloudElementSupportedApiDTO, cloudElementSupportedApi);
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    CloudElementSupportedApi copyDtoToEntity(CloudElementSupportedApiDTO cloudElementSupportedApiDTO, @MappingTarget CloudElementSupportedApi cloudElementSupportedApi);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudElementSupportedApi dtoToEntityForSearch(CloudElementSupportedApiDTO cloudElementSupportedApiDTO){
        CloudElementSupportedApi cloudElementSupportedApi = toEntity(cloudElementSupportedApiDTO);
        cloudElementSupportedApi.setId(cloudElementSupportedApiDTO.getId());
        if(StringUtils.isBlank(cloudElementSupportedApiDTO.getCreatedOn())){
            cloudElementSupportedApi.setCreatedOn(null);
        }
        if(StringUtils.isBlank(cloudElementSupportedApiDTO.getUpdatedOn())){
            cloudElementSupportedApi.setUpdatedOn(null);
        }
        return cloudElementSupportedApi;
    }
}
