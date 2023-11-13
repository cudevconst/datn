package com.example.demo.api;

import com.example.demo.dto.request.InvoiceRequest;
import com.example.demo.dto.request.OrderProductRequest;
import com.example.demo.dto.request.OrderRequest;
import com.example.demo.dto.response.OrderResponse;
import com.example.demo.service.OrderSerive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/order")
public class OrderAPI {
    @Autowired
    private OrderSerive orderSerive;

    @PostMapping("/create")
    private ResponseEntity<?> addNewOrder(@RequestBody InvoiceRequest invoiceRequest, HttpSession session){
        return ResponseEntity.ok(orderSerive.addNewOrder(invoiceRequest, session));
    }

    @GetMapping("/user")
    private ResponseEntity<?> findAll(HttpSession session){
        return ResponseEntity.ok(orderSerive.findAll(session));
    }

    @GetMapping("/admin/find")
    private ResponseEntity<?> findByStatus(@RequestParam("status") String status){
        return ResponseEntity.ok(orderSerive.findByStatus(status));
    }

    @PostMapping("/update")
    private ResponseEntity<?> updataStatus(@RequestParam("status") String status, @RequestParam("orderId") Long orderId){
        return ResponseEntity.ok(orderSerive.updateStatus(orderId, status));
    }
    @DeleteMapping("/delete")
    private ResponseEntity<?> deleteOrder(@RequestParam("orderId") Long orderId){
        boolean status = orderSerive.deleteOrder(orderId);
        if(status){
            return ResponseEntity.ok("Delete success");
        }
        else{
            return ResponseEntity.badRequest().body("Delete fail");
        }
    }
}
