package com.sierra.backend.dto.message;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class MessageSaveRequest {
    private String message;
    private UUID senderId;
    private UUID receiverId;
    private MultipartFile attachment = null;

    public MessageSaveRequest() {
    }

    public MessageSaveRequest(String message, UUID senderId, UUID receiverId, MultipartFile attachment) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.attachment = attachment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    }

    public MultipartFile getAttachment() {
        return attachment;
    }

    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }
}
