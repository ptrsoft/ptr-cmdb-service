package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.InfraTopologyCategoryDTO;
import com.synectiks.asset.domain.query.InfraTopologyCategoryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InfraTopologyCategoryMapper {
    InfraTopologyCategoryMapper INSTANCE = Mappers.getMapper(InfraTopologyCategoryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InfraTopologyCategoryDTO toDto(InfraTopologyCategoryObj infraTopologyCategoryObj);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<InfraTopologyCategoryDTO> toDtoList(List<InfraTopologyCategoryObj> infraTopologyCategoryObjList);

}
