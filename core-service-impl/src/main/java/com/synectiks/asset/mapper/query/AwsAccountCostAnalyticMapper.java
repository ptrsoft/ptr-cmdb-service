package com.synectiks.asset.mapper.query;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.synectiks.asset.api.model.CostAnalyticAwsAccountDTO;
import com.synectiks.asset.domain.query.AwsAccountCostAnalyticQueryObj;

@Mapper
public interface AwsAccountCostAnalyticMapper {
    AwsAccountCostAnalyticMapper INSTANCE = Mappers.getMapper(AwsAccountCostAnalyticMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CostAnalyticAwsAccountDTO toDto(AwsAccountCostAnalyticQueryObj awsAccountCostAnalyticQueryObj) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<CostAnalyticAwsAccountDTO> toDtoList(List<AwsAccountCostAnalyticQueryObj> awsAccountCostAnalyticQueryObj);

}