package com.sierra.backend.controller;

import com.sierra.backend.dto.auth.LoginRequest;
import com.sierra.backend.dto.auth.LoginResponse;
import com.sierra.backend.dto.auth.RegisterRequest;
import com.sierra.backend.dto.auth.RegisterResponse;
import com.sierra.backend.dto.user.UserResponse;
import com.sierra.backend.entity.User;
import com.sierra.backend.exception.ServerException;
import com.sierra.backend.service.JwtService;
import com.sierra.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    PasswordEncoder passwordEncoder;
    UserService userService;
    JwtService jwtService;

    @Autowired
    public AuthController(PasswordEncoder passwordEncoder, UserService userService, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid  @RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userService.findByUsername(username);

        if(user == null) throw new ServerException(HttpStatus.UNAUTHORIZED, "Invalid username or password");

        boolean verifyPassword = passwordEncoder.matches(password, user.getPassword());
        if(!verifyPassword) {
            throw new ServerException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        String token = jwtService.generateToken(user.getId());

        UserResponse userDto = new UserResponse(user);

        return new LoginResponse(token, userDto);
    }

    @PostMapping("/register")
    public RegisterResponse register(@Valid  @RequestBody RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String phone = registerRequest.getPhone();

        User existingUser = userService.findByUsername(username);
        if(existingUser != null) {
            throw new ServerException(HttpStatus.CONFLICT, "User already exists");
        }

        List<User> existingPhoneMatch = userService.findByPhoneIn(List.of(phone));
        if(!existingPhoneMatch.isEmpty()) {
            throw new ServerException(HttpStatus.CONFLICT, "This phone number is already registered with another user");
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User();

        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setPhone(phone);

        User savedUser = userService.save(user);

        String token = jwtService.generateToken(savedUser.getId());
        return new RegisterResponse(token);
    }
}
