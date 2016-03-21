package com.nttdata.batterystatus.domain;

/**
 * Created by kirankumar on 14/03/16.
 */
public class DeviceInfo {

    private String assetCondition;

    private String assetId;

    private boolean availability;

    private BatteryStatus batteryStatus;

    private String brand;

    private long createdOn;

    private String currentCompany;

    private String currentUser;

    private String description;

    private String imei;

    private boolean isOutdated;

    private String lastActionByUid;

    private long lastCheckOutDateTime;

    private long lastModifiedDateTime;

    private String locatedAt;

    private String model;

    private String oldAssetId;

    private String osVersion;

    private String picture;

    private String platform;

    private String  screenResolution;

    public String getAssetCondition() {
        return assetCondition;
    }

    public void setAssetCondition(String assetCondition) {
        this.assetCondition = assetCondition;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public BatteryStatus getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(BatteryStatus batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public String getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(String currentCompany) {
        this.currentCompany = currentCompany;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public boolean isOutdated() {
        return isOutdated;
    }

    public void setIsOutdated(boolean isOutdated) {
        this.isOutdated = isOutdated;
    }

    public String getLastActionByUid() {
        return lastActionByUid;
    }

    public void setLastActionByUid(String lastActionByUid) {
        this.lastActionByUid = lastActionByUid;
    }

    public long getLastCheckOutDateTime() {
        return lastCheckOutDateTime;
    }

    public void setLastCheckOutDateTime(long lastCheckOutDateTime) {
        this.lastCheckOutDateTime = lastCheckOutDateTime;
    }

    public long getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(long lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public String getLocatedAt() {
        return locatedAt;
    }

    public void setLocatedAt(String locatedAt) {
        this.locatedAt = locatedAt;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOldAssetId() {
        return oldAssetId;
    }

    public void setOldAssetId(String oldAssetId) {
        this.oldAssetId = oldAssetId;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getScreenResolution() {
        return screenResolution;
    }

    public void setScreenResolution(String screenResolution) {
        this.screenResolution = screenResolution;
    }
}
