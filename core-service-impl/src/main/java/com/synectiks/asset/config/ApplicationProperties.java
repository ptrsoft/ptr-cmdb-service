package com.synectiks.asset.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Assetservice.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
	private String securityServiceUrl;

	public String getSecurityServiceUrl() {
		return securityServiceUrl;
	}

	public void setSecurityServiceUrl(String securityServiceUrl) {
		this.securityServiceUrl = securityServiceUrl;
	}
	
	
}
