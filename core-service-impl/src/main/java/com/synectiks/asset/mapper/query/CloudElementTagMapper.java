package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.CloudElementTagDTO;
import com.synectiks.asset.domain.query.CloudElementTagQueryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CloudElementTagMapper {
    CloudElementTagMapper INSTANCE = Mappers.getMapper(CloudElementTagMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CloudElementTagDTO toDto(CloudElementTagQueryObj cloudElementTagQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CloudElementTagDTO> toDtoList(List<CloudElementTagQueryObj> cloudElementTagQueryObjList);

}