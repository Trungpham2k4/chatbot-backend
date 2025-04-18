package com.example.backend_chatbot.service;


import com.example.backend_chatbot.dto.request.UserRequest;
import com.example.backend_chatbot.entity.User;
import com.example.backend_chatbot.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public void createUser(UserRequest request) {
        User user = new User();
        user.setUserName(request.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        user.setHashPassword(hashedPassword);
        user.setEmail(request.getEmail());
        userRepo.save(user);
    }
}
