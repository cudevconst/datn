package com.example.demo.api;

import com.example.demo.service.InvoiceService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceAPI {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/user")
    public ResponseEntity<?> test(HttpSession session){
        return ResponseEntity.ok(invoiceService.findOrderByUser(session));
    }

    @GetMapping("/test/{userId}")
    public ResponseEntity<?> test(@PathVariable("userId") Long userId , HttpSession session){
        return ResponseEntity.ok(invoiceService.findOrderByUserTest(userId, session));
    }
}
