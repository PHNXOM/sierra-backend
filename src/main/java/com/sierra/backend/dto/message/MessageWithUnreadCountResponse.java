package com.sierra.backend.dto.message;


import com.sierra.backend.entity.Message;

public class MessageWithUnreadCountResponse extends MessageResponse {
    int unreadCount;

    public MessageWithUnreadCountResponse() {}

    public MessageWithUnreadCountResponse(Message message, int unreadCount) {
        super(message);
        this.unreadCount = unreadCount;
    }


    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
