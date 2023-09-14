package com.synectiks.asset.handler;

public interface CloudHandler {
    public void save(String organization, String department, String landingZone, String awsRegion);
    public String getUrl();
}
