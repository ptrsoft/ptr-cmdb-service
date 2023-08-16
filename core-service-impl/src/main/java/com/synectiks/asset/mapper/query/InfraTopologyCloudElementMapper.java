package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.InfraTopologyCloudElementDTO;
import com.synectiks.asset.domain.query.InfraTopologyCloudElementQueryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InfraTopologyCloudElementMapper {
    InfraTopologyCloudElementMapper INSTANCE = Mappers.getMapper(InfraTopologyCloudElementMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InfraTopologyCloudElementDTO toDto(InfraTopologyCloudElementQueryObj infraTopologyCloudElementQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<InfraTopologyCloudElementDTO> toDtoList(List<InfraTopologyCloudElementQueryObj> infraTopologyCloudElementQueryObjList);

}