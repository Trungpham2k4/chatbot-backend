package com.example.backend_chatbot.service;


import com.example.backend_chatbot.dto.request.UserRequest;
import com.example.backend_chatbot.dto.response.UserInfoResponse;
import com.example.backend_chatbot.entity.User;
import com.example.backend_chatbot.enums.Roles;
import com.example.backend_chatbot.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public UserInfoResponse createUser(UserRequest request) {
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

        HashSet<String> roles = new HashSet<>();
        roles.add(Roles.USER.name());
        user.setRoles(roles);

        try{
            userRepo.save(user);
        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("User already exists");
        }

        return new UserInfoResponse(user.getUserName(), user.getEmail(), user.getRoles());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserInfoResponse> getAllUsers() {
        List<UserInfoResponse> users = new ArrayList<>();

        List<User> userList = userRepo.findAll();
        System.out.println(userList.size());
        for (User user : userList) {
            UserInfoResponse userInfoResponse = new UserInfoResponse();
            userInfoResponse.setUsername(user.getUserName());
            userInfoResponse.setEmail(user.getEmail());
            userInfoResponse.setRoles(user.getRoles());

            users.add(userInfoResponse);
        }
        return users;
    }

    /**
     * Dùng @PostAuthorize với Spring Expression language (SpEL) để check nếu user trả về chính là user đang đăng nhập
     * @return
     */
    @PostAuthorize("returnObject.username == authentication.name")
    public UserInfoResponse getInfo(Integer id){
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setUsername(user.getUserName());
        userInfoResponse.setEmail(user.getEmail());
        userInfoResponse.setRoles(user.getRoles());
        return userInfoResponse;
    }
}
