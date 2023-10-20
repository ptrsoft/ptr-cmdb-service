package com.synectiks.asset.mapper.query;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.synectiks.asset.api.model.CostAnalyticDepartmentDTO;
import com.synectiks.asset.domain.query.DepartmentCostAnalyticQueryObj;

@Mapper
public interface DepartmentCostAnalyticMapper {
    DepartmentCostAnalyticMapper INSTANCE = Mappers.getMapper(DepartmentCostAnalyticMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CostAnalyticDepartmentDTO toDto(DepartmentCostAnalyticQueryObj deaprtmentCostAnalyticQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CostAnalyticDepartmentDTO> toDtoList(List<DepartmentCostAnalyticQueryObj> deaprtmentCostAnalyticQueryObj);

}