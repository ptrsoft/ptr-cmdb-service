package com.synectiks.asset.service;

import com.synectiks.asset.domain.query.EnvironmentCountQueryObj;
import com.synectiks.asset.domain.query.EnvironmentQueryObj;
import com.synectiks.asset.domain.query.EnvironmentSummaryQueryObj;
import com.synectiks.asset.repository.QueryRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QueryService {

	private static final Logger logger = LoggerFactory.getLogger(QueryService.class);
    @Autowired
    private QueryRepository queryRepository;

   public List<EnvironmentCountQueryObj> getEnvironmentCounts(Long orgId){
       logger.debug("Getting cloud wise landing zone and their resource counts for an organization. Org Id: {}", orgId);
       return queryRepository.getCount(orgId);
   }

    public EnvironmentCountQueryObj getEnvironmentCounts(Long orgId, String cloud) {
        logger.debug("Getting cloud wise landing zone and their resource counts for an organization and given cloud. Org Id: {}, Cloud: {}", orgId,cloud);
        return queryRepository.getCount(cloud, orgId);
    }

    public List<EnvironmentQueryObj> getEnvironmentSummary(Long orgId)  {
        logger.debug("Getting organization wise environment summary. Org id: {}", orgId);
        List<EnvironmentSummaryQueryObj> list = queryRepository.getEnvironmentSummary(orgId);
        return filterEnvironmentSummary(list);
    }

    public List<EnvironmentQueryObj> getEnvironmentSummary(Long orgId, String cloud)  {
        logger.debug("Getting organization and cloud wise environment summary. Org Id: {}, Cloud: {}", orgId,cloud);
        List<EnvironmentSummaryQueryObj> list = queryRepository.getEnvironmentSummary(orgId, cloud);
        return filterEnvironmentSummary(list);
    }

    private List<EnvironmentQueryObj> filterEnvironmentSummary(List<EnvironmentSummaryQueryObj> list) {
        Set<String> cloudSet = list.stream().map(EnvironmentSummaryQueryObj::getCloud).collect(Collectors.toSet());
        List<EnvironmentQueryObj> environmentDtoList = new ArrayList<>();
        for (Object obj: cloudSet){
            String cloudName = (String)obj;
            logger.debug("Getting list for cloud: {}", cloudName);
            List<EnvironmentSummaryQueryObj> filteredList = list.stream().filter(l -> !StringUtils.isBlank(l.getCloud()) && l.getCloud().equalsIgnoreCase(cloudName)).collect(Collectors.toList());
            EnvironmentQueryObj dto = EnvironmentQueryObj.builder()
                    .cloud(cloudName)
                    .environmentSummaryList(filteredList)
                    .build();
            environmentDtoList.add(dto);
        }
        return environmentDtoList;
    }
}
