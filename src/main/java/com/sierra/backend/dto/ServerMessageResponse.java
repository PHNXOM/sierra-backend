package com.sierra.backend.dto;

public class ServerMessageResponse {
    String message;

    public ServerMessageResponse() {}

    public ServerMessageResponse(String message) {this.message = message;}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
