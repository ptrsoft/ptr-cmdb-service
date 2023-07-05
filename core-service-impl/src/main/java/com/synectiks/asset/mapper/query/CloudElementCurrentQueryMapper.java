package com.synectiks.asset.mapper.query;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.synectiks.asset.api.model.CloudElementCurrentsDTO;
import com.synectiks.asset.domain.query.CloudElementCurrentQueryObj;

@Mapper
public interface CloudElementCurrentQueryMapper {
    CloudElementCurrentQueryMapper INSTANCE = Mappers.getMapper(CloudElementCurrentQueryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CloudElementCurrentsDTO toDto(CloudElementCurrentQueryObj environmentSummaryQueryObj);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CloudElementCurrentsDTO> toDtoList(List<CloudElementCurrentQueryObj> environmentSummaryQueryObjList);

}