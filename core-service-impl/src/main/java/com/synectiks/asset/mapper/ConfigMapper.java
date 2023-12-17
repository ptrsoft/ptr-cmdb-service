package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.ConfigDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Config;
import com.synectiks.asset.domain.Organization;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ConfigMapper {
    ConfigMapper INSTANCE = Mappers.getMapper(ConfigMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "organizationId", source = "organization.id")
    @Mapping(target = "organizationName", source = "organization.name")
    ConfigDTO toDto(Config config);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Config toEntity(ConfigDTO configDTO){
        Config config = toEntityConvertObjectToMap(configDTO);
        return config;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "organization.id", source = "organizationId", ignore = true)
    Config toEntityConvertObjectToMap(ConfigDTO configDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default ConfigDTO entityToDto(Config config){
        ConfigDTO dto = toDto(config);
        dto.setId(config.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Config dtoToEntity(ConfigDTO configDTO){
        Config config = toEntity(configDTO);
        if(configDTO.getOrganizationId() == null){
            config.setOrganization(null);
        }
        config.setId(configDTO.getId());
        return config;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<ConfigDTO> entityToDtoList(List<Config> configList) {
        List<ConfigDTO> configDTOList = configList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return configDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<Config> dtoToEntityList(List<ConfigDTO> configDTOList){
        List<Config> configList = configDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return configList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default Config dtoToEntityForUpdate(ConfigDTO configDTO, Config config){
        Config temp = copyDtoToEntity(configDTO, config);

        if(configDTO.getOrganizationId() != null){
            temp.setOrganization(Organization.builder().id(configDTO.getOrganizationId()).build());
        }
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Config copyDtoToEntity(ConfigDTO configDTO, @MappingTarget Config config);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Config dtoToEntityForSearch(ConfigDTO configDTO){
        Config config = toEntity(configDTO);
        config.setId(configDTO.getId());
        if(StringUtils.isBlank(configDTO.getCreatedOn())){
            config.setCreatedOn(null);
        }
        if(StringUtils.isBlank(configDTO.getUpdatedOn())){
            config.setUpdatedOn(null);
        }
        if(configDTO.getOrganizationId() != null){
            Organization org = Organization.builder().id(configDTO.getOrganizationId()).build();
            org.setCreatedOn(null);
            org.setUpdatedOn(null);
            config.setOrganization(org);
        }else{
            config.setOrganization(null);
        }

        return config;
    }
}
