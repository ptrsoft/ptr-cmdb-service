package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.InfraTopologyElementDTO;
import com.synectiks.asset.domain.query.InfraTopologyElementObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InfraTopologyElementMapper {
    InfraTopologyElementMapper INSTANCE = Mappers.getMapper(InfraTopologyElementMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InfraTopologyElementDTO toDto(InfraTopologyElementObj infraTopologyElementObj);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<InfraTopologyElementDTO> toDtoList(List<InfraTopologyElementObj> infraTopologyElementObjList);

}
