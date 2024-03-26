package com.synectiks.asset.domain.query;

import com.synectiks.asset.service.CustomeHashMapConverter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Convert;
import java.io.Serializable;
import java.util.Map;

public interface BiMappingBusinessCloudElementQueryObj extends Serializable{

    @Value("#{target.id}")
    Long getId();

    @Value("#{target.business_element_id}")
    Long getBusinessElementId();

    @Value("#{target.service_name}")
    String getServiceName();
    @Value("#{target.service_nature}")
    String getServiceNature();
    @Value("#{target.service_type}")
    String getServiceType();
    @Value("#{target.element_type}")
    String getElementType();
    @Value("#{target.arn}")
    String getArn();
    @Value("#{target.instance_id}")
    String getInstanceId();
    @Value("#{target.instance_name}")
    String getInstanceName();
    @Value("#{target.category}")
    String getCategory();
    @Value("#{target.landingzone_id}")
    Long getLandingzoneId();
    @Value("#{target.db_category_id}")
    Long getDbCategoryId();
    @Value("#{target.product_enclave_id}")
    Long getProductEnclaveId();
    @Value("#{target.product_enclave_instance_id}")
    String getProductEnclaveInstanceId();
    @Value("#{target.status}")
    String getStatus();
    @Value("#{target.created_by}")
    String getCreatedBy();
    @Value("#{target.created_on}")
    String getCreatedOn();
    @Value("#{target.updated_by}")
    String getUpdatedBy();
    @Value("#{target.updated_on}")
    String getUpdatedOn();
    @Value("#{target.log_location}")
    String getLogLocation();
    @Value("#{target.trace_location}")
    String getTraceLocation();
    @Value("#{target.metric_location}")
    String getMetricLocation();
    @Value("#{target.landing_zone}")
    String getLandingZone();
    @Value("#{target.cloud}")
    String getCloud();
    @Value("#{target.db_category_name}")
    String getDbCategoryName();

    @Convert(converter = CustomeHashMapConverter.class)
    @Value("#{target.sla_json}")
    Map<String, Object> getSlaJson();
    @Convert(converter = CustomeHashMapConverter.class)
    @Value("#{target.cost_json}")
    Map<String, Object> getCostJson();
    @Convert(converter = CustomeHashMapConverter.class)
    @Value("#{target.view_json}")
    Map<String, Object> getViewJson();

    @Convert(converter = CustomeHashMapConverter.class)
    @Value("#{target.config_json}")
    Map<String, Object> getConfigJson();

    @Convert(converter = CustomeHashMapConverter.class)
    @Value("#{target.compliance_json}")
    Map<String, Object> getComplianceJson();

    @Convert(converter = CustomeHashMapConverter.class)
    @Value("#{target.hosted_services}")
    Map<String, Object> getHostedServices();

    @Value("#{target.service_category}")
    String getServiceCategory();
    @Value("#{target.region}")
    String getRegion();
    @Value("#{target.log_group}")
    String getLogGroup();



}
