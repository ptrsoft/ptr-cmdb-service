package com.synectiks.asset.mapper.query;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.synectiks.asset.api.model.CloudElementCloudWiseAnalyticsDTO;
import com.synectiks.asset.domain.query.CloudElementCloudWiseQueryObj;

@Mapper
public interface CloudElementCloudWiseQueryMapper {
    CloudElementCloudWiseQueryMapper INSTANCE = Mappers.getMapper(CloudElementCloudWiseQueryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CloudElementCloudWiseAnalyticsDTO toDto(CloudElementCloudWiseQueryObj environmentSummaryQueryObj);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CloudElementCloudWiseAnalyticsDTO> toDtoList(List<CloudElementCloudWiseQueryObj> environmentSummaryQueryObjList);

}