package com.synectiks.asset.mapper.query;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.synectiks.asset.api.model.CloudElementVpcDTO;
import com.synectiks.asset.domain.query.CloudEnvironmentVpcQueryObj;

@Mapper
public interface CloudEnvironmentVpcQueryMapper {
    CloudEnvironmentVpcQueryMapper INSTANCE = Mappers.getMapper(CloudEnvironmentVpcQueryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CloudElementVpcDTO toDto(CloudEnvironmentVpcQueryObj environmentSummaryQueryObj);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CloudElementVpcDTO> toDtoList(List<CloudEnvironmentVpcQueryObj> environmentSummaryQueryObjList);

}