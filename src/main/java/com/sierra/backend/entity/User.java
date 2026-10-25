package com.sierra.backend.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "bio")
    private String bio = "Hey there, I am using Sierra!";

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "push_tokens")
    Set<String> pushTokens = new HashSet<>();

    public User() {}

    public User(UUID id, String username, String password, String profilePhoto, String bio, String phone, Set<String> pushTokens) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.profilePhoto = profilePhoto;
        this.bio = bio;
        this.phone = phone;
        this.pushTokens = pushTokens;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Set<String> getPushTokens() {
        return pushTokens;
    }

    public void setPushTokens(Set<String> pushTokens) {
        this.pushTokens = pushTokens;
    }
}
