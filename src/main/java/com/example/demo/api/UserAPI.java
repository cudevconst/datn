package com.example.demo.api;

import com.example.demo.dto.request.TypeRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailSenderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.TypeService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public")
public class UserAPI {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TypeService typeService;

    @Autowired
    private UserService userService;

    @PostMapping("/change-password")
    private ResponseEntity<?> changePassword(HttpSession session,
                                             @RequestParam("oldPassword") String oldPassword,
                                             @RequestParam("newPassword") String newPassword,
                                             @RequestParam("confirmPassword") String confirmPassword){
        String checkPassword = userService.checkPassword(session, oldPassword, newPassword, confirmPassword);
        Map<Object, Object> map = new HashMap<>();
        if(checkPassword.equals("")){
            UserResponse userResponse = userService.changePassword(session, newPassword);
            map.put("code", 200);
            map.put("message", "Thay đổi mật khẩu thành công");
            map.put("data", userResponse);
            return ResponseEntity.ok(map);
        }
        else{
            map.put("code", 400);
            map.put("message", checkPassword);
            map.put("data", null);
            return ResponseEntity.badRequest().body(map);
        }
    }
    @GetMapping("/all")
    private ResponseEntity<?> findAllProduct(){
        return ResponseEntity.ok(productService.findAllProduct());
    }

    @GetMapping("/product/{slug}")
    private ResponseEntity<?> findPBySlug(@PathVariable("slug") String slug){
        return ResponseEntity.ok(productService.findProductBySlug(slug));
    }

    @GetMapping("/type/all")
    private ResponseEntity<?> findAllType(){
        return ResponseEntity.ok(typeService.findAll());
    }

    @GetMapping("/product/type")
    private ResponseEntity<?> findByTypeid(@RequestParam("typeId") Long typeId){
        return ResponseEntity.ok(productService.findByTypeId(typeId));
    }

    @GetMapping("/type/add")
    private ResponseEntity<?> saveType(@RequestBody TypeRequest typeRequest){
        return ResponseEntity.ok(typeService.saveType(typeRequest));
    }
}
