package com.synectiks.asset.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.identitymanagement.model.GetUserResult;
import com.amazonaws.services.organizations.AWSOrganizations;
import com.amazonaws.services.organizations.AWSOrganizationsClientBuilder;
import com.amazonaws.services.organizations.model.DescribeOrganizationRequest;
import com.amazonaws.services.organizations.model.DescribeOrganizationResult;
import com.amazonaws.services.organizations.model.DescribeOrganizationalUnitRequest;
import com.amazonaws.services.organizations.model.DescribeOrganizationalUnitResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityRequest;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.business.domain.Dashboard;
import com.synectiks.asset.business.domain.DashboardMeta;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Map;

public class Utils {

	private static final Logger logger = LoggerFactory.getLogger(Utils.class);
	
	public static AmazonS3 getAmazonS3Client(String accessKey, String secretKey, String s3Region) {
		logger.info("Getting AWS S3 client");
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		AmazonS3ClientBuilder s3ClientBuilder = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials));
		s3ClientBuilder.setRegion(s3Region);
		AmazonS3 s3Client = s3ClientBuilder.build();
		logger.info("AWS connection established");
		return s3Client;
	}

	@SuppressWarnings("deprecation")
	public static AmazonIdentityManagement getAmazonIdentityManagement(String accessKey, String secretKey, String s3Region) {
		logger.info("Getting AWS AmazonIdentityManagementClient");
//		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
//		AmazonIdentityManagementClient iamClient = new AmazonIdentityManagementClient(credentials);
		AmazonIdentityManagement aim = AmazonIdentityManagementClient.builder()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.withRegion(s3Region)
				.build();
		return aim;
	}
	
	public static GetUserResult getAWSUser(String accessKey, String secretKey, String s3Region) {
	    AmazonIdentityManagement aim = getAmazonIdentityManagement(accessKey, secretKey, s3Region);
	    return aim.getUser();
	}
	
	public static AWSSecurityTokenService getAWSSecurityTokenService(String accessKey, String secretKey, String s3Region) {
		AWSSecurityTokenService awsSecurityTokenService = AWSSecurityTokenServiceClientBuilder.standard().withCredentials(
	            new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey))).withRegion(s3Region).build();
	    return awsSecurityTokenService;
	}
	
	public static String getAwsAccountId(String accessKey, String secretKey, String s3Region) {
	    GetCallerIdentityResult callerIdentity = getAWSSecurityTokenService(accessKey, secretKey, s3Region)
	    		.getCallerIdentity(new GetCallerIdentityRequest());
	    return callerIdentity.getAccount();
	}
	
	
	public static AWSOrganizations getAwsOrganizationClient(String accessKey, String secretKey, String s3Region) {
		logger.info("Getting AWS organization client");
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		AWSOrganizationsClientBuilder s3ClientBuilder = AWSOrganizationsClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials));
		s3ClientBuilder.setRegion(s3Region);
		return s3ClientBuilder.build();
	}
	
	public static com.amazonaws.services.organizations.model.Organization getAwsOrganization(String accessKey, String secretKey, String s3Region) {
		AWSOrganizations awsOrg = getAwsOrganizationClient(accessKey, secretKey, s3Region);
		DescribeOrganizationResult r = awsOrg.describeOrganization(new DescribeOrganizationRequest());
		com.amazonaws.services.organizations.model.Organization org = r.getOrganization();
		return org;
	}
	
	public static com.amazonaws.services.organizations.model.OrganizationalUnit getAwsOrganizationUnit(String accessKey, String secretKey, String s3Region) {
		AWSOrganizations awsOrg = getAwsOrganizationClient(accessKey, secretKey, s3Region);
		DescribeOrganizationalUnitResult r = awsOrg.describeOrganizationalUnit(new DescribeOrganizationalUnitRequest());
		com.amazonaws.services.organizations.model.OrganizationalUnit ou = r.getOrganizationalUnit();
		return ou;
	}
	

	public static Dashboard getDashboardFromAwsS3(Map<String, String> object, ObjectMapper mapper, String bucket, AmazonS3 s3Client) throws IOException {
		logger.info("Downloading dashboard json from AWS");
		
		if (StringUtils.isBlank(object.get("cloudType")) || StringUtils.isBlank(object.get("elementType")) ||
				StringUtils.isBlank(object.get("tenantId")) || StringUtils.isBlank(object.get("accountId")) ||
				StringUtils.isBlank(object.get("inputType")) || StringUtils.isBlank(object.get("fileName")) ||
				StringUtils.isBlank(object.get("dataSource"))) {
			logger.error("Mandatory fields missing");
			return null;
		}
		
		String cloudType = object.get("cloudType");
		String elementType = object.get("elementType");
		String tenantId = object.get("tenantId");
		String accountId = object.get("accountId");
		String inputType = object.get("inputType");
		String fileName = object.get("fileName");
		String dataSource = object.get("dataSource");
		
//		Accounts account = accountsService.getAccountByAccountAndTenantId(accountId, tenantId);
//		
//		AmazonS3 s3Client = AwsUtils.getAmazonS3Client(account.getAccessKey(), account.getSecretKey(), account.getRegion());
		S3Object file = null;
		try {
			file = s3Client.getObject(bucket, fileName);
		}catch(AmazonS3Exception e) {
			logger.error("AmazonS3Exception. Either given aws bucket: "+bucket+" or given file : "+fileName+" not found. "+e.getMessage());
			throw e;
		}
		
		
		Dashboard dashboard = new Dashboard();
		
		dashboard.setCloudName(cloudType);
		dashboard.setElementType(elementType);
		dashboard.setTenantId(tenantId);
		dashboard.setAccountId(accountId);
		dashboard.setInputType(inputType);
		dashboard.setFileName(fileName);
		dashboard.setInputSourceId(dataSource);
		dashboard.setTitle(cloudType+"_"+elementType+"_"+dataSource);
		dashboard.setSlug(cloudType+"_"+elementType+"_"+dataSource);
		String data = getTextFromInputStream(file.getObjectContent());
		
//		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();
		
		ObjectNode dataNode = (ObjectNode)mapper.readTree(data);
		for(JsonNode panel : dataNode.get("panels")) {
			ObjectNode oPanel = (ObjectNode)panel;
			oPanel.put("datasource", dataSource);
			arrayNode.add(oPanel);
        }
		dataNode.put("panels", arrayNode);
		data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataNode);
		logger.debug("Datasource updated. Json : "+ data);
		
		String uid = RandomStringUtils.random(8, true, true);
		dashboard.setUid(uid);
		dashboard.setData(data);
		
		DashboardMeta meta = new DashboardMeta();
		meta.setSlug(dashboard.getSlug());
		
		dashboard.setDashboardMeta(meta);
		return dashboard;
	}
	
	private static String getTextFromInputStream(InputStream input) throws IOException {
        // Read the text input stream one line at a time and display each line.
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuffer sb = new StringBuffer();
		String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append(" ");
//        	logger.debug(sb.toString());
        }
        return sb.toString();
	}
	
	public static String getRandomStringUuid() {
		return RandomStringUtils.randomAlphanumeric(9);
	}

	public static void offsetDateComparison() {
		OffsetDateTime now = OffsetDateTime.now();
		System.out.println("current time : "+now);
//		Long x = now.toOffsetTime(). + (6*60*1000);
		OffsetDateTime ndt = now.plus(Duration.ofMillis(6*60*1000));
		System.err.println("future time: "+ndt);
		
		boolean n = ndt.compareTo(now) > 0;
		System.out.println(n);
	}
	
	public static void decimalFormat() {
		DecimalFormat df = new DecimalFormat("0.00");
		String p = "78.987";
		String d = "9";
		double dn = Double.parseDouble(p);
		System.out.println(p + ", dn: "+ dn+", formated: "+ df.format(dn));
	}
	
	public static void sringFormat() {
		String p = "78.987";
		String d = "9";
		double dp = Double.parseDouble(p);
		double dc = Double.parseDouble(d);
		double x = (dc/100*dp);
		System.out.println(x);
		System.out.println(p + ", dp: "+ dp+", formated: "+ String.format("%.2f", x));
	}
	
	public static void bigDecimalWithoutRounding() {
		String p = "78.987";
		String d = "9";
		BigDecimal dp = new BigDecimal(p);
		BigDecimal dc = new BigDecimal(d);
		System.out.println("price: "+dp+", % discount: "+dc);
		double x = (dc.doubleValue()/100*dp.doubleValue());
		BigDecimal bd=new BigDecimal(x);
		System.out.println("Actual double: "+x);
		System.out.println("Actual bigdecimal: "+bd);
		BigDecimal nbd = new BigDecimal(bd.toPlainString()).setScale(2, RoundingMode.FLOOR);
		System.out.println("Bigdecimal after format: "+nbd);

	}
	
	public static void calculateFinalPrice(String price, String discountPercent, int roundScale, RoundingMode rm) {
		BigDecimal dp = new BigDecimal(price);
		BigDecimal dc = new BigDecimal(discountPercent);
//		System.out.println("base price : "+dp.toPlainString());
//		System.out.println("Percetage : "+dc.toPlainString());
		BigDecimal pc = dc.divide(new BigDecimal(100), roundScale, rm);
		System.out.println("percentage after divide: "+pc.toPlainString());
		BigDecimal pcPrice = pc.multiply(dp);
		System.out.println("percent amount : "+pcPrice.toPlainString());
//		BigDecimal fPrice = dp.add(pcPrice);
//		System.out.println("Bigdecimal after format: "+fPrice);
		
//		BigDecimal discount = new BigDecimal(new BigDecimal((dc.doubleValue()/100*dp.doubleValue())).toPlainString()).setScale(roundScale, rm);
//		System.out.println("With double : "+discount);
	}
	
	public static void main(String a[]) {
//		bigDecimalWithoutRounding();
		calculateFinalPrice("20","18",2,RoundingMode.FLOOR);
//		String roleArn = "arn:aws:iam::657907747545:role/CrossAccount";
//		String arn[] = roleArn.split(":");
//		System.out.println(arn[4]);
	}
}
