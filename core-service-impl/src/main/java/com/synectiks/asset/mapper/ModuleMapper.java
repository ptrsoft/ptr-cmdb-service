package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.ModuleDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Module;
import com.synectiks.asset.domain.Product;
import com.synectiks.asset.domain.ProductEnv;
import com.synectiks.asset.service.CustomeHashMapConverter;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ModuleMapper {
    ModuleMapper INSTANCE = Mappers.getMapper(ModuleMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productEnvId", source = "productEnv.id")
    ModuleDTO toDto(Module module);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Module toEntity(ModuleDTO moduleDTO){
        Module module = toEntityConvertObjectToMap(moduleDTO);
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        module.setServiceTopology(converter.convertObjectToMap(moduleDTO.getServiceTopology()));
        return module;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target="serviceTopology", ignore = true)
    @Mapping(target="metadata", ignore = true)
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "productEnv.id", source = "productEnvId")
    Module toEntityConvertObjectToMap(ModuleDTO moduleDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default ModuleDTO entityToDto(Module module){
        ModuleDTO dto = toDto(module);
        dto.setId(module.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Module dtoToEntity(ModuleDTO moduleDTO){
        Module module = toEntity(moduleDTO);
        module.setId(moduleDTO.getId());
        return module;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<ModuleDTO> entityToDtoList(List<Module> moduleList) {
        List<ModuleDTO> moduleDTOList = moduleList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return moduleDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<Module> dtoToEntityList(List<ModuleDTO> moduleDTOList){
        List<Module> moduleList = moduleDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return moduleList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default Module dtoToEntityForUpdate(ModuleDTO moduleDTO, Module module){
        Module temp = copyDtoToEntity(moduleDTO, module);

        if(moduleDTO.getProductEnvId() != null){
            temp.setProductEnv(ProductEnv.builder().id(moduleDTO.getProductEnvId()).build());
        }
        if(moduleDTO.getProductId() != null){
            temp.setProduct(Product.builder().id(moduleDTO.getProductId()).build());
        }
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        temp.setServiceTopology(converter.convertObjectToMap(moduleDTO.getServiceTopology()));
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target="serviceTopology", ignore = true)
    @Mapping(target="metadata", ignore = true)
    Module copyDtoToEntity(ModuleDTO moduleDTO, @MappingTarget Module module);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Module dtoToEntityForSearch(ModuleDTO moduleDTO){
        Module module = toEntity(moduleDTO);
        module.setId(moduleDTO.getId());
        if(StringUtils.isBlank(moduleDTO.getCreatedOn())){
            module.setCreatedOn(null);
        }
        if(StringUtils.isBlank(moduleDTO.getUpdatedOn())){
            module.setUpdatedOn(null);
        }
        if(moduleDTO.getProductEnvId() != null){
            module.setProductEnv(ProductEnv.builder().id(moduleDTO.getProductEnvId()).build());
        }
        if(moduleDTO.getProductId() != null){
            module.setProduct(Product.builder().id(moduleDTO.getProductId()).build());
        }
        if(module.getProductEnv() != null) {
            module.getProductEnv().setCreatedOn(null);
            module.getProductEnv().setUpdatedOn(null);
        }
        if(module.getProduct() != null) {
            module.getProduct().setCreatedOn(null);
            module.getProduct().setUpdatedOn(null);
        }
        return module;
    }
}
