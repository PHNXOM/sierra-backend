package com.sierra.backend.service;

import com.sierra.backend.dto.message.MessageResponse;
import com.sierra.backend.dto.message.MessageSaveRequest;
import com.sierra.backend.dto.message.MessageWithUnreadCountResponse;
import com.sierra.backend.dto.socket.SocketEventResponse;
import com.sierra.backend.entity.Message;
import com.sierra.backend.entity.User;
import com.sierra.backend.exception.ServerException;
import com.sierra.backend.repository.MessageRepository;
import com.sierra.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MessageService {
    MessageRepository messageRepository;
    UserRepository userRepository;
    SimpMessagingTemplate simpMessagingTemplate;
    ObjectMapper objectMapper;
    CloudinaryService cloudinaryService;

    @Autowired
    public MessageService(MessageRepository messageRepository, SimpMessagingTemplate simpMessagingTemplate,
                          ObjectMapper objectMapper, CloudinaryService cloudinaryService, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.objectMapper = objectMapper;
        this.cloudinaryService = cloudinaryService;
        this.userRepository = userRepository;
    }

    @Transactional
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public Message handleMessageSend(MessageSaveRequest messageSaveRequest) {
        String mediaURL = null;
        String mediaType = null;

        if(messageSaveRequest.getAttachment() != null && !messageSaveRequest.getAttachment().isEmpty()) {
            Map<?, ?> uploadResult = cloudinaryService.uploadFile(messageSaveRequest.getAttachment());
            mediaURL = uploadResult.get("secure_url").toString();
            mediaType = uploadResult.get("resource_type").toString();
        }

        Message savedMessage = persistMessage(messageSaveRequest, mediaURL, mediaType);

        // push notification and emit message to sender and receiver
        emitMessageToReceiver(savedMessage);
        emitMessageToSender(savedMessage);

        sendPushNotification(savedMessage);

        return savedMessage;
    }

    @Transactional
    public Message persistMessage(MessageSaveRequest messageSaveRequest, String mediaURL, String mediaType) {
        User sender = userRepository.findById(messageSaveRequest.getSenderId()).orElseThrow(
                () -> new ServerException(HttpStatus.NOT_FOUND, "User not found")
        );
        User receiver = userRepository.findById(messageSaveRequest.getReceiverId()).orElseThrow(
                () -> new ServerException(HttpStatus.NOT_FOUND, "User not found")
        );

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessage(messageSaveRequest.getMessage());
        message.setMediaURL(mediaURL);
        message.setMediaType(mediaType);

        Message savedMessage = messageRepository.save(message);

        return savedMessage;
    }

    public List<Message> getMessagesBetweenTwoUsers(UUID userId1, UUID userId2) {
        return messageRepository.getMessagesBetweenTwoUsers(userId1, userId2);
    }

    @Transactional
    public void deleteAllMessagesByUserId(UUID userId) {
        messageRepository.deleteAllMessagesByUserId(userId);
    }

    @Transactional
    public void deleteAllMessagesBetweenTwoUsers(UUID userId1, UUID userId2) {
        messageRepository.deleteAllMessagesBetweenTwoUsers(userId1, userId2);
        emitDeleteChat(userId1, userId2);
    }


    public List<MessageWithUnreadCountResponse> getLatestMessagesByUserId(UUID userId) {
        List<Message> messages = messageRepository.getLatestMessagesByUserId(userId);

        List<MessageWithUnreadCountResponse> response = messages.stream().map(message -> {
            int unreadCount = messageRepository.getUnreadCount(userId, message.getSender().getId());

            return new MessageWithUnreadCountResponse(message, unreadCount);
        }).toList();

        return response;
    }

    @Transactional
    public void markMessagesAsRead(UUID currentUserId, UUID otherUserId) {
        messageRepository.markMessagesAsRead(currentUserId, otherUserId);
    }



    // WEBSOCKET
    public void emitMessageToReceiver(Message message) {
        int unreadCount = messageRepository.getUnreadCount(message.getReceiver().getId(), message.getSender().getId());

        MessageWithUnreadCountResponse response = new MessageWithUnreadCountResponse(message, unreadCount);

        SocketEventResponse payload = new SocketEventResponse("NEW_MESSAGE", response);

        simpMessagingTemplate.convertAndSend("/queue/events/" + message.getReceiver().getId(), payload);
    }

    public void emitMessageToSender(Message message) {
        MessageResponse response = new MessageResponse(message);

        SocketEventResponse payload = new SocketEventResponse("MESSAGE_SENT", response);

        simpMessagingTemplate.convertAndSend("/queue/events/" + message.getSender().getId(), payload);
    }

    public void emitDeleteChat(UUID userId1, UUID userId2) {
        SocketEventResponse payload1 = new SocketEventResponse("DELETE_CHAT", Map.of(
                "receiverId", userId2
        ));

        SocketEventResponse payload2 = new SocketEventResponse("DELETE_CHAT", Map.of(
                "receiverId", userId1
        ));

        simpMessagingTemplate.convertAndSend("/queue/events/" + userId2, payload1);
        simpMessagingTemplate.convertAndSend("/queue/events/" + userId1, payload2);
    }

    // PUSH NOTIFICATION
    @Async
    public void sendPushNotification(Message message) {
        User sender = message.getSender();
        User receiver = message.getReceiver();

        if(receiver == null || receiver.getPushTokens() == null || receiver.getPushTokens().isEmpty()) return;

        RestClient restClient = RestClient.create();

        String notificationBody;
        if (message.getMessage() != null && !message.getMessage().isEmpty()) {
            notificationBody = message.getMessage();
        } else if ("image".equals(message.getMediaType())) {
            notificationBody = "📷 Photo";
        } else if ("video".equals(message.getMediaType())) {
            notificationBody = "🎥 Video";
        } else {
            notificationBody = "🎧 Audio";
        }

        for(String receiverToken: receiver.getPushTokens()) {
            Map<String, Object> payload = Map.of(
                    "to", receiverToken,
                    "title", sender.getUsername(),
                    "body", notificationBody
            );

            restClient.post()
                    .uri("https://exp.host/--/api/v2/push/send")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
        }
    }
}
