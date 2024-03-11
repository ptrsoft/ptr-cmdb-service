package com.synectiks.asset.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

/**
 * Application constants.
 */
public final class Constants {

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ACTIVE = "ACTIVE";
    public static final String DEACTIVE = "DEACTIVE";
    public static final String DEFAULT_AWS_REGION = "us-east-1";
    public static final String DEFAULT_AWS_BUCKET = "xformation.synectiks.com";
    public static final String STATUS_READY_TO_ENABLE = "READY_TO_ENABLE";
    public static final String STATUS_ENABLED = "ENABLED";

    public static final String INPUT_TYPE_PERFORMANCE = "Performance";
    public static final String INPUT_TYPE_AVAILABILITY = "Availability";

    public static final String CLOUD_TYPE_AWS = "AWS";
    public static final String VPC = "VPC";
//    public static final Map<String, Map<String, Map<String, List<Dashboard>> > > ENABLED_DASHBOARD_CACHE = new HashMap<String, Map<String, Map<String, List<Dashboard>> > >();

    public static final List<String> AWS_DISCOVERED_ASSETS = new ArrayList<>();

    public static final String SERVICE_ID = "serviceId";
    public static final String STATUS_UNPAID = "UNPAID";
    public static final String SERVICE_FIREWALL = "Firewall";
    public static final String SERVICE_LOAD_BALANCER = "Load Balancer";
    public static final String SERVICE_GATEWAY_SERVICE = "Gateway Services";
    public static final String SERVICE_BUSINESS_SERVICE = "Business Services";
    public static final String APP_SERVICES = "APP";
    public static final String DATA_SERVICES = "DATA";

    public static final String WEB_SERVICES = "WEB";

    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_MM_DD_YYYY = "MM-dd-yyyy";
    public static final String DATE_FORMAT_DD_MM_YYYY = "dd-MM-yyyy";
    public static final String DATE_FORMAT_DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy H:mm:s";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";
    
    
    public static final String DEFAULT_DATE_FORMAT =  DATE_FORMAT_YYYY_MM_DD;
    public static final String DEFAULT_DATETIME_FORMAT =  DATE_FORMAT_YYYY_MM_DD_HH_MM_SS;

    public static final String PERFORMANCE  = "performance";
    public static final String AVAILABILITY = "availability";
    public static final String RELIABILITY  = "reliability";
    public static final String ENDUSAGE     = "endusage";
    public static final String SECURITY     = "security";
    public static final String COMPLIANCE   = "compliance";
    public static final String ALERTS       = "alerts";

    public static final String DASHBOARD_TYPE[] = {PERFORMANCE,AVAILABILITY, RELIABILITY, ENDUSAGE, SECURITY, COMPLIANCE, ALERTS};
	public static final List<String> VIEW_JSON_KEYS = Arrays.asList("id", "slug", "uid", "cloudElement", "accountId", "url", "cloudElementId"
			, "associatedOU","associatedDept","associatedProduct","associatedEnv","serviceType","serviceNature","serviceName","serviceInstance");
	public static final List<String> DASHBOARD_TYPE_KEYS = Arrays.asList(DASHBOARD_TYPE);

	private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
			.appendOptional(DateTimeFormatter.ISO_DATE_TIME).appendOptional(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
			.appendOptional(DateTimeFormatter.ISO_INSTANT)
			.appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SX"))
			.appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX"))
			.appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toFormatter().withZone(ZoneOffset.UTC);

	public static OffsetDateTime parseDateTimeString(String str) {
		return ZonedDateTime.from(DATE_TIME_FORMATTER.parse(str)).toOffsetDateTime();
	}

	public static ObjectMapper instantiateMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		SimpleModule module = new SimpleModule();
		module.addDeserializer(OffsetDateTime.class, new JsonDeserializer<OffsetDateTime>() {
			@Override
			public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
					throws IOException, JsonProcessingException {
				String value = jsonParser.getText();
				return parseDateTimeString(value);
			}
		});
		mapper.registerModule(module);
		return mapper;
	}

    public static String PROXY_GRAFANA_BASE_API = "";
    public static String PROXY_GRAFANA_USER = "";
    public static String PROXY_GRAFANA_PASSWORD = "";
//    public static String IMPORT_DASHBOARD_TO_GRAFANA_API = "";
    public static String DEPARTMENT_WISE_ANALYTICS_CACHE_KEY ="department-wise-analytics";
    public static String PRODUCT_CACHE_KEY ="product";
    public static Map<String, Object> cache = new HashMap<>();
    public static String AVAILABLE_CLOUDS [] = {"AWS","AZURE","GCP","OTHER"};
    public static final String AWS = "AWS";
    public static final String AZURE = "AZURE";
    public static final String GCP = "GCP";

    public static final DecimalFormat decfor = new DecimalFormat("0.00");
    public static final String DEFAULT_TIMEZONE = "Asia/Kolkata";
    public static final String ORGANIZATION_ID = "organizationId";
    public static final String ORGANIZATION_NAME = "organizationName";
    public static final String DEPARTMENT_ID = "departmentId";
    public static final String DEPARTMENT_NAME = "departmentName";
    public static final String PRODUCT_ID = "productId";
    public static final String PRODUCT_NAME = "productName";
    public static final String ACCOUNT_ID = "accountId";
    public static final String LANDING_ZONE = "landingZone";
    public static final String CREATED_ON = "createdOn";
    public static final String UPDATED_ON = "updatedOn";
    
    public static final String DEPLOYMENT_ENVIRONMENT_ID = "deploymentEnvironmentId";
    public static final String DEPLOYMENT_ENVIRONMENT_NAME = "deploymentEnvironmentName";
    public static final String SERVICE_NAME = "serviceName";
    public static final String SERVICE_TYPE = "type";
    public static final String SERVICE_NATURE = "serviceNature";
    public static final String SERVICES_ID = "servicesId";
    public static final String TAGGED = "Tagged";
    public static final String DISCOVERED_ASSET_ID = "discoveredAssetId";
    public static final String SERVICE_ALLOCATION_ID = "serviceAllocationId";
    public static final String CLOUD_ENVIRONMENT_ID = "cloudEnvironmentId";
    
    public static final String MODULE_ID = "moduleId";

//    public static final String AWS_PROVIDER_SECRET_KEY = "aws-provider-secret-key";
//    public static final String AWS_PROVIDER_ACCESS_KEY = "aws-provider-access-key";
//    public static final String AWS_PROVIDER_ZONE = "aws-provider-zone";

    public static final String VAULT_HEADER = "X-Vault-Token";
    public static String VAULT_SECRET_VERSION = "";
    public static String VAULT_SECRET_ENGINE = "";
    public static String VAULT_URL = "";
    public static String VAULT_ROOT_TOKEN = "";
    public static String VAULT_LANDING_ZONE_PATH = "";
    public static String VAULT_PROVIDER_AWS_CREDS_KEY = "";

    public static final String ACCESS_KEY = "accessKey";
    public static final String SECRET_KEY = "secretKey";

    public static final String CLOUD = "cloud";

    public static final String ZONE = "zone";
    public static final String REGION = "region";
    public static final String EXTERNAL_ID = "externalId";
    public static final String CROSS_ACCOUNT_ROLE_ARN = "crossAccountRoleArn";
    public static final String DISPLAY_NAME = "displayName";

    public static String AWSX_API_BASE_URL = "";
    public static String AWSX_API_APPCONFIG_URL = "";

    public static String AWSX_API_LAMBDA_URL = "";

    public static String AWSX_API_EKS_URL = "";
    public static final String AWS_LAMBDA_FUNCTION_KEY = "AWS::Lambda::Function";

    public static final String CLOUD_ELEMENTS = "CLOUD_ELEMENTS";

    public static final String APP_CONFIG_SUMMARY = "APP_CONFIG_SUMMARY";
    public static final String LAMBDA = "LAMBDA";
    public static final String EKS = "EKS";
    public static final String ECS = "ECS";
    public static final String S3 = "S3";

    public static final String EC2 = "EC2";
    public static final String RDS = "RDS";
    public static final String GLACIER = "GLACIER";
    public static final String CDN = "CDN";
    public static final String KMS = "KMS";

    public static final String KINESYS = "KINESYS";
    public static final String DYNAMODB = "DYNAMODB";
    public static final String WAF = "WAF";
    public static final String APIGATEWAY = "APIGATEWAY";
    public static final String LOADBALANCER = "LOADBALANCER";
    public static final String PERIPHERAL_SERVICES = "PERIPHERAL";

    public static final String SQL_DB = "SQL DB";
    public static final String NO_SQL_DB = "No SQL DB";
    public static final String SEARCH_DB = "Search DB";
    public static final String LEDGER_DB = "Ledger DB";
    public static final String CACHE_DB = "Cache DB";
    public static final String OBJECT_DB = "Object DB";
    public static final String GIT_DB = "Git DB";
    public static final String METRICS_DB = "Metrics DB";


    public static final String APPKUBE_DEPARTMENT = "APPKUBE_DEPARTMENT";
    public static final String APPKUBE_PRODUCT = "APPKUBE_PRODUCT";
    public static final String APPKUBE_ENV = "APPKUBE_ENV";
    public static final String APPKUBE_MODULE = "APPKUBE_MODULE";
    public static final String APPKUBE_SERVICE = "APPKUBE_SERVICE";

    public static final String SOA = "SOA";
    public static final String THREE_TIER = "3 Tier";

    public static final String TYPE_BUSINESS = "BUSINESS";
    public static final String TYPE_COMMON = "COMMON";

    public static final String COMPUTE = "COMPUTE";
    public static final String STORAGE = "STORAGE";
    public static final String NETWORK = "NETWORK";
    public static final String DATABASE = "DATABASE";
    public static final String OTHER = "OTHER";

    private Constants() {
    }
}
