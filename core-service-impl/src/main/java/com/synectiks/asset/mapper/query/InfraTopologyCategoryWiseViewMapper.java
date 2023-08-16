package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.InfraTopologyCategoryWiseViewDTO;
import com.synectiks.asset.domain.query.InfraTopologyCategoryWiseViewQueryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InfraTopologyCategoryWiseViewMapper {
    InfraTopologyCategoryWiseViewMapper INSTANCE = Mappers.getMapper(InfraTopologyCategoryWiseViewMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InfraTopologyCategoryWiseViewDTO toDto(InfraTopologyCategoryWiseViewQueryObj infraTopologyCategoryWiseViewQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<InfraTopologyCategoryWiseViewDTO> toDtoList(List<InfraTopologyCategoryWiseViewQueryObj> infraTopologyCategoryWiseViewQueryObjList);

}