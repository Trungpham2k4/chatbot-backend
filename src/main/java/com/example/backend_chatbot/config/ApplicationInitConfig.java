package com.example.backend_chatbot.config;

import com.example.backend_chatbot.entity.User;
import com.example.backend_chatbot.enums.Roles;
import com.example.backend_chatbot.repository.UserRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j /// Inject logger
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    @Autowired
    public ApplicationInitConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    ApplicationRunner applicationRunner(UserRepo repo){
        return args -> {
            if(repo.findByUserName("admin").isEmpty()){
                var roles = new HashSet<String>();
                roles.add(Roles.ADMIN.name());

                User user = User.builder()
                        .userName("admin")
                        .hashPassword(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                repo.save(user);
            }
        };
    }
}
