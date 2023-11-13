package com.example.demo.service;

import com.example.demo.dto.request.InventoryRequest;
import com.example.demo.dto.response.InventoryResponse;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.entity.Inventory;
import com.example.demo.entity.Product;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public Integer findQuantityProductByColorAndSize(Long productId, String color, String size){
        return inventoryRepository.countQuantityProductByColorAndSize(productId, color, size);
    }
    @Transactional
    public InventoryResponse addNewInventory(InventoryRequest inventoryRequest){
        Product product = productRepository.findById(inventoryRequest.getProductId()).orElse(null);
        if(product != null){
            Inventory inventory = Inventory.builder()
                    .color(inventoryRequest.getColor())
                    .size(inventoryRequest.getSize())
                    .quantity(inventoryRequest.getQuantity())
                    .dateInput(inventoryRequest.getDateInput())
                    .product(product)
                    .build();
            inventoryRepository.save(inventory);

            ProductResponse productResponse = ProductResponse.builder()
                    .id(product.getId())
                    .slug(product.getSlug())
                    .price(product.getPrice())
                    .image1(product.getImage1())
                    .image2(product.getImage2())
                    .image3(product.getImage3())
                    .name(product.getName())
                    .types(product.getTypes())
                    .colors(product.getColor())
                    .sizes(product.getSizes())
                    .build();
            InventoryResponse inventoryResponse = InventoryResponse.builder()
                    .id(inventory.getId())
                    .color(inventoryRequest.getColor())
                    .size(inventoryRequest.getSize())
                    .quantity(inventoryRequest.getQuantity())
                    .dateInput(inventoryRequest.getDateInput())
                    .productResponse(productResponse)
                    .build();
            return  inventoryResponse;
        }
        return null;
    }
}
