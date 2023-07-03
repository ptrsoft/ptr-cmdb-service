package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.InfraTopologyProductEnclaveDTO;
import com.synectiks.asset.domain.query.InfraTopologyProductEnclaveObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InfraTopologyProductEnclaveMapper {
    InfraTopologyProductEnclaveMapper INSTANCE = Mappers.getMapper(InfraTopologyProductEnclaveMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    InfraTopologyProductEnclaveDTO toDto(InfraTopologyProductEnclaveObj infraTopologyProductEnclaveObj);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<InfraTopologyProductEnclaveDTO> toDtoList(List<InfraTopologyProductEnclaveObj> infraTopologyProductEnclaveObjList);

}
