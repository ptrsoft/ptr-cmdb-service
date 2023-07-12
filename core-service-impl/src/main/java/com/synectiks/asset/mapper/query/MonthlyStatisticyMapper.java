package com.synectiks.asset.mapper.query;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.synectiks.asset.api.model.MonthlyStatisticsDTO;
import com.synectiks.asset.domain.query.MonthlyStatisticsQueryObj;

@Mapper
public interface MonthlyStatisticyMapper {
    MonthlyStatisticyMapper INSTANCE = Mappers.getMapper(MonthlyStatisticyMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MonthlyStatisticsDTO toDto(MonthlyStatisticsQueryObj monthlyStatisticsQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<MonthlyStatisticsDTO> toDtoList(List<MonthlyStatisticsQueryObj> monthlyStatisticsQueryObj);

}