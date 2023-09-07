package com.synectiks.asset.mapper;

import com.synectiks.asset.api.model.DbCategoryDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.DbCategory;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface DbCategoryMapper {
    DbCategoryMapper INSTANCE = Mappers.getMapper(DbCategoryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target = "createdOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "updatedOn", dateFormat = Constants.DEFAULT_DATETIME_FORMAT)
    @Mapping(target = "organizationId", source = "organization.id")
    @Mapping(target = "organizationName", source = "organization.name")
    DbCategoryDTO toDto(DbCategory dbCategory);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    DbCategory toEntity(DbCategoryDTO dbCategoryDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default DbCategoryDTO entityToDto(DbCategory dbCategory){
        DbCategoryDTO dto = toDto(dbCategory);
        dto.setId(dbCategory.getId());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default DbCategory dtoToEntity(DbCategoryDTO dbCategoryDTO){
        DbCategory dbCategory = toEntity(dbCategoryDTO);
        dbCategory.setId(dbCategoryDTO.getId());
        return dbCategory;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<DbCategoryDTO> entityToDtoList(List<DbCategory> dbCategoryList) {
        List<DbCategoryDTO> dbCategoryDTOList = dbCategoryList.stream().map(entityObj -> entityToDto(entityObj)).collect(Collectors.toList());
        return dbCategoryDTOList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default List<DbCategory> dtoToEntityList(List<DbCategoryDTO> dbCategoryDTOList){
        List<DbCategory> dbCategoryList = dbCategoryDTOList.stream().map(dtoObj -> dtoToEntity(dtoObj)).collect(Collectors.toList());
        return dbCategoryList;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    DbCategory dtoToEntityForUpdate(DbCategoryDTO dbCategoryDTO, @MappingTarget DbCategory dbCategory);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default DbCategory dtoToEntityForSearch(DbCategoryDTO dbCategoryDTO){
        DbCategory dbCategory = toEntity(dbCategoryDTO);
        dbCategory.setId(dbCategoryDTO.getId());
        if(StringUtils.isBlank(dbCategoryDTO.getCreatedOn())){
            dbCategory.setCreatedOn(null);
        }
        if(StringUtils.isBlank(dbCategoryDTO.getUpdatedOn())){
            dbCategory.setUpdatedOn(null);
        }
        return dbCategory;
    }
}
