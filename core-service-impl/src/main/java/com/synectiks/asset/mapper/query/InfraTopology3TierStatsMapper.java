package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.InfraTopologyThreeTierStatsDTO;
import com.synectiks.asset.domain.query.InfraTopology3TierStatsQueryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InfraTopology3TierStatsMapper {
    InfraTopology3TierStatsMapper INSTANCE = Mappers.getMapper(InfraTopology3TierStatsMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InfraTopologyThreeTierStatsDTO toDto(InfraTopology3TierStatsQueryObj infraTopology3TierStatsQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<InfraTopologyThreeTierStatsDTO> toDtoList(List<InfraTopology3TierStatsQueryObj> infraTopology3TierStatsQueryObjList);

}