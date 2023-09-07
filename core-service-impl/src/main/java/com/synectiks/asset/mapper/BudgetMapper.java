package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.BudgetDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.*;
import com.synectiks.asset.service.CustomeHashMapConverter;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface BudgetMapper {
    BudgetMapper INSTANCE = Mappers.getMapper(BudgetMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "organizationId", source = "organization.id")
    @Mapping(target = "organizationName", source = "organization.name")
    BudgetDTO toDto(Budget budget);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Budget toEntity(BudgetDTO budgetDTO){
        Budget budget = toEntityConvertObjectToMap(budgetDTO);
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        budget.setBudgetJson(converter.convertObjectToMap(budgetDTO.getBudgetJson()));
        return budget;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target="budgetJson", ignore = true)
    @Mapping(target = "organization.id", source = "organizationId")
    Budget toEntityConvertObjectToMap(BudgetDTO budgetDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default BudgetDTO entityToDto(Budget budget){
        BudgetDTO dto = toDto(budget);
        dto.setId(budget.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Budget dtoToEntity(BudgetDTO budgetDTO){
        Budget budget = toEntity(budgetDTO);
        budget.setId(budgetDTO.getId());
        return budget;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<BudgetDTO> entityToDtoList(List<Budget> budgetList) {
        List<BudgetDTO> budgetDTOList = budgetList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return budgetDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<Budget> dtoToEntityList(List<BudgetDTO> budgetDTOList){
        List<Budget> budgetList = budgetDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return budgetList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default Budget dtoToEntityForUpdate(BudgetDTO budgetDTO, Budget budget){
        Budget temp = copyDtoToEntity(budgetDTO, budget);

        if(budgetDTO.getOrganizationId() != null){
            temp.setOrganization(Organization.builder().id(budgetDTO.getOrganizationId()).build());
        }
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        temp.setBudgetJson(converter.convertObjectToMap(budgetDTO.getBudgetJson()));
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target="budgetJson", ignore = true)
    Budget copyDtoToEntity(BudgetDTO budgetDTO, @MappingTarget Budget budget);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Budget dtoToEntityForSearch(BudgetDTO budgetDTO){
        Budget budget = toEntity(budgetDTO);
        budget.setId(budgetDTO.getId());
        if(StringUtils.isBlank(budgetDTO.getCreatedOn())){
            budget.setCreatedOn(null);
        }
        if(StringUtils.isBlank(budgetDTO.getUpdatedOn())){
            budget.setUpdatedOn(null);
        }
        if(budgetDTO.getOrganizationId() != null){
            Organization org = Organization.builder().id(budgetDTO.getOrganizationId()).build();
            org.setCreatedOn(null);
            org.setUpdatedOn(null);
            budget.setOrganization(org);
        }
        return budget;
    }
}
