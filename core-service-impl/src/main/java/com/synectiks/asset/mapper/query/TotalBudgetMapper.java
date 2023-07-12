package com.synectiks.asset.mapper.query;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.synectiks.asset.api.model.TotalBudgetDTO;
import com.synectiks.asset.domain.query.TotalBudgetQueryObj;

@Mapper
public interface TotalBudgetMapper {
    TotalBudgetMapper INSTANCE = Mappers.getMapper(TotalBudgetMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TotalBudgetDTO toDto(TotalBudgetQueryObj totalBudgetQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<TotalBudgetDTO> toDtoList(List<TotalBudgetQueryObj> totalBudgetQueryObj);

}