package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.EnvironmentQueryDTO;
import com.synectiks.asset.domain.query.EnvironmentQueryObj;
import com.synectiks.asset.domain.query.EnvironmentSummaryQueryObj;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EnvironmentQueryMapper {
    EnvironmentQueryMapper INSTANCE = Mappers.getMapper(EnvironmentQueryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    EnvironmentQueryDTO toDto(EnvironmentQueryObj environmentQueryObj);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<EnvironmentQueryDTO> toDtoList(List<EnvironmentQueryObj> environmentQueryObjList);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<EnvironmentQueryDTO> towDtoList(List<EnvironmentSummaryQueryObj> environmentQueryObjList);

}
