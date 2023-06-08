package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.OrganizationDTO;
import com.synectiks.asset.business.domain.Organization;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface OrganizationMapper {
    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = "yyyy-MM-dd hh:mm:ss")
    @Mapping(target = "updatedOn", dateFormat = "yyyy-MM-dd hh:mm:ss")
    OrganizationDTO toDto(Organization organization);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    Organization toEntity(OrganizationDTO organizationDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default OrganizationDTO entityToDto(Organization organization){
        OrganizationDTO dto = toDto(organization);
        dto.setId(organization.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Organization dtoToEntity(OrganizationDTO organizationDTO){
        Organization organization = toEntity(organizationDTO);
        organization.setId(organizationDTO.getId());
        return organization;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<OrganizationDTO> entityToDtoList(List<Organization> organizationList) {
        List<OrganizationDTO> organizationDTOList = organizationList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return organizationDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<Organization> dtoToEntityList(List<OrganizationDTO> organizationDTOList){
        List<Organization> organizationList = organizationDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return organizationList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Organization dtoToEntityForUpdate(OrganizationDTO organizationDTO, @MappingTarget Organization organization);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Organization dtoToEntityForSearch(OrganizationDTO organizationDTO){
        Organization organization = toEntity(organizationDTO);
        organization.setId(organizationDTO.getId());
        if(StringUtils.isBlank(organizationDTO.getCreatedOn())){
            organization.setCreatedOn(null);
        }
        if(StringUtils.isBlank(organizationDTO.getUpdatedOn())){
            organization.setUpdatedOn(null);
        }
        return organization;
    }
}
