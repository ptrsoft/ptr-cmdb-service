package com.synectiks.asset.mapper.query;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.synectiks.asset.api.model.InfraTopologySummaryDTO;
import com.synectiks.asset.domain.query.InfraTopologySummaryQueryObj;

@Mapper
public interface InfraTopologySummeryMapper {
    InfraTopologySummeryMapper INSTANCE = Mappers.getMapper(InfraTopologySummeryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InfraTopologySummaryDTO toDto(InfraTopologySummaryQueryObj infraTopologyObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<InfraTopologySummaryDTO> toDtoList(List<InfraTopologySummaryQueryObj> infraTopologyObjList);

}