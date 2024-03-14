package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.ReportingApi;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.reporting.SpendOverviewReportObj;
import com.synectiks.asset.service.CloudElementService;
import com.synectiks.asset.service.QueryService;
import com.synectiks.asset.util.DateFormatUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ReportingController implements ReportingApi {

    private final Logger logger = LoggerFactory.getLogger(ReportingController.class);

    private static final String ENTITY_NAME = "Reporting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private QueryService queryService;

    @Autowired
    private CloudElementService cloudElementService;

    private DateFormatUtil  dateFormatUtil = new DateFormatUtil();

    @Override
    public ResponseEntity<Object> getSpendOverviewReport(Long orgId, String serviceCategory, String cloud, String granularity, Long compareTo)  {
        Map<String, LocalDate> dateRange = null;
        if(Constants.GRANULARITY_QUARTERLY.equalsIgnoreCase(granularity)){
            dateRange = dateFormatUtil.getDateRangeOfGivenQuarter(compareTo.intValue());
        }
        logger.debug("Request to get spend-overview report. organization id: {}, start date:{}, end date id: {}, cloud: {}", orgId, dateRange.get("startDate").toString(), dateRange.get("endDate").toString(), cloud);
        List<SpendOverviewReportObj> list = queryService.getSpendOverviewReport(orgId, serviceCategory, dateRange.get("startDate").toString(), dateRange.get("endDate").toString(), cloud);
        return ResponseEntity.ok(list);
    }

}
