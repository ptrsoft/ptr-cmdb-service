package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.DepartmentDTO;
import com.synectiks.asset.api.model.OrganizationDTO;
import com.synectiks.asset.business.domain.Department;
import com.synectiks.asset.business.domain.Organization;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface DepartmentMapper {
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    DepartmentDTO toDto(Department department);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    Department toEntity(DepartmentDTO departmentDTO);



    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default DepartmentDTO entityToDto(Department department){
        DepartmentDTO dto = toDto(department);
        dto.setId(department.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Department dtoToEntity(DepartmentDTO departmentDTO){
        Department department = toEntity(departmentDTO);
        department.setId(departmentDTO.getId());
        return department;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<DepartmentDTO> entityToDtoList(List<Department> departmentList) {
        List<DepartmentDTO> departmentDTOList = departmentList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return departmentDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<Department> dtoToEntityList(List<DepartmentDTO> departmentDTOList){
        List<Department> departmentList = departmentDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return departmentList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Department dtoToEntityForUpdate(DepartmentDTO departmentDTO, @MappingTarget Department department);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Department dtoToEntityForSearch(DepartmentDTO departmentDTO){
        Department department = toEntity(departmentDTO);
        department.setId(departmentDTO.getId());
//        if(StringUtils.isBlank(departmentDTO.getCreatedOn())){
//            department.setCreatedOn(null);
//        }
//        if(StringUtils.isBlank(departmentDTO.getUpdatedOn())){
//            department.setUpdatedOn(null);
//        }
        return department;
    }
}
