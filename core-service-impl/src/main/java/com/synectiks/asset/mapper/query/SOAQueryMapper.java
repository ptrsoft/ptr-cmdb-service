package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.SOADTO;
import com.synectiks.asset.domain.query.SOAQueryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface SOAQueryMapper {
    SOAQueryMapper INSTANCE = Mappers.getMapper(SOAQueryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SOADTO toDto(SOAQueryObj soaQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<SOADTO> toDtoList(List<SOAQueryObj> soaQueryObjList);

}