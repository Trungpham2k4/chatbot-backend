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



        /// Thuật toán Bcrypt được sử dụng để mã hóa password
        /// Mỗi lần encode thì nó sẽ tạo 1 chuỗi salt ngẫu nhiên khiến cho hacker khó dùng được dictionary attack
        /// Dictionary attack: Hacker hash trc 1 số lượng mật khẩu sau đó match với mk trong database của user
        /// Param được truyền vào thường là độ mạnh của password (int) -> Default là 10,
        /// lưu ý càng khó thì thuật toán chạy lâu => Ảnh hưởng đến hiệu suất
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        user.setHashPassword(hashedPassword);
        user.setEmail(request.getEmail());
        userRepo.save(user);
    }
}
