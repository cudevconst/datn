package com.example.demo.api;

import com.example.demo.dto.request.InventoryRequest;
import com.example.demo.dto.response.InventoryResponse;
import com.example.demo.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/inventory")
public class InventoryAPI {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/quantity")
    private ResponseEntity<Integer> findQuantityByColorAndSize(@RequestParam("productId") Long productId,
                                                               @RequestParam("color") String color,
                                                               @RequestParam("size") String size){
        System.out.println(productId + " " + color + " " + size);
        return ResponseEntity.ok(inventoryService.findQuantityProductByColorAndSize(productId, color, size));
    }
    @PostMapping("/add")
    private ResponseEntity<?> addNewProduct(@RequestBody InventoryRequest inventoryRequest) {
        InventoryResponse inventoryResponse = inventoryService.addNewInventory(inventoryRequest);
        if (inventoryResponse != null) {
            return ResponseEntity.ok(inventoryResponse);
        }
        return ResponseEntity.badRequest().body("Error");
    }
}
