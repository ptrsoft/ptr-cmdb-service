package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.EnvironmentQueryDTO;
import com.synectiks.asset.api.model.EnvironmentSummaryQueryDTO;
import com.synectiks.asset.domain.query.EnvironmentQueryObj;
import com.synectiks.asset.domain.query.EnvironmentSummaryQueryObj;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface EnvironmentQueryMapper {
    EnvironmentQueryMapper INSTANCE = Mappers.getMapper(EnvironmentQueryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    EnvironmentQueryDTO toDto(EnvironmentQueryObj environmentQueryObj);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "landingZoneId", source = "id")
    default List<EnvironmentQueryDTO> toDtoList(List<EnvironmentQueryObj> environmentQueryObjList){
        List<EnvironmentQueryDTO> environmentQueryDTOList = new ArrayList<>();
        for(EnvironmentQueryObj environmentQueryObj: environmentQueryObjList){
            EnvironmentQueryDTO environmentQueryDTO = new EnvironmentQueryDTO();
            environmentQueryDTO.setCloud(environmentQueryObj.getCloud());
            List<EnvironmentSummaryQueryDTO> environmentSummaryQueryDTOList = new ArrayList<>();
            for(EnvironmentSummaryQueryObj environmentSummaryQueryObj: environmentQueryObj.getEnvironmentSummaryList()){
                EnvironmentSummaryQueryDTO environmentSummaryQueryDTO = EnvironmentSummaryQueryMapper.INSTANCE.toDto(environmentSummaryQueryObj);
                environmentSummaryQueryDTOList.add(environmentSummaryQueryDTO);
            }
            environmentQueryDTO.setEnvironmentSummaryList(environmentSummaryQueryDTOList);
            environmentQueryDTOList.add(environmentQueryDTO);
        }
        return environmentQueryDTOList;
    }
    
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    List<EnvironmentQueryDTO> towDtoList(List<EnvironmentSummaryQueryObj> environmentQueryObjList);

}
