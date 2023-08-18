package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.ProductDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Department;
import com.synectiks.asset.domain.Organization;
import com.synectiks.asset.domain.Product;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "organizationId", source = "organization.id")
    @Mapping(target = "departmentId", source = "department.id")
    ProductDTO toDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "organization.id", source = "organizationId")
    @Mapping(target = "department.id", source = "departmentId")
    Product toEntity(ProductDTO productDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default ProductDTO entityToDto(Product product){
        ProductDTO dto = toDto(product);
        dto.setId(product.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Product dtoToEntity(ProductDTO productDTO){
        Product product = toEntity(productDTO);
        product.setId(productDTO.getId());
        return product;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<ProductDTO> entityToDtoList(List<Product> productList) {
        List<ProductDTO> productDTOList = productList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return productDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<Product> dtoToEntityList(List<ProductDTO> productDTOList){
        List<Product> productList = productDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return productList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default Product dtoToEntityForUpdate(ProductDTO productDTO, Product product){
        Product temp = copyDtoToEntity(productDTO, product);
        if(productDTO.getOrganizationId() != null){
            temp.setOrganization(Organization.builder().id(productDTO.getOrganizationId()).build());
        }
        if(productDTO.getDepartmentId() != null){
            temp.setDepartment(Department.builder().id(productDTO.getDepartmentId()).build());
        }
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Product copyDtoToEntity(ProductDTO productDTO, @MappingTarget Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Product dtoToEntityForSearch(ProductDTO productDTO){
        Product product = toEntity(productDTO);
        product.setId(productDTO.getId());
        if(StringUtils.isBlank(productDTO.getCreatedOn())){
            product.setCreatedOn(null);
        }
        if(StringUtils.isBlank(productDTO.getUpdatedOn())){
            product.setUpdatedOn(null);
        }
        if(productDTO.getOrganizationId() != null){
            product.setOrganization(Organization.builder().id(productDTO.getOrganizationId()).build());
        }
        if(productDTO.getDepartmentId() != null){
            product.setDepartment(Department.builder().id(productDTO.getDepartmentId()).build());
        }
        if(product.getOrganization() != null){
            product.getOrganization().setCreatedOn(null);
            product.getOrganization().setUpdatedOn(null);
        }
        if(product.getDepartment() != null){
            product.getDepartment().setCreatedOn(null);
            product.getDepartment().setUpdatedOn(null);
        }
        return product;
    }
}
