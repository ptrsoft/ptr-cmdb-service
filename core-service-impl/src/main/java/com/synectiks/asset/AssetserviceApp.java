package com.synectiks.asset;

import com.synectiks.asset.config.ApplicationProperties;
import com.synectiks.asset.config.Constants;
import io.github.jhipster.config.DefaultProfileUtil;
import io.github.jhipster.config.JHipsterConstants;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.apache.bcel.classfile.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
public class AssetserviceApp {

    private static final Logger log = LoggerFactory.getLogger(AssetserviceApp.class);

    private static ConfigurableApplicationContext ctx = null;

    private final Environment env;

    public AssetserviceApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes assetservice.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AssetserviceApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
//        Environment env = app.run(args).getEnvironment();

        ctx  = app.run(args);
        Environment env = ctx.getEnvironment();
        initConstants(env);
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}{}\n\t" +
                "External: \t{}://{}:{}{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles());
    }

    /**
	 * Utility method to get bean from spring context.
	 * @param cls
	 * @return
	 */
	public static <T> T getBean(Class<T> cls) {
		return ctx.getBean(cls);
	}

	public static Environment getEnvironment() {
		return ctx.getEnvironment();
	}

	public static int getServerPort() {
		return Integer.parseInt(ctx.getEnvironment().getProperty("server.port"));
	}

	private static void initConstants(Environment env) {
        Constants.PROXY_GRAFANA_BASE_API = env.getProperty("proxy-grafana.protocol")+"://"+env.getProperty("proxy-grafana.address")+":"+env.getProperty("proxy-grafana.port")+"/api";
        Constants.PROXY_GRAFANA_USER = env.getProperty("proxy-grafana.grafana-user");
        Constants.PROXY_GRAFANA_PASSWORD = env.getProperty("proxy-grafana.grafana-password");

        Constants.VAULT_SECRET_VERSION = env.getProperty("hashicorp-vault.secret-version");
        Constants.VAULT_SECRET_ENGINE = env.getProperty("hashicorp-vault.secret-engine");
        Constants.VAULT_URL = env.getProperty("hashicorp-vault.url").replaceAll("_secret-version_",Constants.VAULT_SECRET_VERSION).replaceAll("_secret-engine_",Constants.VAULT_SECRET_ENGINE);
        Constants.VAULT_ROOT_TOKEN = env.getProperty("hashicorp-vault.root-token");
        Constants.VAULT_LANDING_ZONE_PATH = env.getProperty("hashicorp-vault.landing-zone-path");
        Constants.VAULT_PROVIDER_AWS_CREDS_KEY = env.getProperty("hashicorp-vault.provider-aws-creds-key");

        log.info("VAULT_URL: {}",Constants.VAULT_URL);

        Constants.AWSX_API_BASE_URL = env.getProperty("awsx-api.base-url");
        Constants.AWSX_API_APPCONFIG_URL = env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.appconfig-api");
        Constants.AWSX_API_LAMBDA_URL = env.getProperty("awsx-api.base-url")+env.getProperty("awsx-api.lambda-api");

        log.info("AWSX_API_APPCONFIG_URL: {}",Constants.AWSX_API_APPCONFIG_URL);
    }
}
