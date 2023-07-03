package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.InfraTopologyHostingTypeDTO;
import com.synectiks.asset.domain.query.InfraTopologyHostingTypeObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InfraTopologyHostingTypeMapper {
    InfraTopologyHostingTypeMapper INSTANCE = Mappers.getMapper(InfraTopologyHostingTypeMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InfraTopologyHostingTypeDTO toDto(InfraTopologyHostingTypeObj infraTopologyHostingTypeObj);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<InfraTopologyHostingTypeDTO> toDtoList(List<InfraTopologyHostingTypeObj> infraTopologyHostingTypeObjList);

}
