package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.ProcessCentralAnalyticDTO;
import com.synectiks.asset.domain.query.ProcessCentralAnalyticQueryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface ProcessCentralAnalyticMapper {
    ProcessCentralAnalyticMapper INSTANCE = Mappers.getMapper(ProcessCentralAnalyticMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProcessCentralAnalyticDTO toDto(ProcessCentralAnalyticQueryObj processCentralAnalyticQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<ProcessCentralAnalyticDTO> toDtoList(List<ProcessCentralAnalyticQueryObj> slaAnalyticQueryObj);

}