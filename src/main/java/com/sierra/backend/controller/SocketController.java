package com.sierra.backend.controller;

import com.sierra.backend.dto.socket.SocketEventResponse;
import com.sierra.backend.dto.socket.TypingEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class SocketController {
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public SocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/typing")
    public void handleTypingEvent(@Payload TypingEventRequest typingEventRequest) {
        String senderId = typingEventRequest.getSenderId();
        String receiverId = typingEventRequest.getReceiverId();

        SocketEventResponse payload = new SocketEventResponse("TYPING", Map.of("otherUserId", senderId));

        simpMessagingTemplate.convertAndSend("/queue/events/" + receiverId, payload);
    }

    @MessageMapping("/stop-typing")
    public void handleStopTypingEvent(@Payload TypingEventRequest typingEventRequest) {
        String senderId = typingEventRequest.getSenderId();
        String receiverId = typingEventRequest.getReceiverId();

        SocketEventResponse payload = new SocketEventResponse("STOP_TYPING", Map.of("otherUserId", senderId));

        simpMessagingTemplate.convertAndSend("/queue/events/" + receiverId, payload);
    }
}
