package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.InfraTopologySOAStatsDTO;
import com.synectiks.asset.domain.query.InfraTopologySOAStatsQueryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InfraTopologySOAStatsMapper {
    InfraTopologySOAStatsMapper INSTANCE = Mappers.getMapper(InfraTopologySOAStatsMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InfraTopologySOAStatsDTO toDto(InfraTopologySOAStatsQueryObj infraTopologySOAStatsQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<InfraTopologySOAStatsDTO> toDtoList(List<InfraTopologySOAStatsQueryObj> infraTopologySOAStatsQueryObjList);

}