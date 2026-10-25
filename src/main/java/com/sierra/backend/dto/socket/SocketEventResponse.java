package com.sierra.backend.dto.socket;

public class SocketEventResponse {
    String event;
    Object data;

    public SocketEventResponse() {}

    public SocketEventResponse(String event, Object data) {
        this.event = event;
        this.data = data;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
