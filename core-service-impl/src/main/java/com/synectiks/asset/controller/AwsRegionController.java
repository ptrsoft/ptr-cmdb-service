package com.synectiks.asset.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public final class AwsRegionController {
   
	private static final Logger logger = LoggerFactory.getLogger(AwsRegionController.class);
	
	public static final String AP_SOUTH_1 = "ap-south-1";

    public static final String EU_SOUTH_1 = "eu-south-1";

    public static final String US_GOV_EAST_1 = "us-gov-east-1";

    public static final String CA_CENTRAL_1 = "ca-central-1";

    public static final String EU_CENTRAL_1 = "eu-central-1";

    public static final String US_WEST_1 = "us-west-1";

    public static final String US_WEST_2 = "us-west-2";

    public static final String AF_SOUTH_1 = "af-south-1";

    public static final String EU_NORTH_1 = "eu-north-1";

    public static final String EU_WEST_3 = "eu-west-3";

    public static final String EU_WEST_2 = "eu-west-2";

    public static final String EU_WEST_1 = "eu-west-1";

    public static final String AP_NORTHEAST_3 = "ap-northeast-3";

    public static final String AP_NORTHEAST_2 = "ap-northeast-2";

    public static final String AP_NORTHEAST_1 = "ap-northeast-1";

    public static final String ME_SOUTH_1 = "me-south-1";

    public static final String SA_EAST_1 = "sa-east-1";

    public static final String AP_EAST_1 = "ap-east-1";

    public static final String CN_NORTH_1 = "cn-north-1";

    public static final String US_GOV_WEST_1 = "us-gov-west-1";

    public static final String AP_SOUTHEAST_1 = "ap-southeast-1";

    public static final String AP_SOUTHEAST_2 = "ap-southeast-2";

    public static final String US_ISO_EAST_1 = "us-iso-east-1";

    public static final String US_EAST_1 = "us-east-1";

    public static final String US_EAST_2 = "us-east-2";

    public static final String CN_NORTHWEST_1 = "cn-northwest-1";

    public static final String US_ISOB_EAST_1 = "us-isob-east-1";

    public static final String AWS_GLOBAL = "aws-global";

    public static final String AWS_CN_GLOBAL = "aws-cn-global";

    public static final String AWS_US_GOV_GLOBAL = "aws-us-gov-global";

    public static final String AWS_ISO_GLOBAL = "aws-iso-global";

    public static final String AWS_ISO_B_GLOBAL = "aws-iso-b-global";

    private static final List<String> AwsRegionControllerS = Collections.unmodifiableList(Arrays.asList(AP_SOUTH_1, EU_SOUTH_1, US_GOV_EAST_1,
            CA_CENTRAL_1, EU_CENTRAL_1, US_WEST_1, US_WEST_2, AF_SOUTH_1, EU_NORTH_1, EU_WEST_3, EU_WEST_2, EU_WEST_1,
            AP_NORTHEAST_3, AP_NORTHEAST_2, AP_NORTHEAST_1, ME_SOUTH_1, SA_EAST_1, AP_EAST_1, CN_NORTH_1, US_GOV_WEST_1,
            AP_SOUTHEAST_1, AP_SOUTHEAST_2, US_ISO_EAST_1, US_EAST_1, US_EAST_2, CN_NORTHWEST_1, US_ISOB_EAST_1, AWS_GLOBAL,
            AWS_CN_GLOBAL, AWS_US_GOV_GLOBAL, AWS_ISO_GLOBAL, AWS_ISO_B_GLOBAL));

    public static List<String> getAwsRegionControllers() {
        return AwsRegionControllerS;
    }

    @GetMapping("/getAwsRegions")
	public List<String> getAwsRegions(@RequestParam Map<String, String> object) {
		logger.info("Request to get list of aws regions");
		return getAwsRegionControllers();
	}
}
