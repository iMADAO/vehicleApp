package cn.haizhi.bean;

public class Device {
    private String deviceId;

    private String userId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Device(String deviceId, String userId) {
        this.deviceId = deviceId;
        this.userId = userId;
    }

    public Device(String deviceId) {
        this.deviceId = deviceId;
    }

    public Device() {
    }
}