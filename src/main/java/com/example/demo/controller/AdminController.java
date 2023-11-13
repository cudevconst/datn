package com.example.demo.controller;

import com.example.demo.dto.request.TypeRequest;
import com.example.demo.dto.response.TypeResponse;
import com.example.demo.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private TypeService typeService;
    public void setUserHeader(HttpSession httpSession, Model model){
        String username = (String) httpSession.getAttribute("username");
        if(username != null){
            model.addAttribute("username", username);
        }
        else{
            model.addAttribute("user", username);
        }
    }
    @GetMapping("/type")
    public String typePage(Model model, HttpSession httpSession){
        setUserHeader(httpSession, model);
        List<TypeResponse> typeResponses = typeService.findAll();
        model.addAttribute("types", typeResponses);
//        System.out.println(nameType);
        return "type";
    }

    @GetMapping("/product")
    public String productAdminPage(Model model, HttpSession session){
        setUserHeader(session, model);
        return "admin-product";
    }

    @GetMapping("/product/add")
    public String productAdminAddPage(Model model, HttpSession session){
        setUserHeader(session, model);
        return "admin-product-add";
    }
    @GetMapping("/product/update/{slug}")
    public String productAdminUpdatePage(@PathVariable("slug") String slug, Model model, HttpSession session){
        setUserHeader(session, model);
        session.setAttribute("slug", slug);
        return "admin-product-update";
    }
    @PostMapping("/type/add")
    public String typePageProcess(@RequestParam String nameType){
        TypeRequest typeRequest = TypeRequest.builder()
                .nameType(nameType)
                .build();
        typeService.saveType(typeRequest);
        return "redirect:/type";
    }

    @PostMapping("/type/delete/{id}")
    private ResponseEntity<?> deleteByTypeId(@PathVariable("id") Long id){
        boolean statusDelete = typeService.deleteByTypeId(id);
        if(statusDelete){
            return ResponseEntity.ok("Delete success");
        }
        else{
            return ResponseEntity.ok("Delete fail");
        }
    }

    @GetMapping("/order/admin")
    public String adminOrderPage(HttpSession session, Model model){
        setUserHeader(session, model);
        return "admin-order";
    }
}
