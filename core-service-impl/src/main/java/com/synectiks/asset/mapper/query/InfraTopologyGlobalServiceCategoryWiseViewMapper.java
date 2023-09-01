package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.InfraTopologyCategoryWiseViewDTO;
import com.synectiks.asset.api.model.InfraTopologyGlobalServiceCategoryWiseViewDTO;
import com.synectiks.asset.domain.query.InfraTopologyCategoryWiseViewQueryObj;
import com.synectiks.asset.domain.query.InfraTopologyGlobalServiceCategoryWiseViewQueryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InfraTopologyGlobalServiceCategoryWiseViewMapper {
    InfraTopologyGlobalServiceCategoryWiseViewMapper INSTANCE = Mappers.getMapper(InfraTopologyGlobalServiceCategoryWiseViewMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InfraTopologyGlobalServiceCategoryWiseViewDTO toDto(InfraTopologyGlobalServiceCategoryWiseViewQueryObj infraTopologyGlobalServiceCategoryWiseViewQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<InfraTopologyGlobalServiceCategoryWiseViewDTO> toDtoList(List<InfraTopologyGlobalServiceCategoryWiseViewQueryObj> infraTopologyGlobalServiceCategoryWiseViewQueryObjList);

}