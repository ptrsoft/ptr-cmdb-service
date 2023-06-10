package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.MicroServiceDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.MicroService;
import com.synectiks.asset.service.CustomeHashMapConverter;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface MicroServiceMapper {
    MicroServiceMapper INSTANCE = Mappers.getMapper(MicroServiceMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    MicroServiceDTO toDto(MicroService microService);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default MicroService toEntity(MicroServiceDTO microServiceDTO){
        MicroService microService = toEntityConvertObjectToMap(microServiceDTO);
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        microService.setServiceTopology(converter.convertObjectToMap(microServiceDTO.getServiceTopology()));
        microService.setBusinessLocation(converter.convertObjectToMap(microServiceDTO.getBusinessLocation()));
        microService.setSlaJson(converter.convertObjectToMap(microServiceDTO.getSlaJson()));
        microService.setCostJson(converter.convertObjectToMap(microServiceDTO.getCostJson()));
        microService.setViewJson(converter.convertObjectToMap(microServiceDTO.getViewJson()));
        microService.setConfigJson(converter.convertObjectToMap(microServiceDTO.getConfigJson()));
        microService.setComplianceJson(converter.convertObjectToMap(microServiceDTO.getComplianceJson()));
        return microService;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target="serviceTopology", ignore = true)
    @Mapping(target="businessLocation", ignore = true)
    @Mapping(target="slaJson", ignore = true)
    @Mapping(target="costJson", ignore = true)
    @Mapping(target="viewJson", ignore = true)
    @Mapping(target="configJson", ignore = true)
    @Mapping(target="complianceJson", ignore = true)
    MicroService toEntityConvertObjectToMap(MicroServiceDTO microServiceDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default MicroServiceDTO entityToDto(MicroService microService){
        MicroServiceDTO dto = toDto(microService);
        dto.setId(microService.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default MicroService dtoToEntity(MicroServiceDTO microServiceDTO){
        MicroService microService = toEntity(microServiceDTO);
        microService.setId(microServiceDTO.getId());
        return microService;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<MicroServiceDTO> entityToDtoList(List<MicroService> cloudElementList) {
        List<MicroServiceDTO> cloudElementDTOList = cloudElementList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return cloudElementDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<MicroService> dtoToEntityList(List<MicroServiceDTO> cloudElementDTOList){
        List<MicroService> cloudElementList = cloudElementDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return cloudElementList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default MicroService dtoToEntityForUpdate(MicroServiceDTO microServiceDTO, MicroService microService){
        MicroService temp = copyDtoToEntity(microServiceDTO, microService);
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        temp.setServiceTopology(converter.convertObjectToMap(microServiceDTO.getServiceTopology()));
        temp.setBusinessLocation(converter.convertObjectToMap(microServiceDTO.getBusinessLocation()));
        temp.setSlaJson(converter.convertObjectToMap(microServiceDTO.getSlaJson()));
        temp.setCostJson(converter.convertObjectToMap(microServiceDTO.getCostJson()));
        temp.setViewJson(converter.convertObjectToMap(microServiceDTO.getViewJson()));
        temp.setConfigJson(converter.convertObjectToMap(microServiceDTO.getConfigJson()));
        temp.setComplianceJson(converter.convertObjectToMap(microServiceDTO.getComplianceJson()));
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target="serviceTopology", ignore = true)
    @Mapping(target="businessLocation", ignore = true)
    @Mapping(target="slaJson", ignore = true)
    @Mapping(target="costJson", ignore = true)
    @Mapping(target="viewJson", ignore = true)
    @Mapping(target="configJson", ignore = true)
    @Mapping(target="complianceJson", ignore = true)
    MicroService copyDtoToEntity(MicroServiceDTO microServiceDTO, @MappingTarget MicroService microService);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default MicroService dtoToEntityForSearch(MicroServiceDTO microServiceDTO){
        MicroService microService = toEntity(microServiceDTO);
        microService.setId(microServiceDTO.getId());
        if(StringUtils.isBlank(microServiceDTO.getCreatedOn())){
            microService.setCreatedOn(null);
        }
        if(StringUtils.isBlank(microServiceDTO.getUpdatedOn())){
            microService.setUpdatedOn(null);
        }
        return microService;
    }
}
