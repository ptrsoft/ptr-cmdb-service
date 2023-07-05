package com.synectiks.asset.mapper.query;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.synectiks.asset.api.model.CloudElementCloudWiseMonthlyAnalyticsDTO;
import com.synectiks.asset.domain.query.CloudElementCloudWiseMonthlyQueryObj;

@Mapper
public interface CloudElementCloudWiseMonthlyQueryMapper {
    CloudElementCloudWiseMonthlyQueryMapper INSTANCE = Mappers.getMapper(CloudElementCloudWiseMonthlyQueryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CloudElementCloudWiseMonthlyAnalyticsDTO toDto(CloudElementCloudWiseMonthlyQueryObj environmentSummaryQueryObj);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CloudElementCloudWiseMonthlyAnalyticsDTO> toDtoList(List<CloudElementCloudWiseMonthlyQueryObj> environmentSummaryQueryObjList);

}