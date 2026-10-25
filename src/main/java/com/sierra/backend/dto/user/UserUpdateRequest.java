package com.sierra.backend.dto.user;

import org.springframework.web.multipart.MultipartFile;

public class UserUpdateRequest {
    String bio;
    MultipartFile profilePhoto;
    Boolean unsetProfilePhoto = false;

    public UserUpdateRequest() {}

    public UserUpdateRequest(String bio, MultipartFile profilePhoto, Boolean unsetProfilePhoto) {
        this.bio = bio;
        this.profilePhoto = profilePhoto;
        this.unsetProfilePhoto = unsetProfilePhoto;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public MultipartFile getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(MultipartFile profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Boolean getUnsetProfilePhoto() {
        return unsetProfilePhoto;
    }

    public void setUnsetProfilePhoto(Boolean unsetProfilePhoto) {
        this.unsetProfilePhoto = unsetProfilePhoto;
    }
}
