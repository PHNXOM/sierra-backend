package com.sierra.backend.service;

import com.sierra.backend.dto.user.PushTokenRequest;
import com.sierra.backend.dto.user.UserUpdateRequest;
import com.sierra.backend.entity.User;
import com.sierra.backend.exception.ServerException;
import com.sierra.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    UserRepository userRepository;
    CloudinaryService cloudinaryService;
    MessageService messageService;

    @Autowired
    public UserService(UserRepository userRepository, CloudinaryService cloudinaryService, MessageService messageService) {
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
        this.messageService = messageService;
    }



    public User findById(UUID id) {
        Optional<User> result = userRepository.findById(id);
        return result.orElse(null);

    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findByPhoneIn(List<String> phones) {
        return userRepository.findByPhoneIn(phones);
    }




    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UUID userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND, "User not found"));

        if (userUpdateRequest.getBio() != null) {
            user.setBio(userUpdateRequest.getBio());
        }

        if (Boolean.TRUE.equals(userUpdateRequest.getUnsetProfilePhoto())) {
            user.setProfilePhoto(null);
        } else if (userUpdateRequest.getProfilePhoto() != null && !userUpdateRequest.getProfilePhoto().isEmpty()) {
            Map<?, ?> uploadResult = cloudinaryService.uploadFile(userUpdateRequest.getProfilePhoto());
            user.setProfilePhoto(uploadResult.get("secure_url").toString());
        }

        User saved = userRepository.save(user);
        return saved;
    }

    @Transactional
    public void deleteUser(User user) {
        UUID userId = user.getId();

        messageService.deleteAllMessagesByUserId(userId);
        userRepository.deleteById(userId);
    }





    @Transactional
    public void addPushToken(UUID userId, PushTokenRequest pushTokenRequest) {
        if(pushTokenRequest.getPushToken() == null) return;

        User user = userRepository.findById(userId).orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND, "User not found"));

        if(user.getPushTokens() == null) {
            user.setPushTokens(new HashSet<>());
        }

        user.getPushTokens().add(pushTokenRequest.getPushToken());

        userRepository.save(user);
    }

    @Transactional
    public void deletePushTokens(UUID userId, PushTokenRequest pushTokenRequest) {
        if(pushTokenRequest.getPushToken() == null) return;

        User user = userRepository.findById(userId).orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND, "User not found"));

        user.getPushTokens().remove(pushTokenRequest.getPushToken());
        userRepository.save(user);
    }
}
