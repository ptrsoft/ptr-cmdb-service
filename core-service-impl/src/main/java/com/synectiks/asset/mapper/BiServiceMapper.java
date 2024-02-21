package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.BiServiceDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.BiService;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface BiServiceMapper {
    BiServiceMapper INSTANCE = Mappers.getMapper(BiServiceMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    BiServiceDTO toDto(BiService biService);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default BiService toEntity(BiServiceDTO biServiceDTO){
        BiService biService = toEntityConvertObjectToMap(biServiceDTO);
        return biService;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    BiService toEntityConvertObjectToMap(BiServiceDTO biServiceDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default BiServiceDTO entityToDto(BiService biService){
        BiServiceDTO dto = toDto(biService);
        dto.setId(biService.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default BiService dtoToEntity(BiServiceDTO biServiceDTO){
        BiService biService = toEntity(biServiceDTO);
        biService.setId(biServiceDTO.getId());
        return biService;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<BiServiceDTO> entityToDtoList(List<BiService> biServiceList) {
        List<BiServiceDTO> biServiceDTOList = biServiceList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return biServiceDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<BiService> dtoToEntityList(List<BiServiceDTO> biServiceDTOList){
        List<BiService> biServiceList = biServiceDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return biServiceList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default BiService dtoToEntityForUpdate(BiServiceDTO biServiceDTO, BiService biService){
        BiService temp = copyDtoToEntity(biServiceDTO, biService);
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    BiService copyDtoToEntity(BiServiceDTO biServiceDTO, @MappingTarget BiService biService);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default BiService dtoToEntityForSearch(BiServiceDTO biServiceDTO){
        BiService biService = toEntity(biServiceDTO);
        biService.setId(biServiceDTO.getId());
        if(StringUtils.isBlank(biServiceDTO.getCreatedOn())){
            biService.setCreatedOn(null);
        }
        if(StringUtils.isBlank(biServiceDTO.getUpdatedOn())){
            biService.setUpdatedOn(null);
        }
        return biService;
    }
}
