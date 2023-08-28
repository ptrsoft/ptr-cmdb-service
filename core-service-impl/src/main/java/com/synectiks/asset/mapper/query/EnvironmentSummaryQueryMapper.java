package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.EnvironmentSummaryQueryDTO;
import com.synectiks.asset.domain.query.EnvironmentSummaryQueryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EnvironmentSummaryQueryMapper {
    EnvironmentSummaryQueryMapper INSTANCE = Mappers.getMapper(EnvironmentSummaryQueryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "landingZoneId", source = "id")
    EnvironmentSummaryQueryDTO toDto(EnvironmentSummaryQueryObj environmentSummaryQueryObj);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<EnvironmentSummaryQueryDTO> toDtoList(List<EnvironmentSummaryQueryObj> environmentSummaryQueryObjList);

}
