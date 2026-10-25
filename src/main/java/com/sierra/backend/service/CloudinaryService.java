package com.sierra.backend.service;

import com.cloudinary.Cloudinary;
import com.sierra.backend.exception.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {
    Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<?, ?> uploadFile(MultipartFile file) {
        if(file == null || file.isEmpty()) return null;

        Map<String, Object> options = new HashMap<>();
        options.put("folder", "Sierra");
        options.put("resource_type", "auto");

        try {
            return cloudinary.uploader().upload(file.getBytes(), options);
        }catch (IOException e) {
            throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, "Error uploading media");
        }
    }
}
