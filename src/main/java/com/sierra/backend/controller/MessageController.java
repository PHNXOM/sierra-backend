package com.sierra.backend.controller;

import com.sierra.backend.dto.ServerMessageResponse;
import com.sierra.backend.dto.message.MessageSaveRequest;
import com.sierra.backend.dto.message.MessageResponse;
import com.sierra.backend.dto.message.MessageWithUnreadCountResponse;
import com.sierra.backend.entity.Message;
import com.sierra.backend.entity.User;
import com.sierra.backend.service.CloudinaryService;
import com.sierra.backend.service.MessageService;
import com.sierra.backend.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MessageController {
    CloudinaryService cloudinaryService;
    UserService userService;
    MessageService messageService;

    public MessageController(CloudinaryService cloudinaryService, UserService userService, MessageService messageService) {
        this.cloudinaryService = cloudinaryService;
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("/messages/{otherUserId}")
    public List<MessageResponse> getMessagesBetweenTwoUsers(@AuthenticationPrincipal User user, @PathVariable("otherUserId") UUID otherUserId) {
        UUID userId1 = user.getId();
        UUID userId2 = otherUserId;

        List<Message> messages = messageService.getMessagesBetweenTwoUsers(userId1, userId2);

        List<MessageResponse> response =  messages.stream().map(message -> new MessageResponse(message)).toList();

        return response;
    }

    @GetMapping("/messages/inbox")
    public List<MessageWithUnreadCountResponse> getInbox(@AuthenticationPrincipal User user) {
        UUID userId = user.getId();

        List<MessageWithUnreadCountResponse> response = messageService.getLatestMessagesByUserId(userId);

        return response;
    }

    @PostMapping(value = "/messages", consumes = "multipart/form-data")
    public Map<String, Object> saveMessage(@ModelAttribute MessageSaveRequest messageSaveRequest) {
        Message savedMessage = messageService.handleMessageSend(messageSaveRequest);

        MessageResponse messageResponse = new MessageResponse(savedMessage);

        return Map.of("message", "Save successfully",
                "savedMessage", messageResponse);
    }

    @DeleteMapping("/messages/{otherUserId}")
    public ServerMessageResponse deleteMessages(@AuthenticationPrincipal User user, @PathVariable("otherUserId")UUID otherUserId) {
        UUID userId1 = user.getId();
        UUID userId2 = otherUserId;

        messageService.deleteAllMessagesBetweenTwoUsers(userId1, userId2);

        return new ServerMessageResponse("Messages deleted successfully");
    }


    @PatchMapping("/messages/{otherUserId}/read")
    public ServerMessageResponse markRead(@AuthenticationPrincipal User user, @PathVariable UUID otherUserId) {
        UUID currentUserId = user.getId();

        messageService.markMessagesAsRead(currentUserId, otherUserId);

        return new ServerMessageResponse("Messages marked as read");
    }
}
