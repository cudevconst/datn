package com.example.demo.service;

import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User saveUser(User user){
        User saved = userRepository.save(user);
        return saved;
    }

    @Transactional
    public UserResponse changePassword(HttpSession session, String newPassword){
        User user = (User) session.getAttribute("user");
        user.setPassword(newPassword);
        userRepository.save(user);
        UserResponse userResponse =UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .fullName(user.getFullName())
                .build();
        return userResponse;
    }

    public String checkPassword(HttpSession session, String oldPassword, String newPassword, String confirmPassword){
        User user = (User) session.getAttribute("user");
        String password = user.getPassword();

        if(!password.equals(oldPassword)){
            return "Mật khẩu cũ không chính xác";
        }
        if(!newPassword.equals(confirmPassword)){
            return "Mật khẩu không trùng khớp!";
        }
        return "";
    }
    public User findByUsername(String username){
        User user = userRepository.findByUsername(username).orElse(null);
        return user;
    }

    public User findByEmaail(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        return user;
    }
}
