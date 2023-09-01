package com.synectiks.asset.domain.query;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface InfraTopologyGlobalServiceCategoryWiseViewQueryObj extends Serializable{

    @Value("#{target.element_type}")
    String getElementType();

    @Value("#{target.metadata}")
    ObjectNode getMetadata();

    @Value("#{target.total_record}")
    Long getTotalRecord();


}
