package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;
    public boolean sendEmail(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("nguyenvancuong.datn@gmail.com");
            message.setTo(email);
            message.setSubject("Lấy lại thông tin mật khẩu");
            message.setText("Username: " + user.getUsername() + " Password: " + user.getPassword());
            javaMailSender.send(message);
            return true;
        }
        return false;
    }
}
