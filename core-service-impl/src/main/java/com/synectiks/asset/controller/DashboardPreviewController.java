package com.synectiks.asset.controller;

import com.synectiks.asset.business.domain.Dashboard;
import com.synectiks.asset.business.service.AwsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DashboardPreviewController {
	
	private static final Logger logger = LoggerFactory.getLogger(DashboardPreviewController.class);
	
	@Autowired
	AwsService awsService;
	
	@GetMapping("/dashboard/preview")
	public ResponseEntity<Dashboard> previewDashboard(@RequestParam Map<String, String> object) {
		logger.info("Request to get dashboard json");
		try {
			Dashboard dashboard = awsService.getDashboardFromAwsS3(object);
			return ResponseEntity.status(HttpStatus.OK).body(dashboard);
		}catch(Exception e) {
			logger.error("Exception in getting dashboard json: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}
	
}
