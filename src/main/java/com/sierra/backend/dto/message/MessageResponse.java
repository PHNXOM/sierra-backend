package com.sierra.backend.dto.message;

import com.sierra.backend.dto.user.UserResponse;
import com.sierra.backend.entity.Message;

import java.time.LocalDateTime;
import java.util.UUID;

public class MessageResponse {
    private UUID id;
    private UserResponse sender;
    private UserResponse receiver;
    private String message;
    private String mediaURL;
    private String mediaType;
    private LocalDateTime sentAt;
    private boolean isRead;

    public MessageResponse() {}

    public MessageResponse(Message message) {
        this.id = message.getId();
        this.sender = new UserResponse(message.getSender());
        this.receiver = new UserResponse(message.getReceiver());
        this.mediaURL = message.getMediaURL();
        this.message = message.getMessage();
        this.mediaType = message.getMediaType();
        this.sentAt = message.getSentAt();
        this.isRead = message.getIsRead();
    }

    public MessageResponse(UUID id, UserResponse sender, UserResponse receiver, String mediaURL, String message, String mediaType, LocalDateTime sentAt, Boolean isRead) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.mediaURL = mediaURL;
        this.message = message;
        this.mediaType = mediaType;
        this.sentAt = sentAt;
        this.isRead = isRead;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserResponse getSender() {
        return sender;
    }

    public void setSender(UserResponse sender) {
        this.sender = sender;
    }

    public UserResponse getReceiver() {
        return receiver;
    }

    public void setReceiver(UserResponse receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
