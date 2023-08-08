package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.LandingzoneDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.domain.Department;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface LandingzoneMapper {
    LandingzoneMapper INSTANCE = Mappers.getMapper(LandingzoneMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "departmentId", source = "department.id")
    LandingzoneDTO toDto(Landingzone landingzone);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "department.id", source = "departmentId")
    Landingzone toEntity(LandingzoneDTO landingzoneDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default LandingzoneDTO entityToDto(Landingzone landingzone){
        LandingzoneDTO dto = toDto(landingzone);
        dto.setId(landingzone.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Landingzone dtoToEntity(LandingzoneDTO landingzoneDTO){
        Landingzone landingzone = toEntity(landingzoneDTO);
        landingzone.setId(landingzoneDTO.getId());
        return landingzone;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<LandingzoneDTO> entityToDtoList(List<Landingzone> landingzoneList) {
        List<LandingzoneDTO> landingzoneDTOList = landingzoneList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return landingzoneDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<Landingzone> dtoToEntityList(List<LandingzoneDTO> landingzoneDTOList){
        List<Landingzone> landingzoneList = landingzoneDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return landingzoneList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default Landingzone dtoToEntityForUpdate(LandingzoneDTO landingzoneDTO, Landingzone landingzone){
        Landingzone temp = copyDtoToEntity(landingzoneDTO, landingzone);
        if(landingzoneDTO.getDepartmentId() != null){
            temp.setDepartment(Department.builder().id(landingzoneDTO.getDepartmentId()).build());
        }
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Landingzone copyDtoToEntity(LandingzoneDTO landingzoneDTO, @MappingTarget Landingzone landingzone);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Landingzone dtoToEntityForSearch(LandingzoneDTO landingzoneDTO){
        Landingzone landingzone = toEntity(landingzoneDTO);
        landingzone.setId(landingzoneDTO.getId());
        if(StringUtils.isBlank(landingzoneDTO.getCreatedOn())){
            landingzone.setCreatedOn(null);
        }
        if(StringUtils.isBlank(landingzoneDTO.getUpdatedOn())){
            landingzone.setUpdatedOn(null);
        }
        if(landingzoneDTO.getDepartmentId() != null){
            landingzone.setDepartment(Department.builder().id(landingzoneDTO.getDepartmentId()).build());
        }
        landingzone.getDepartment().setCreatedOn(null);
        landingzone.getDepartment().setUpdatedOn(null);
        return landingzone;
    }
}
