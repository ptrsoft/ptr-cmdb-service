package com.synectiks.asset.mapper.query;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.synectiks.asset.api.model.CostAnalyticCloudElementDTO;
import com.synectiks.asset.domain.query.CloudElementCostAnalyticQueryObj;

@Mapper
public interface CloudElementCostAnalyticMapper {
    CloudElementCostAnalyticMapper INSTANCE = Mappers.getMapper(CloudElementCostAnalyticMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CostAnalyticCloudElementDTO toDto(CloudElementCostAnalyticQueryObj cloudElementCostAnalyticQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CostAnalyticCloudElementDTO> toDtoList(List<CloudElementCostAnalyticQueryObj> cloudElementCostAnalyticQueryObj);

}