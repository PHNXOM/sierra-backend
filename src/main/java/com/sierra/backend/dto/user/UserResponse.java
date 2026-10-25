package com.sierra.backend.dto.user;

import com.sierra.backend.entity.User;

import java.util.Set;
import java.util.UUID;

public class UserResponse {
    UUID id;
    String username;
    String profilePhoto;
    String bio;
    String phone;

    public UserResponse() {}

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profilePhoto = user.getProfilePhoto();
        this.bio = user.getBio();
        this.phone = user.getPhone();
    }

    public UserResponse(UUID id, String username, String profilePhoto, String bio, String phone) {
        this.id = id;
        this.username = username;
        this.profilePhoto = profilePhoto;
        this.bio = bio;
        this.phone = phone;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
