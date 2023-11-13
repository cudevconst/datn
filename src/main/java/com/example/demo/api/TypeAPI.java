package com.example.demo.api;

import com.example.demo.dto.request.TypeRequest;
import com.example.demo.dto.response.TypeResponse;
import com.example.demo.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/type")
public class TypeAPI {
    @Autowired
    private TypeService typeService;

    @GetMapping("/all")
    private ResponseEntity<?> findAll(){
        return ResponseEntity.ok(typeService.findAll());
    }
    @PostMapping("/add")
    private ResponseEntity<?> saveType(@RequestBody TypeRequest typeRequest){
        return ResponseEntity.ok(typeService.saveType(typeRequest));
    }

    @PostMapping("/delete/{id}")
    private ResponseEntity<?> deleteByTypeId(@PathVariable("id") Long id){
        boolean statusDelete = typeService.deleteByTypeId(id);
        if(statusDelete){
            return ResponseEntity.ok("Delete success");
        }
        else{
            return ResponseEntity.ok("Delete fail");
        }
    }

    @PostMapping("/update/{id}/{nameType}")
    private ResponseEntity<?> updateById(@PathVariable("id") Long id, @PathVariable("nameType") String nameType){
        TypeResponse typeResponse = typeService.updateById(id, nameType);
        System.out.println(id);
        System.out.println(nameType);
        if(typeResponse == null){
            return ResponseEntity.badRequest().body("ID khong ton tai");
        }
        else{
            return ResponseEntity.ok(typeResponse);
        }
    }
}
