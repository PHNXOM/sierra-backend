package com.sierra.backend.controller;

import com.sierra.backend.dto.ServerMessageResponse;
import com.sierra.backend.dto.user.MatchPhoneRequest;
import com.sierra.backend.dto.user.PushTokenRequest;
import com.sierra.backend.dto.user.UserResponse;
import com.sierra.backend.dto.user.UserUpdateRequest;
import com.sierra.backend.entity.User;
import com.sierra.backend.service.CloudinaryService;
import com.sierra.backend.service.MessageService;
import com.sierra.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {
    UserService userService;
    MessageService messageService;
    CloudinaryService cloudinaryService;

    @Autowired
    public UserController(UserService userService, MessageService messageService,CloudinaryService cloudinaryService) {
        this.userService = userService;
        this.messageService = messageService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/profile")
    public UserResponse getProfile(@AuthenticationPrincipal User user) {
        UserResponse userResponse = new UserResponse(user);

        return userResponse;
    }

    @GetMapping("/users/{id}")
    public UserResponse getUserById(@PathVariable UUID id) {
        User user = userService.findById(id);

        return new UserResponse(user);
    }

    @PatchMapping(value = "/users", consumes = "multipart/form-data")
    public UserResponse updatedUser(@AuthenticationPrincipal User user, @ModelAttribute UserUpdateRequest userUpdateRequest) throws IOException {
        User saved = userService.updateUser(user.getId(), userUpdateRequest);

        return new UserResponse(saved);
    }

    @DeleteMapping("/users")
    public ServerMessageResponse deleteUser(@AuthenticationPrincipal User user) {
        userService.deleteUser(user);

        return new ServerMessageResponse("User deleted successfully");
    }

    @PostMapping("/users/push-tokens")
    public ServerMessageResponse addPushToken(@AuthenticationPrincipal User user, @RequestBody PushTokenRequest pushTokenRequest) {
        userService.addPushToken(user.getId(), pushTokenRequest);

        return new ServerMessageResponse("Push Token Saved!");
    }

    @DeleteMapping("/users/push-tokens")
    public ServerMessageResponse deletePushToken(@AuthenticationPrincipal User user, @RequestBody PushTokenRequest pushTokenRequest) {
       userService.deletePushTokens(user.getId(), pushTokenRequest);

        return new ServerMessageResponse("Push Token Deleted!");
    }

    @PostMapping("/users/contacts/match")
    public List<UserResponse> matchContacts(@RequestBody MatchPhoneRequest matchPhoneRequest) {
        List<User> users = userService.findByPhoneIn(matchPhoneRequest.getPhoneNumbers());

        List<UserResponse> response = users.stream().map(user -> new UserResponse(user)).toList();

        return response;
    }
}
