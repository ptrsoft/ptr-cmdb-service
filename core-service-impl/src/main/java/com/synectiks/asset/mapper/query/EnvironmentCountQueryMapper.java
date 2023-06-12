package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.EnvironmentCountQueryDTO;
import com.synectiks.asset.domain.query.EnvironmentCountQueryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EnvironmentCountQueryMapper {
    EnvironmentCountQueryMapper INSTANCE = Mappers.getMapper(EnvironmentCountQueryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    EnvironmentCountQueryDTO toDto(EnvironmentCountQueryObj environmentCountQueryObj);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<EnvironmentCountQueryDTO> toDtoList(List<EnvironmentCountQueryObj> environmentCountQueryObj);

}
