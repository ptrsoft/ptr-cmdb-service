package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.InfraTopologyDTO;
import com.synectiks.asset.domain.query.InfraTopologyObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InfraTopologyMapper {
    InfraTopologyMapper INSTANCE = Mappers.getMapper(InfraTopologyMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InfraTopologyDTO toDto(InfraTopologyObj infraTopologyObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<InfraTopologyDTO> toDtoList(List<InfraTopologyObj> infraTopologyObjList);

}