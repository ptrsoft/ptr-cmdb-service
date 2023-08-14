package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.ThreeTierDTO;
import com.synectiks.asset.domain.query.ThreeTierQueryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface ThreeTierQueryMapper {
    ThreeTierQueryMapper INSTANCE = Mappers.getMapper(ThreeTierQueryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ThreeTierDTO toDto(ThreeTierQueryObj threeTierQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<ThreeTierDTO> toDtoList(List<ThreeTierQueryObj> threeTierQueryObjList);

}