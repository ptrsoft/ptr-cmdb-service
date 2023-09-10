package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.ApplicationTopologyDTO;
import com.synectiks.asset.domain.query.ApplicationTopologyQueryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ApplicationTopologyMapper {
    ApplicationTopologyMapper INSTANCE = Mappers.getMapper(ApplicationTopologyMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ApplicationTopologyDTO toDto(ApplicationTopologyQueryObj applicationTopologyQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<ApplicationTopologyDTO> toDtoList(List<ApplicationTopologyQueryObj> applicationTopologyQueryObjList);

}