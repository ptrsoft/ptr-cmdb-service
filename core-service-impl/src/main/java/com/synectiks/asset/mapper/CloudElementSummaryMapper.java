package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.CloudElementSummaryDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElementSummary;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.service.CustomeHashMapConverter;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CloudElementSummaryMapper {
    CloudElementSummaryMapper INSTANCE = Mappers.getMapper(CloudElementSummaryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "landingzoneId", source = "landingzone.id")
    CloudElementSummaryDTO toDto(CloudElementSummary cloudElementSummary);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudElementSummary toEntity(CloudElementSummaryDTO cloudElementSummaryDTO){
        CloudElementSummary cloudElementSummary = toEntityConvertObjectToMap(cloudElementSummaryDTO);
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        cloudElementSummary.setSummaryJson(converter.convertObjectToMap(cloudElementSummaryDTO.getSummaryJson()));
        return cloudElementSummary;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target="summaryJson", ignore = true)
    @Mapping(target = "landingzone.id", source = "landingzoneId")
    CloudElementSummary toEntityConvertObjectToMap(CloudElementSummaryDTO cloudElementSummaryDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudElementSummaryDTO entityToDto(CloudElementSummary cloudElementSummary){
        CloudElementSummaryDTO dto = toDto(cloudElementSummary);
        dto.setId(cloudElementSummary.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudElementSummary dtoToEntity(CloudElementSummaryDTO cloudElementSummaryDTO){
        CloudElementSummary cloudElementSummary = toEntity(cloudElementSummaryDTO);
        cloudElementSummary.setId(cloudElementSummaryDTO.getId());
        return cloudElementSummary;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<CloudElementSummaryDTO> entityToDtoList(List<CloudElementSummary> cloudElementSummaryList) {
        List<CloudElementSummaryDTO> cloudElementSummaryDTOList = cloudElementSummaryList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return cloudElementSummaryDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<CloudElementSummary> dtoToEntityList(List<CloudElementSummaryDTO> cloudElementSummaryDTOList){
        List<CloudElementSummary> cloudElementSummaryList = cloudElementSummaryDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return cloudElementSummaryList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default CloudElementSummary dtoToEntityForUpdate(CloudElementSummaryDTO cloudElementSummaryDTO, CloudElementSummary cloudElementSummary){
        CloudElementSummary temp = copyDtoToEntity(cloudElementSummaryDTO, cloudElementSummary);
        if(cloudElementSummaryDTO.getLandingzoneId() != null){
            temp.setLandingzone(Landingzone.builder().id(cloudElementSummaryDTO.getLandingzoneId()).build());
        }
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        temp.setSummaryJson(converter.convertObjectToMap(cloudElementSummaryDTO.getSummaryJson()));
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target="summaryJson", ignore = true)
    CloudElementSummary copyDtoToEntity(CloudElementSummaryDTO cloudElementSummaryDTO, @MappingTarget CloudElementSummary cloudElementSummary);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudElementSummary dtoToEntityForSearch(CloudElementSummaryDTO cloudElementSummaryDTO){
        CloudElementSummary cloudElementSummary = toEntity(cloudElementSummaryDTO);
        cloudElementSummary.setId(cloudElementSummaryDTO.getId());
        if(StringUtils.isBlank(cloudElementSummaryDTO.getCreatedOn())){
            cloudElementSummary.setCreatedOn(null);
        }
        if(StringUtils.isBlank(cloudElementSummaryDTO.getUpdatedOn())){
            cloudElementSummary.setUpdatedOn(null);
        }
        if(cloudElementSummaryDTO.getLandingzoneId() != null){
            cloudElementSummary.setLandingzone(Landingzone.builder().id(cloudElementSummaryDTO.getLandingzoneId()).build());
        }
        if(cloudElementSummary.getLandingzone() != null){
            cloudElementSummary.getLandingzone().setCreatedOn(null);
            cloudElementSummary.getLandingzone().setUpdatedOn(null);
        }

        return cloudElementSummary;
    }
}
