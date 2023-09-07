package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.ProductEnvDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Product;
import com.synectiks.asset.domain.ProductEnv;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductEnvMapper {
    ProductEnvMapper INSTANCE = Mappers.getMapper(ProductEnvMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    ProductEnvDTO toDto(ProductEnv productEnv);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "product.id", source = "productId")
    ProductEnv toEntity(ProductEnvDTO productEnvDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default ProductEnvDTO entityToDto(ProductEnv productEnv){
        ProductEnvDTO dto = toDto(productEnv);
        dto.setId(productEnv.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default ProductEnv dtoToEntity(ProductEnvDTO productEnvDTO){
        ProductEnv productEnv = toEntity(productEnvDTO);
        productEnv.setId(productEnvDTO.getId());
        return productEnv;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<ProductEnvDTO> entityToDtoList(List<ProductEnv> productEnvList) {
        List<ProductEnvDTO> productEnvDTOList = productEnvList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return productEnvDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<ProductEnv> dtoToEntityList(List<ProductEnvDTO> productEnvDTOList){
        List<ProductEnv> productEnvList = productEnvDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return productEnvList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default ProductEnv dtoToEntityForUpdate(ProductEnvDTO productEnvDTO, @MappingTarget ProductEnv productEnv) {
        ProductEnv temp = copyDtoToEntity(productEnvDTO, productEnv);
        if(productEnvDTO.getProductId() != null){
            temp.setProduct(Product.builder().id(productEnvDTO.getProductId()).build());
        }
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    ProductEnv copyDtoToEntity(ProductEnvDTO productEnvDTO, @MappingTarget ProductEnv productEnv);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default ProductEnv dtoToEntityForSearch(ProductEnvDTO productEnvDTO){
        ProductEnv productEnv = toEntity(productEnvDTO);
        productEnv.setId(productEnvDTO.getId());
        if(StringUtils.isBlank(productEnvDTO.getCreatedOn())){
            productEnv.setCreatedOn(null);
        }
        if(StringUtils.isBlank(productEnvDTO.getUpdatedOn())){
            productEnv.setUpdatedOn(null);
        }
        if(productEnvDTO.getProductId() != null){
            productEnv.setProduct(Product.builder().id(productEnvDTO.getProductId()).build());
        }
        if(productEnv.getProduct() != null){
            productEnv.getProduct().setCreatedOn(null);
            productEnv.getProduct().setUpdatedOn(null);
        }
        return productEnv;
    }
}
