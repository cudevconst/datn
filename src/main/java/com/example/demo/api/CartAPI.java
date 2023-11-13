package com.example.demo.api;

import com.example.demo.dto.request.CartRequest;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.entity.User;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin
public class CartAPI {


    @Autowired
    private CartService cartService;

    @GetMapping("/test")
    private void test(HttpSession session){
        User user = (User) session.getAttribute("user");
        System.out.println(user);
        System.out.println(user.getEmail());
    }
    @GetMapping("/user")
    private ResponseEntity<?> findByUserId(HttpSession session){
        User user = (User) session.getAttribute("user");
        List<CartResponse> cartResponses = cartService.findByUserId(user.getId());
        return ResponseEntity.ok(cartResponses);
    }
    @PostMapping("/add")
    private ResponseEntity<?> addProducttoCart(HttpSession session, @RequestBody CartRequest cartRequest){
        return ResponseEntity.ok(cartService.addProductToCart(session, cartRequest));

    }

    @DeleteMapping("/delete/{cartId}")
    private ResponseEntity<?> deleteProductToCart(@PathVariable("cartId") Long cartId){
        boolean status = cartService.deleteProductToCart(cartId);
        if(status){
            return ResponseEntity.ok("Delete success");
        }
        else{
            return ResponseEntity.badRequest().body("Fail");
        }
    }
}
