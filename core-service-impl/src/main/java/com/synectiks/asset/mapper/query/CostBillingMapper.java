package com.synectiks.asset.mapper.query;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.synectiks.asset.api.model.CostBillingDTO;
import com.synectiks.asset.domain.query.CostBillingQueryObj;

@Mapper
public interface CostBillingMapper {
    CostBillingMapper INSTANCE = Mappers.getMapper(CostBillingMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CostBillingDTO toDto(CostBillingQueryObj costBillingQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CostBillingDTO> toDtoList(List<CostBillingQueryObj> costBillingQueryObj);

}