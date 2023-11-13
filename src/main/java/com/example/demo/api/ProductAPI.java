package com.example.demo.api;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/product")
public class ProductAPI {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(productService.findAllProduct());
    }

    private ResponseEntity<?> findByTypeid(@RequestParam("typeId") Long typeId){
        return ResponseEntity.ok(productService.findByTypeId(typeId));
    }
    @GetMapping("/productId")
    public ResponseEntity<?> findByIds(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(productService.findByIds(ids));
    }
    @GetMapping("/detail/{slug}")
    public ResponseEntity<?> findBySlug(@PathVariable("slug") String slug){
        ProductResponse productResponse = productService.findProductBySlug(slug);
        if(productResponse != null){
            return ResponseEntity.ok(productResponse);
        }
        else{
            return ResponseEntity.badRequest().body("Not found by slug");
        }
    }
    @PostMapping("/add")
    public ResponseEntity<?> getByIds(@RequestParam("name") String name,
                                      @RequestParam("price") Double price,
                                      @RequestParam("image1") String image1,
                                      @RequestParam("image2") String image2,
                                      @RequestParam("image3") String image3,
                                      @RequestParam("sizes")Set<String> sizes,
                                      @RequestParam("colors") Set<String> colors,
                                      @RequestParam("ids") List<Long> ids){
        ProductRequest productRequest = ProductRequest.builder()
                .name(name)
                .price(price)
                .image1(image1)
                .image2(image2)
                .image3(image3)
                .sizes(sizes)
                .colors(colors)
                .types(ids)
                .build();

        return ResponseEntity.ok(productService.save(productRequest));
    }

    @PostMapping("/update/{slug}")
    public ResponseEntity<?> updateProduct(@RequestParam("name") String name,
                                      @RequestParam("price") Double price,
                                      @RequestParam("image1") String image1,
                                      @RequestParam("image2") String image2,
                                      @RequestParam("image3") String image3,
                                      @RequestParam("sizes")Set<String> sizes,
                                      @RequestParam("colors") Set<String> colors,
                                      @RequestParam("ids") List<Long> ids,
                                           @PathVariable("slug") String slug){
        ProductRequest productRequest = ProductRequest.builder()
                .name(name)
                .price(price)
                .image1(image1)
                .image2(image2)
                .image3(image3)
                .sizes(sizes)
                .colors(colors)
                .types(ids)
                .build();

        return ResponseEntity.ok(productService.updateProduct(slug, productRequest));
    }

    @PostMapping("/delete/{slug}")
    public ResponseEntity<?> deleteProduct(@PathVariable("slug") String slug){
        boolean status = productService.deleteByLug(slug);
        if(status){
            return ResponseEntity.ok("Delete success");
        }
        else{
            return ResponseEntity.badRequest().body("Not found product by slug");
        }
    }

    @GetMapping("/type/{slug}")
    public ResponseEntity<?> findByType(@PathVariable("slug") String slug){
        return ResponseEntity.ok(productService.findByTypeSlug(slug));
    }
}
