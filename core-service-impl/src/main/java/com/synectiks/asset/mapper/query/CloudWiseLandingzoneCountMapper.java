package com.synectiks.asset.mapper.query;

import com.synectiks.asset.api.model.CloudWiseLandingzoneCountQueryDTO;
import com.synectiks.asset.domain.query.CloudWiseLandingzoneCountQueryObj;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CloudWiseLandingzoneCountMapper {
    CloudWiseLandingzoneCountMapper INSTANCE = Mappers.getMapper(CloudWiseLandingzoneCountMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CloudWiseLandingzoneCountQueryDTO toDto(CloudWiseLandingzoneCountQueryObj cloudWiseLandingzoneCountQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CloudWiseLandingzoneCountQueryDTO> toDtoList(List<CloudWiseLandingzoneCountQueryObj> cloudWiseLandingzoneCountQueryObjList);

}