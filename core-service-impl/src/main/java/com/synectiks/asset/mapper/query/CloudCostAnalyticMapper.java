package com.synectiks.asset.mapper.query;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.synectiks.asset.api.model.CostAnalyticCloudDTO;
import com.synectiks.asset.domain.query.CloudCostAnalyticQueryObj;

@Mapper
public interface CloudCostAnalyticMapper {
    CloudCostAnalyticMapper INSTANCE = Mappers.getMapper(CloudCostAnalyticMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CostAnalyticCloudDTO toDto(CloudCostAnalyticQueryObj cloudCostAnalyticQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CostAnalyticCloudDTO> toDtoList(List<CloudCostAnalyticQueryObj> cloudCostAnalyticQueryObj);

}