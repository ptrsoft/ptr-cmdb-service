package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.CloudDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Cloud;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CloudMapper {
    CloudMapper INSTANCE = Mappers.getMapper(CloudMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    CloudDTO toDto(Cloud cloud);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Cloud toEntity(CloudDTO cloudDTO){
        Cloud cloud = toEntityConvertObjectToMap(cloudDTO);
        return cloud;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    Cloud toEntityConvertObjectToMap(CloudDTO cloudDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default CloudDTO entityToDto(Cloud cloud){
        CloudDTO dto = toDto(cloud);
        dto.setId(cloud.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Cloud dtoToEntity(CloudDTO cloudDTO){
        Cloud cloud = toEntity(cloudDTO);
        cloud.setId(cloudDTO.getId());
        return cloud;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<CloudDTO> entityToDtoList(List<Cloud> cloudList) {
        List<CloudDTO> cloudDTOList = cloudList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return cloudDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<Cloud> dtoToEntityList(List<CloudDTO> cloudDTOList){
        List<Cloud> cloudList = cloudDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return cloudList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default Cloud dtoToEntityForUpdate(CloudDTO cloudDTO, Cloud cloud){
        Cloud temp = copyDtoToEntity(cloudDTO, cloud);
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Cloud copyDtoToEntity(CloudDTO cloudDTO, @MappingTarget Cloud cloud);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Cloud dtoToEntityForSearch(CloudDTO cloudDTO){
        Cloud cloud = toEntity(cloudDTO);
        cloud.setId(cloudDTO.getId());
        if(StringUtils.isBlank(cloudDTO.getCreatedOn())){
            cloud.setCreatedOn(null);
        }
        if(StringUtils.isBlank(cloudDTO.getUpdatedOn())){
            cloud.setUpdatedOn(null);
        }
        return cloud;
    }
}
