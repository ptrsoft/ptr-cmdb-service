package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.ReportingApi;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.reporting.*;
import com.synectiks.asset.service.CloudElementService;
import com.synectiks.asset.service.QueryService;
import com.synectiks.asset.util.DateFormatUtil;
import com.synectiks.asset.util.RandomUtil;
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
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


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
        Map<String, LocalDate> dateRange = dateFormatUtil.getDateRange(granularity, compareTo);
        if(dateRange == null || (dateRange != null && dateRange.size() ==0)){
            logger.error("Granularity not supported. Granularity: {}",granularity);
            StringBuilder sb = new StringBuilder("Supported granularity: ");
            sb = sb.append(Constants.GRANULARITY_YEARLY).append(",").append(Constants.GRANULARITY_MONTHLY).append(",")
                    .append(Constants.GRANULARITY_QUARTERLY).append(",").append(Constants.GRANULARITY_WEEKLY).append(",")
                    .append(Constants.GRANULARITY_DAILY);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(sb);
        }
        logger.debug("Request to get spend-overview report. organization id: {}, start date:{}, end date id: {}, cloud: {}", orgId, dateRange.get("startDate").toString(), dateRange.get("endDate").toString(), cloud);
        List<SpendOverviewReportObj> list = queryService.getSpendOverviewReport(orgId, serviceCategory, dateRange.get("startDate").toString(), dateRange.get("endDate").toString(), cloud);
        Map<String, Object> response = new HashMap<>();
        response.put("report","SPEND OVERVIEW");
        response.put("data", (list == null || (list != null && list.size() ==0)) ? Collections.emptyList() : list);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Object> getTopUsedServicesReport(Long orgId, String service,String cloud, String granularity,Long compareTo,Long noOfRecords,String order) {
        Map<String, LocalDate> currentDateRange = dateFormatUtil.getDateRange(granularity, compareTo);

        int prevCompareTo = (Math.abs(compareTo.intValue())+1);
        if (compareTo < 0){
            prevCompareTo = prevCompareTo * -1;
        }
        Map<String, LocalDate> prevDateRange = dateFormatUtil.getDateRange(granularity, new Long(prevCompareTo));

        logger.debug("Request to get top-used-services report. organization id: {}, start date:{}, end date id: {}, previous start date:{}, previous end date id: {}, cloud: {}", orgId, currentDateRange.get("startDate").toString(), currentDateRange.get("endDate").toString(), prevDateRange.get("startDate").toString(), prevDateRange.get("endDate").toString(), cloud);
        List<TopUsedServicesReportObj> list = queryService.getTopUsedServicesReport(orgId, service, currentDateRange.get("startDate").toString(), currentDateRange.get("endDate").toString(), prevDateRange.get("startDate").toString(), prevDateRange.get("endDate").toString(), cloud, noOfRecords, order);
        boolean isCurrentTotalFound = false;
        boolean isPreviousTotalFound = false;
        for(TopUsedServicesReportObj topUsedServicesReportObj: list){
            if(topUsedServicesReportObj.getElementType().equalsIgnoreCase("CURRENT_TOTAL")){
                isCurrentTotalFound = true;
            }
        }
        if(!isCurrentTotalFound){
            ResponseEntity.ok(Collections.emptyList());
        }
        for(TopUsedServicesReportObj topUsedServicesReportObj: list){
            if(topUsedServicesReportObj.getElementType().equalsIgnoreCase("PREVIOUS_TOTAL")){
                isPreviousTotalFound = true;
            }
        }
        TopUsedServicesReportObj previousObj = null;
        if(!isPreviousTotalFound && isCurrentTotalFound){
            for(TopUsedServicesReportObj topUsedServicesReportObj: list){
                if(topUsedServicesReportObj.getElementType().equalsIgnoreCase("PERCENTAGE")
                        && topUsedServicesReportObj.getTotal() == null){
                    topUsedServicesReportObj.setTotal("100");
                    previousObj = new TopUsedServicesReportObj();
                    break;
                }
            }
            if(previousObj != null){
                previousObj.setId(new Long(list.size()+1));
                previousObj.setElementType("PREVIOUS_TOTAL");
                previousObj.setTotal("0");
            }
            list.add(previousObj);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("report","TOP USED SERVICE");
        response.put("data", (list == null || (list != null && list.size() ==0)) ? Collections.emptyList() : list);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Object> getPotentialSavingsReport(Long orgId, String cloud, String granularity, Long compareTo) {
        int min = 1000;
        int max = 5000;

        PotentialSavingsReportObj spotInstance = new PotentialSavingsReportObj();
        spotInstance.setId(1L);
        spotInstance.setInstanceType("SPOT");
        spotInstance.setTotal(String.valueOf(RandomUtil.getRandomLong(min, max)));

        PotentialSavingsReportObj reservedInstance = new PotentialSavingsReportObj();
        reservedInstance.setId(2L);
        reservedInstance.setInstanceType("RESERVED");
        reservedInstance.setTotal(String.valueOf(RandomUtil.getRandomLong(min, max)));

        PotentialSavingsReportObj othersInstance = new PotentialSavingsReportObj();
        othersInstance.setId(3L);
        othersInstance.setInstanceType("OTHERS");
        othersInstance.setTotal(String.valueOf(RandomUtil.getRandomLong(min, max)));

        PotentialSavingsReportObj rightSizeInstance = new PotentialSavingsReportObj();
        rightSizeInstance.setId(4L);
        rightSizeInstance.setInstanceType("RIGHTSIZE");
        rightSizeInstance.setTotal(String.valueOf(RandomUtil.getRandomLong(min, max)));

        PotentialSavingsReportObj previousTotal = new PotentialSavingsReportObj();
        previousTotal.setId(5L);
        previousTotal.setInstanceType("PREVIOUS_TOTAL");
        Long pt = Long.parseLong(spotInstance.getTotal())+Long.parseLong(reservedInstance.getTotal())+Long.parseLong(othersInstance.getTotal())+Long.parseLong(rightSizeInstance.getTotal())+RandomUtil.getRandomLong(min, max);
        previousTotal.setTotal(String.valueOf(pt));

        PotentialSavingsReportObj currentTotal = new PotentialSavingsReportObj();
        currentTotal.setId(6L);
        currentTotal.setInstanceType("CURRENT_TOTAL");
        Long ct = pt+RandomUtil.getRandomLong(min, max);
        currentTotal.setTotal(String.valueOf(ct));

        PotentialSavingsReportObj percentage = new PotentialSavingsReportObj();
        percentage.setId(7L);
        percentage.setInstanceType("PERCENTAGE");

        double x = ct-pt;
        double y = x/ct;
        double per = y*100;

        DecimalFormat df = new DecimalFormat("#.##");
        percentage.setTotal(df.format(per));

        List<PotentialSavingsReportObj> list = new ArrayList<>();
        list.add(spotInstance);
        list.add(reservedInstance);
        list.add(othersInstance);
        list.add(rightSizeInstance);
        list.add(previousTotal);
        list.add(currentTotal);
        list.add(percentage);

        Map<String, Object> response = new HashMap<>();
        response.put("report","POTENTIAL SAVINGS");
        response.put("data", (list == null || (list != null && list.size() ==0)) ? Collections.emptyList() : list);

        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<Object> getCostTopAccountsReport(Long orgId, String cloud, String account, String granularity, Long compareTo,Long noOfRecords, String order) {
        Map<String, LocalDate> dateRange = dateFormatUtil.getDateRange(granularity, compareTo);
        if(dateRange == null || (dateRange != null && dateRange.size() ==0)){
            logger.error("Granularity not supported. Granularity: {}",granularity);
            StringBuilder sb = new StringBuilder("Supported granularity: ");
            sb = sb.append(Constants.GRANULARITY_YEARLY).append(",").append(Constants.GRANULARITY_MONTHLY).append(",")
                    .append(Constants.GRANULARITY_QUARTERLY).append(",").append(Constants.GRANULARITY_WEEKLY).append(",")
                    .append(Constants.GRANULARITY_DAILY);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(sb);
        }

        logger.debug("Request to get cost-of-top-account report. organization id: {}, start date:{}, end date id: {}, cloud: {}", orgId, dateRange.get("startDate").toString(), dateRange.get("endDate").toString(), cloud);
        List<CostOfTopAccountsReportObj> list = queryService.getCostOfTopAccountsReport(orgId, cloud, account, dateRange.get("startDate").toString(), dateRange.get("endDate").toString(), noOfRecords,  order);
        Map<String, Object> response = new HashMap<>();
        response.put("report","COST OF TOP ACCOUNTS");
        response.put("data", (list == null || (list != null && list.size() ==0)) ? Collections.emptyList() : list);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Object> getSpendingTrendReport(Long orgId, String cloud, String granularity, Long compareTo, Boolean forcast) {
        Map<String, LocalDate> currentDateRange = dateFormatUtil.getDateRange(granularity, compareTo);

        int prevCompareTo = (Math.abs(compareTo.intValue())+1);
        if (compareTo < 0){
            prevCompareTo = prevCompareTo * -1;
        }
        Map<String, LocalDate> prevDateRange = dateFormatUtil.getDateRange(granularity, new Long(prevCompareTo));
        Map<String, LocalDate> futureDateRange = dateFormatUtil.getDateRange(granularity, new Long(Math.abs(prevCompareTo)));

        List<SpendingTrendReportObj> list = queryService.getSpendingTrendReport(orgId, currentDateRange.get("startDate").toString(), currentDateRange.get("endDate").toString(), prevDateRange.get("startDate").toString(), prevDateRange.get("endDate").toString(), futureDateRange.get("startDate").toString(), futureDateRange.get("endDate").toString(), cloud);

        Map<String, Object> response = new HashMap<>();
        response.put("report","SPENDING TREND");
        if(list == null || (list != null && list.size() ==0)){
            response.put("data",Collections.emptyList());
            return ResponseEntity.ok(response);
        }

        Map<String, Object> dataObj = new HashMap<>();
        dataObj.put("current", list.stream().filter(entityObj -> entityObj.getTenure().equalsIgnoreCase("current")).collect(Collectors.toList()));
        dataObj.put("previous", list.stream().filter(entityObj -> entityObj.getTenure().equalsIgnoreCase("previous")).collect(Collectors.toList()));
        if(forcast){
            dataObj.put("forcast", list.stream().filter(entityObj -> entityObj.getTenure().equalsIgnoreCase("forcast")).collect(Collectors.toList()));
        }
        response.put("data",dataObj);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Object> getSpendOverviewDetailReport(Long orgId, String serviceCategory, String cloud, String granularity, Long compareTo)  {
        Map<String, LocalDate> currentDateRange = dateFormatUtil.getDateRange(granularity, compareTo);
        int prevCompareTo = (Math.abs(compareTo.intValue())+1);
        if (compareTo < 0){
            prevCompareTo = prevCompareTo * -1;
        }

        int prevToPrevCompareTo = (Math.abs(prevCompareTo)+1);
        if (prevCompareTo < 0){
            prevToPrevCompareTo = prevToPrevCompareTo * -1;
        }

        Map<String, LocalDate> prevDateRange = dateFormatUtil.getDateRange(granularity, new Long(prevCompareTo));
        Map<String, LocalDate> prevToPreviousDateRange = dateFormatUtil.getDateRange(granularity, new Long(prevToPrevCompareTo));

        if(currentDateRange == null || (currentDateRange != null && currentDateRange.size() ==0)){
            logger.error("Granularity not supported. Granularity: {}",granularity);
            StringBuilder sb = new StringBuilder("Supported granularity: ");
            sb = sb.append(Constants.GRANULARITY_YEARLY).append(",").append(Constants.GRANULARITY_MONTHLY).append(",")
                    .append(Constants.GRANULARITY_QUARTERLY).append(",").append(Constants.GRANULARITY_WEEKLY).append(",")
                    .append(Constants.GRANULARITY_DAILY);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(sb);
        }
        logger.debug("Request to get spend-overview-detail report. organization id: {}, start date:{}, end date id: {}, cloud: {}", orgId, currentDateRange.get("startDate").toString(), currentDateRange.get("endDate").toString(), cloud);
        List<SpendOverviewDetailReportObj> list = queryService.getSpendOverviewDetailReport(orgId, serviceCategory, currentDateRange.get("startDate").toString(), currentDateRange.get("endDate").toString(), prevDateRange.get("startDate").toString(), prevDateRange.get("endDate").toString(), prevToPreviousDateRange.get("startDate").toString(), prevToPreviousDateRange.get("endDate").toString(), cloud);
        Map<String, Object> response = new HashMap<>();
        response.put("report","SPEND OVERVIEW DETAIL");
        response.put("data", (list == null || (list != null && list.size() ==0)) ? Collections.emptyList() : list);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Object> getTopUsedServiceDetailReport(Long orgId, String cloud, String granularity, Long compareTo) {
        Map<String, LocalDate> currentDateRange = dateFormatUtil.getDateRange(granularity, compareTo);
        int prevCompareTo = (Math.abs(compareTo.intValue())+1);
        if (compareTo < 0){
            prevCompareTo = prevCompareTo * -1;
        }

        int prevToPrevCompareTo = (Math.abs(prevCompareTo)+1);
        if (prevCompareTo < 0){
            prevToPrevCompareTo = prevToPrevCompareTo * -1;
        }

        Map<String, LocalDate> prevDateRange = dateFormatUtil.getDateRange(granularity, new Long(prevCompareTo));
        Map<String, LocalDate> prevToPreviousDateRange = dateFormatUtil.getDateRange(granularity, new Long(prevToPrevCompareTo));

        if(currentDateRange == null || (currentDateRange != null && currentDateRange.size() ==0)){
            logger.error("Granularity not supported. Granularity: {}",granularity);
            StringBuilder sb = new StringBuilder("Supported granularity: ");
            sb = sb.append(Constants.GRANULARITY_YEARLY).append(",").append(Constants.GRANULARITY_MONTHLY).append(",")
                    .append(Constants.GRANULARITY_QUARTERLY).append(",").append(Constants.GRANULARITY_WEEKLY).append(",")
                    .append(Constants.GRANULARITY_DAILY);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(sb);
        }
        logger.debug("Request to get top-used-service-detail report. organization id: {}, start date:{}, end date id: {}, cloud: {}", orgId, currentDateRange.get("startDate").toString(), currentDateRange.get("endDate").toString(), cloud);
        List<TopUsedServicesDetailReportObj> list = queryService.getTopUsedServiceDetailReport(orgId, currentDateRange.get("startDate").toString(), currentDateRange.get("endDate").toString(), prevDateRange.get("startDate").toString(), prevDateRange.get("endDate").toString(), prevToPreviousDateRange.get("startDate").toString(), prevToPreviousDateRange.get("endDate").toString(), cloud);
        Map<String, Object> response = new HashMap<>();
        response.put("report","TOP USED SERVICE DETAIL");
        response.put("data", (list == null || (list != null && list.size() ==0)) ? Collections.emptyList() : list);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Object> getCostTopAccountsDetailReport(Long orgId, String cloud, String granularity, Long compareTo, Long noOfRecords, String order) {
        Map<String, LocalDate> currentDateRange = dateFormatUtil.getDateRange(granularity, compareTo);
        int prevCompareTo = (Math.abs(compareTo.intValue())+1);
        if (compareTo < 0){
            prevCompareTo = prevCompareTo * -1;
        }

        int prevToPrevCompareTo = (Math.abs(prevCompareTo)+1);
        if (prevCompareTo < 0){
            prevToPrevCompareTo = prevToPrevCompareTo * -1;
        }

        Map<String, LocalDate> prevDateRange = dateFormatUtil.getDateRange(granularity, new Long(prevCompareTo));
        Map<String, LocalDate> prevToPreviousDateRange = dateFormatUtil.getDateRange(granularity, new Long(prevToPrevCompareTo));

        if(currentDateRange == null || (currentDateRange != null && currentDateRange.size() ==0)){
            logger.error("Granularity not supported. Granularity: {}",granularity);
            StringBuilder sb = new StringBuilder("Supported granularity: ");
            sb = sb.append(Constants.GRANULARITY_YEARLY).append(",").append(Constants.GRANULARITY_MONTHLY).append(",")
                    .append(Constants.GRANULARITY_QUARTERLY).append(",").append(Constants.GRANULARITY_WEEKLY).append(",")
                    .append(Constants.GRANULARITY_DAILY);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(sb);
        }
        logger.debug("Request to get cost-of-top-accounts-detail report. organization id: {}, start date:{}, end date id: {}, cloud: {}", orgId, currentDateRange.get("startDate").toString(), currentDateRange.get("endDate").toString(), cloud);

        List<CostOfTopAccountsDetailReportObj>  list = queryService.getCostTopAccountsDetailReport(orgId, cloud, noOfRecords, order, currentDateRange.get("startDate").toString(), currentDateRange.get("endDate").toString(), prevDateRange.get("startDate").toString(), prevDateRange.get("endDate").toString(), prevToPreviousDateRange.get("startDate").toString(), prevToPreviousDateRange.get("endDate").toString());
        Map<String, Object> response = new HashMap<>();
        response.put("report","COST OF TOP ACCOUNTS DETAIL");
        response.put("data", (list == null || (list != null && list.size() ==0)) ? Collections.emptyList() : list);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Object> getPotentialSavingsDetailSummaryReport(Long orgId, String serviceCategory, String cloud, String granularity, Long compareTo) {
        logger.debug("Request to get potential-savings-detail-summary report. organization id: {}, serviceCategory:{}, cloud: {}, granularity: {}, compareTo: {}", orgId, serviceCategory, cloud, granularity, compareTo);
        double current = RandomUtil.getRandomLong(80000, 100000)*1.0;
        double prev = RandomUtil.getRandomLong(100000, 120000)*1.0;
        double prevToPrev = RandomUtil.getRandomLong(80000, 120000)*1.0;
        double forecast = RandomUtil.getRandomLong(60000, 80000)*1.0;

        double currentVariance = ((current-prev)/current)*100.0;
        DecimalFormat df = new DecimalFormat("#.##");
        PotentialSavingsDetailSummaryReportObj thisMonth = PotentialSavingsDetailSummaryReportObj
                .build(1L, serviceCategory, "This Month Savings", String.valueOf(current), String.valueOf(prev), df.format(currentVariance));

        double futureVariance = ((forecast-current)/forecast)*100.0;
        PotentialSavingsDetailSummaryReportObj futureMonth = PotentialSavingsDetailSummaryReportObj
                .build(2L, serviceCategory, "Forecasting Savings", String.valueOf(forecast), String.valueOf(current), df.format(futureVariance));

        double prevVariance = ((prev-prevToPrev)/prev)*100.0;
        PotentialSavingsDetailSummaryReportObj lastMonth = PotentialSavingsDetailSummaryReportObj
                .build(3L, serviceCategory, "Last Month savings", String.valueOf(prev), String.valueOf(prevToPrev), df.format(prevVariance));

        Map<String, Object> response = new HashMap<>();
        response.put("report","POTENTIAL SAVINGS DETAIL - SUMMARY");
        List list = new ArrayList();
        list.add(thisMonth);
        list.add(futureMonth);
        list.add(lastMonth);
        response.put("data", (list == null || (list != null && list.size() ==0)) ? Collections.emptyList() : list);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Object> getPotentialSavingsDetailTotalSavingReport(Long orgId, String serviceCategory, String cloud, String granularity, Long compareTo) {
        int min = 10;
        int max = 60;
        logger.debug("Request to get potential-savings-detail-total-saving report. organization id: {}, serviceCategory:{}, cloud: {}, granularity: {}, compareTo: {}", orgId, serviceCategory, cloud, granularity, compareTo);

        PotentialSavingsReportObj spotInstance = new PotentialSavingsReportObj();
        spotInstance.setId(1L);
        spotInstance.setInstanceType("SPOT");
        spotInstance.setTotal(String.valueOf(RandomUtil.getRandomLong(min, max)));

        PotentialSavingsReportObj reservedInstance = new PotentialSavingsReportObj();
        reservedInstance.setId(2L);
        reservedInstance.setInstanceType("RESERVED");
        reservedInstance.setTotal(String.valueOf(RandomUtil.getRandomLong(min, max)));

        PotentialSavingsReportObj othersInstance = new PotentialSavingsReportObj();
        othersInstance.setId(3L);
        othersInstance.setInstanceType("OTHERS");
        othersInstance.setTotal(String.valueOf(RandomUtil.getRandomLong(min, max)));

        PotentialSavingsReportObj rightSizeInstance = new PotentialSavingsReportObj();
        rightSizeInstance.setId(4L);
        rightSizeInstance.setInstanceType("RIGHTSIZE");
        rightSizeInstance.setTotal(String.valueOf(RandomUtil.getRandomLong(min, max)));

        PotentialSavingsReportObj savingPlanInstance = new PotentialSavingsReportObj();
        savingPlanInstance.setId(5L);
        savingPlanInstance.setInstanceType("SAVING_PLAN");
        savingPlanInstance.setTotal(String.valueOf(RandomUtil.getRandomLong(min, max)));

        PotentialSavingsReportObj currentTotal = new PotentialSavingsReportObj();
        currentTotal.setId(6L);
        currentTotal.setInstanceType("CURRENT_TOTAL");
        Long ct = RandomUtil.getRandomLong(80000, 100000);
        currentTotal.setTotal(String.valueOf(ct));

        List<PotentialSavingsReportObj> list = new ArrayList<>();
        list.add(spotInstance);
        list.add(reservedInstance);
        list.add(othersInstance);
        list.add(rightSizeInstance);
        list.add(savingPlanInstance);
        list.add(currentTotal);

        Map<String, Object> response = new HashMap<>();
        response.put("report","POTENTIAL SAVINGS DETAIL - TOTAL SAVINGS");
        response.put("data", (list == null || (list != null && list.size() ==0)) ? Collections.emptyList() : list);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Object> getPotentialSavingsDetailmonthlySavingReport(Long orgId, String serviceCategory, String cloud, String granularity, Long compareTo) {
        logger.debug("Request to get potential-savings-detail-monthly-saving report. organization id: {}, serviceCategory:{}, cloud: {}, granularity: {}, compareTo: {}", orgId, serviceCategory, cloud, granularity, compareTo);
        Map<String, LocalDate> currentDateRange = dateFormatUtil.getDateRange(granularity, compareTo);
        List<String> dateList = DateFormatUtil.getYearMonthBetweenDates(currentDateRange.get("startDate"), LocalDate.now());
        int min = 100000;
        int max = 180000;
        Map<String, Object> data;
        List<Map<String, Object>> list = new ArrayList<>();

        for(String date: dateList){
            data = new HashMap<>();
            data.put("date", date);
            data.put("total", RandomUtil.getRandomLong(min, max));
            list.add(data);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("report","POTENTIAL SAVINGS DETAIL - MONTHLY SAVINGS");
        response.put("data", list);
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<Object> getPotentialSavingsDetailTopRiRecommendationReport(Long orgId, String serviceCategory, String cloud, String granularity, Long compareTo) {
        logger.debug("Request to get potential-savings-top-ri-recommendation report. organization id: {}, serviceCategory:{}, cloud: {}, granularity: {}, compareTo: {}", orgId, serviceCategory, cloud, granularity, compareTo);
        Map<String, LocalDate> currentDateRange = dateFormatUtil.getDateRange(granularity, compareTo);
        List<PotentialSavingsDetailRiRecommendReportObj> list =  queryService.getPotentialSavingsDetailTopRiRecommendationReport(orgId, serviceCategory, currentDateRange.get("startDate").toString(), currentDateRange.get("endDate").toString(), cloud);
        Map<String, Object> response = new HashMap<>();
        response.put("report","POTENTIAL SAVINGS DETAIL - TOP RI RECOMMENDATIONS");
        response.put("data", (list == null || (list != null && list.size() ==0)) ? Collections.emptyList() : list);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Object> getSpendOverviewElementDetailReport(Long orgId, String serviceCategory, String elementType, String cloud, String granularity, Long compareTo) {
        logger.debug("Request to get spend-overview-element-detail report. organization id: {}, serviceCategory:{}, cloud: {}, granularity: {}, compareTo: {}", orgId, serviceCategory, cloud, granularity, compareTo);
        Map<String, LocalDate> currentDateRange = dateFormatUtil.getDateRange(granularity, compareTo);
        List<SpendOverviewElementDetailReportObj> list = queryService.getSpendOverviewElementDetailReport(orgId, serviceCategory, elementType, cloud, currentDateRange.get("startDate").toString(), currentDateRange.get("endDate").toString());
        Map<String, Object> response = new HashMap<>();
        response.put("report","SPEND OVERVIEW ELEMENT DETAIL");
        response.put("data", (list == null || (list != null && list.size() ==0)) ? Collections.emptyList() : list);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Object> getSpendOverviewElementSummaryReport(Long orgId, String serviceCategory, String elementType, String cloud, String granularity, Long compareTo) {
        logger.debug("Request to get spend-overview-element-summary report. organization id: {}, serviceCategory:{}, cloud: {}, granularity: {}, compareTo: {}", orgId, serviceCategory, cloud, granularity, compareTo);
        Map<String, LocalDate> currentDateRange = dateFormatUtil.getDateRange(granularity, compareTo);
        int prevCompareTo = (Math.abs(compareTo.intValue())+1);
        if (compareTo < 0){
            prevCompareTo = prevCompareTo * -1;
        }
        Map<String, LocalDate> prevDateRange = dateFormatUtil.getDateRange(granularity, new Long(prevCompareTo));

        List<SpendOverviewElementSummaryReportObj> list = queryService.getSpendOverviewElementSummaryReport(orgId, serviceCategory, elementType, cloud, currentDateRange.get("startDate").toString(), currentDateRange.get("endDate").toString(), prevDateRange.get("startDate").toString(), prevDateRange.get("endDate").toString());
        Map<String, Object> response = new HashMap<>();
        response.put("report","SPEND OVERVIEW ELEMENT SUMMARY");
        response.put("data", (list == null || (list != null && list.size() ==0)) ? Collections.emptyList() : list);
        return ResponseEntity.ok(response);

    }
}
