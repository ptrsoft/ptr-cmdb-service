package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.ProductEnclaveDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Department;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.domain.ProductEnclave;
import com.synectiks.asset.service.CustomeHashMapConverter;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductEnclaveMapper {
    ProductEnclaveMapper INSTANCE = Mappers.getMapper(ProductEnclaveMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "landingzoneId", source = "landingzone.id")
    ProductEnclaveDTO toDto(ProductEnclave productEnclave);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default ProductEnclave toEntity(ProductEnclaveDTO productEnclaveDTO){
        ProductEnclave productEnclave = toEntityConvertObjectToMap(productEnclaveDTO);
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        productEnclave.setMetadata(converter.convertObjectToMap(productEnclaveDTO.getMetadata()));
        return productEnclave;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target="metadata", ignore = true)
    @Mapping(target = "department.id", source = "departmentId")
    @Mapping(target = "landingzone.id", source = "landingzoneId")
    ProductEnclave toEntityConvertObjectToMap(ProductEnclaveDTO productEnclaveDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default ProductEnclaveDTO entityToDto(ProductEnclave productEnclave){
        ProductEnclaveDTO dto = toDto(productEnclave);
        dto.setId(productEnclave.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default ProductEnclave dtoToEntity(ProductEnclaveDTO productEnclaveDTO){
        ProductEnclave productEnclave = toEntity(productEnclaveDTO);
        productEnclave.setId(productEnclaveDTO.getId());
        return productEnclave;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<ProductEnclaveDTO> entityToDtoList(List<ProductEnclave> productEnclaveList) {
        List<ProductEnclaveDTO> productEnclaveDTOList = productEnclaveList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return productEnclaveDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<ProductEnclave> dtoToEntityList(List<ProductEnclaveDTO> productEnclaveDTOList){
        List<ProductEnclave> productEnclaveList = productEnclaveDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return productEnclaveList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    default ProductEnclave dtoToEntityForUpdate(ProductEnclaveDTO productEnclaveDTO, ProductEnclave productEnclave){
        ProductEnclave temp = copyDtoToEntity(productEnclaveDTO, productEnclave);

        if(productEnclaveDTO.getDepartmentId() != null){
            temp.setDepartment(Department.builder().id(productEnclaveDTO.getDepartmentId()).build());
        }
        if(productEnclaveDTO.getLandingzoneId() != null){
            temp.setLandingzone(Landingzone.builder().id(productEnclaveDTO.getLandingzoneId()).build());
        }
        CustomeHashMapConverter converter = new CustomeHashMapConverter();
        temp.setMetadata(converter.convertObjectToMap(productEnclaveDTO.getMetadata()));
        return temp;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target="metadata", ignore = true)
    ProductEnclave copyDtoToEntity(ProductEnclaveDTO productEnclaveDTO, @MappingTarget ProductEnclave productEnclave);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default ProductEnclave dtoToEntityForSearch(ProductEnclaveDTO productEnclaveDTO){
        ProductEnclave productEnclave = toEntity(productEnclaveDTO);
        productEnclave.setId(productEnclaveDTO.getId());
        if(StringUtils.isBlank(productEnclaveDTO.getCreatedOn())){
            productEnclave.setCreatedOn(null);
        }
        if(StringUtils.isBlank(productEnclaveDTO.getUpdatedOn())){
            productEnclave.setUpdatedOn(null);
        }
        if(productEnclaveDTO.getDepartmentId() != null){
            productEnclave.setDepartment(Department.builder().id(productEnclaveDTO.getDepartmentId()).build());
        }
        if(productEnclaveDTO.getLandingzoneId() != null){
            productEnclave.setLandingzone(Landingzone.builder().id(productEnclaveDTO.getLandingzoneId()).build());
        }

        if(productEnclave.getDepartment() != null){
            productEnclave.getDepartment().setCreatedOn(null);
            productEnclave.getDepartment().setUpdatedOn(null);
        }
        if(productEnclave.getLandingzone() != null){
            productEnclave.getLandingzone().setCreatedOn(null);
            productEnclave.getLandingzone().setUpdatedOn(null);
        }
        return productEnclave;
    }
}
