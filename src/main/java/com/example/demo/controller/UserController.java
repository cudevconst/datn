package com.example.demo.controller;

import com.example.demo.dto.ChangeInfoForm;
import com.example.demo.dto.ChangePasswordForm;
import com.example.demo.entity.User;
import com.example.demo.service.EmailSenderService;
import com.example.demo.service.OrderSerive;
import com.example.demo.service.UserService;
import com.example.demo.service.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private OrderSerive orderSerive;
    @GetMapping("/demo")
    public String testPage(){
        return "index";
    }

    @PostMapping("/demo")
    public String test1pagfe(){
        return "index";
    }
    public void setUserHeader(HttpSession httpSession, Model model){
        String username = (String) httpSession.getAttribute("username");
        if(username != null){
            model.addAttribute("username", username);
        }
        else{
            model.addAttribute("user", username);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            httpSession.setAttribute("user", null);

        }
        else{
            User user = userService.findByUsername(authentication.getName());
            httpSession.setAttribute("user", user);
        }
    }
    @GetMapping("/")
    public String homePage(Model model, HttpSession httpSession){
        setUserHeader(httpSession, model);
        System.out.println(httpSession.getAttribute("username"));
        System.out.println(httpSession.getAttribute("user"));
        return "index";
    }
    @GetMapping("/login")
    public String login(Model model, HttpSession session){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("username", false);

        }
        else{
            User user = userService.findByUsername(authentication.getName());

        }
        return "login";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model){

        model.addAttribute("userRegister", new User());
        model.addAttribute("message", "teur");
        return "register";
    }
    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute("userRegister") User user, BindingResult results){
        if(results.hasErrors()){
            return "register";
        }
        else {

            user.setRole("USER");
            userService.saveUser(user);
            return "redirect:/login";
        }
    }
    @GetMapping("/forward-password")
    public String forwardPasswordPage(Model model, HttpSession session){
        setUserHeader(session, model);
        return "forward-password";
    }
    @PostMapping("/forward-password")
    public String processForwardPassword(@RequestParam("email") String email, Model model){
        boolean status = emailSenderService.sendEmail(email);
        if(status){
            model.addAttribute("success", true);
            model.addAttribute("message", "Send email success");
        }
        else{
            model.addAttribute("error", true);
            model.addAttribute("message", "Email does not exist");
        }
        return "forward-password";
    }

    @GetMapping("/info")
    public String infoPage(Model model, HttpSession session){
        setUserHeader(session, model);
        String username = (String) session.getAttribute("username");
        User user = userService.findByUsername(username);
        System.out.println(user.getPassword());
        model.addAttribute("user", user);
        System.out.println(user.getPassword());
        return "change-info";
    }
//    @PostMapping("/change-info")
//    public String processInfo(@Valid @ModelAttribute("user") ChangeInfoForm user, BindingResult results, Model model){
//        System.out.println("hehe");
//        if(results.hasErrors()){
//            model.addAttribute("message", false);
//            return "change-info";
//        }
//        else{
//            User userOld = userService.findByUsername(user.getUsername());
//            userOld.setEmail(user.getEmail());
//            userOld.setFullName(user.getFullName());
//            userOld.setPhoneNumber(user.getPhoneNumber());
//            userService.saveUser(userOld);
//            model.addAttribute("message", true);
//            System.out.println(userOld.getPhoneNumber());
//
//        }
//        return "change-info";
//    }

    @GetMapping("/change-password")
    public String changePasswordPage(Model model, HttpSession session){
        setUserHeader(session, model);
        ChangePasswordForm changePasswordForm = new ChangePasswordForm();
        model.addAttribute("changePassword", changePasswordForm);
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePasswordProcess(@Valid @ModelAttribute("changePassword")ChangePasswordForm changePasswordForm, BindingResult results, Model model, HttpSession session){

        String username = (String) session.getAttribute("username");
        if(results.hasErrors()){
            return "change-password";
        }
        else{
            User user = userService.findByUsername(username);
            String password = user.getPassword();
            if(!changePasswordForm.getCurrentPassword().equals(password)){
                model.addAttribute("currentPasswordMessage", true);
                return "change-password";
            }
            if(!changePasswordForm.getNewPassword().equals(changePasswordForm.getConfirmPassword())){
                model.addAttribute("confirmPasswordMessage", true);
                return "change-password";
            }

            model.addAttribute("message", true);
            return "change-password";
        }
    }

    @GetMapping("/product/{slug}")
    public String productDetailPage(HttpSession session, Model model){
        setUserHeader(session, model);
        return "detail";
    }

    @GetMapping("/cart")
    public String cartPage(HttpSession session, Model model){
        setUserHeader(session, model);
        return "cart";
    }

    @GetMapping("/order")
    public String orderPage(HttpSession session, Model model){
        setUserHeader(session, model);
        return "order";
    }

    @GetMapping("/checkout/payment")
    private String paymentInfo(@RequestParam("vnp_ResponseCode") String responseCode,
                                          @RequestParam("vnp_TransactionStatus") String transactionStatus,
                                          @RequestParam("vnp_OrderInfo") String orderId){
        Map<Object, Object> mapResults = vnPayService.paymentInfo(responseCode, transactionStatus, orderId);
        Integer statusCode = (Integer) mapResults.get("status");
        if(statusCode == 200){
            return "order";
        }
        else {
            System.out.println(orderId);
            orderSerive.deleteOrder(Long.parseLong(orderId));
            return "cart";
        }
    }

    @GetMapping("/history/order")
    private String invoicePage(HttpSession session, Model model){
        setUserHeader(session, model);
        return "invoice";
    }

    @GetMapping("/{name}")
    private String homeByProducrtPage(HttpSession session, Model model, @PathVariable("name") String name){
        setUserHeader(session, model);
        return "index.product";
    }
}
