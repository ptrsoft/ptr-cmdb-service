package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.CloudEnvironmentDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudEnvironment;
import com.synectiks.asset.domain.Department;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CloudEnvironmentMapper {
    CloudEnvironmentMapper INSTANCE = Mappers.getMapper(CloudEnvironmentMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "departmentId", source = "department.id")
    CloudEnvironmentDTO toDto(CloudEnvironment cloudEnvironment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "department.id", source = "departmentId")
    CloudEnvironment toEntity(CloudEnvironmentDTO cloudEnvironmentDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudEnvironmentDTO entityToDto(CloudEnvironment cloudEnvironment){
        CloudEnvironmentDTO dto = toDto(cloudEnvironment);
        dto.setId(cloudEnvironment.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudEnvironment dtoToEntity(CloudEnvironmentDTO cloudEnvironmentDTO){
        CloudEnvironment cloudEnvironment = toEntity(cloudEnvironmentDTO);
        cloudEnvironment.setId(cloudEnvironmentDTO.getId());
        return cloudEnvironment;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<CloudEnvironmentDTO> entityToDtoList(List<CloudEnvironment> cloudEnvironmentList) {
        List<CloudEnvironmentDTO> cloudEnvironmentDTOList = cloudEnvironmentList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return cloudEnvironmentDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<CloudEnvironment> dtoToEntityList(List<CloudEnvironmentDTO> cloudEnvironmentDTOList){
        List<CloudEnvironment> cloudEnvironmentList = cloudEnvironmentDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return cloudEnvironmentList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default CloudEnvironment dtoToEntityForUpdate(CloudEnvironmentDTO cloudEnvironmentDTO, CloudEnvironment cloudEnvironment){
        CloudEnvironment temp = copyDtoToEntity(cloudEnvironmentDTO, cloudEnvironment);
        if(cloudEnvironmentDTO.getDepartmentId() != null){
            temp.setDepartment(Department.builder().id(cloudEnvironmentDTO.getDepartmentId()).build());
        }
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    CloudEnvironment copyDtoToEntity(CloudEnvironmentDTO cloudEnvironmentDTO, @MappingTarget CloudEnvironment cloudEnvironment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudEnvironment dtoToEntityForSearch(CloudEnvironmentDTO cloudEnvironmentDTO){
        CloudEnvironment cloudEnvironment = toEntity(cloudEnvironmentDTO);
        cloudEnvironment.setId(cloudEnvironmentDTO.getId());
        if(StringUtils.isBlank(cloudEnvironmentDTO.getCreatedOn())){
            cloudEnvironment.setCreatedOn(null);
        }
        if(StringUtils.isBlank(cloudEnvironmentDTO.getUpdatedOn())){
            cloudEnvironment.setUpdatedOn(null);
        }
        if(cloudEnvironmentDTO.getDepartmentId() != null){
            cloudEnvironment.setDepartment(Department.builder().id(cloudEnvironmentDTO.getDepartmentId()).build());
        }
        cloudEnvironment.getDepartment().setCreatedOn(null);
        cloudEnvironment.getDepartment().setUpdatedOn(null);
        return cloudEnvironment;
    }
}
