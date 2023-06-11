package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.DeploymentEnvironmentDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.DeploymentEnvironment;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface DeploymentEnvironmentMapper {
    DeploymentEnvironmentMapper INSTANCE = Mappers.getMapper(DeploymentEnvironmentMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    DeploymentEnvironmentDTO toDto(DeploymentEnvironment deploymentEnvironment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    DeploymentEnvironment toEntity(DeploymentEnvironmentDTO deploymentEnvironmentDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default DeploymentEnvironmentDTO entityToDto(DeploymentEnvironment deploymentEnvironment){
        DeploymentEnvironmentDTO dto = toDto(deploymentEnvironment);
        dto.setId(deploymentEnvironment.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default DeploymentEnvironment dtoToEntity(DeploymentEnvironmentDTO deploymentEnvironmentDTO){
        DeploymentEnvironment deploymentEnvironment = toEntity(deploymentEnvironmentDTO);
        deploymentEnvironment.setId(deploymentEnvironmentDTO.getId());
        return deploymentEnvironment;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<DeploymentEnvironmentDTO> entityToDtoList(List<DeploymentEnvironment> deploymentEnvironmentList) {
        List<DeploymentEnvironmentDTO> deploymentEnvironmentDTOList = deploymentEnvironmentList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return deploymentEnvironmentDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<DeploymentEnvironment> dtoToEntityList(List<DeploymentEnvironmentDTO> deploymentEnvironmentDTOList){
        List<DeploymentEnvironment> deploymentEnvironmentList = deploymentEnvironmentDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return deploymentEnvironmentList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    DeploymentEnvironment dtoToEntityForUpdate(DeploymentEnvironmentDTO deploymentEnvironmentDTO, @MappingTarget DeploymentEnvironment deploymentEnvironment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default DeploymentEnvironment dtoToEntityForSearch(DeploymentEnvironmentDTO deploymentEnvironmentDTO){
        DeploymentEnvironment deploymentEnvironment = toEntity(deploymentEnvironmentDTO);
        deploymentEnvironment.setId(deploymentEnvironmentDTO.getId());
        if(StringUtils.isBlank(deploymentEnvironmentDTO.getCreatedOn())){
            deploymentEnvironment.setCreatedOn(null);
        }
        if(StringUtils.isBlank(deploymentEnvironmentDTO.getUpdatedOn())){
            deploymentEnvironment.setUpdatedOn(null);
        }
        return deploymentEnvironment;
    }
}
