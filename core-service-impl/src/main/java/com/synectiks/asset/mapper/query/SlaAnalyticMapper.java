package com.synectiks.asset.mapper.query;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.synectiks.asset.api.model.SlaAnalyticDTO;
import com.synectiks.asset.domain.query.SlaAnalyticQueryObj;


@Mapper
public interface SlaAnalyticMapper {
    SlaAnalyticMapper INSTANCE = Mappers.getMapper(SlaAnalyticMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SlaAnalyticDTO toDto(SlaAnalyticQueryObj slaAnalyticQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<SlaAnalyticDTO> toDtoList(List<SlaAnalyticQueryObj> slaAnalyticQueryObj);

}