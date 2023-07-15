package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.CostAnalyticDTO;
import com.synectiks.asset.domain.query.CostAnalyticQueryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CostAnalyticMapper {
    CostAnalyticMapper INSTANCE = Mappers.getMapper(CostAnalyticMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CostAnalyticDTO toDto(CostAnalyticQueryObj costAnalyticQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CostAnalyticDTO> toDtoList(List<CostAnalyticQueryObj> costAnalyticQueryObjList);

}