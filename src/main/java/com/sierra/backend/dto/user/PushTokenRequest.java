package com.sierra.backend.dto.user;

public class PushTokenRequest {
    private String pushToken;

    public PushTokenRequest() {}
    public PushTokenRequest(String pushToken) {this.pushToken = pushToken;}

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }
}
