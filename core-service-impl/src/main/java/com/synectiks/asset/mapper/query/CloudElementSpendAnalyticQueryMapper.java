package com.synectiks.asset.mapper.query;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.synectiks.asset.api.model.CloudElementSpendAnalyticsDTO;
import com.synectiks.asset.domain.query.CloudElementSpendAnalyticsQueryObj;

@Mapper
public interface CloudElementSpendAnalyticQueryMapper {
    CloudElementSpendAnalyticQueryMapper INSTANCE = Mappers.getMapper(CloudElementSpendAnalyticQueryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CloudElementSpendAnalyticsDTO toDto(CloudElementSpendAnalyticsQueryObj environmentQueryObjList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CloudElementSpendAnalyticsDTO> toDtoList(List<CloudElementSpendAnalyticsQueryObj> environmentQueryObjList);

}