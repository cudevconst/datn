package com.example.demo.service;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.entity.Product;
import com.example.demo.entity.Type;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    public ProductRepository productRepository;


    @Autowired
    private TypeRepository typeRepository;
    public List<ProductResponse> findAllProduct(){
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponseList = new ArrayList<>();
        for(Product p : products){
            ProductResponse productResponse = ProductResponse.builder()
                    .id(p.getId())
                    .slug(p.getSlug())
                    .price(p.getPrice())
                    .image1(p.getImage1())
                    .image2(p.getImage2())
                    .image3(p.getImage3())
                    .name(p.getName())
                    .types(p.getTypes())
                    .sizes(p.getSizes())
                    .colors(p.getColor())
                    .build();
            productResponseList.add(productResponse);
        }

        return productResponseList;
    }
    public List<ProductResponse> findByIds(List<Long> ids){
        List<Product> products = productRepository.findByIdIn(ids);
        List<ProductResponse> productResponseList = new ArrayList<>();
        for(Product p : products){
            ProductResponse productResponse = ProductResponse.builder()
                    .id(p.getId())
                    .slug(p.getSlug())
                    .price(p.getPrice())
                    .image1(p.getImage1())
                    .image2(p.getImage2())
                    .image3(p.getImage3())
                    .name(p.getName())
                    .types(p.getTypes())
                    .sizes(p.getSizes())
                    .colors(p.getColor())
                    .build();
            productResponseList.add(productResponse);
        }

        return productResponseList;
    }
    public ProductResponse findProductBySlug(String slug){
        Product product = productRepository.findBySlug(slug).orElse(null);
        if(product != null){
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
            return productResponse;
        }
        return null;

    }

    public List<ProductResponse> findByTypeId(Long typeId){
        List<Product> products = productRepository.findByTypeId(typeId);
        List<ProductResponse> productResponseList = new ArrayList<>();
        for(Product p : products){
            ProductResponse productResponse = ProductResponse.builder()
                    .id(p.getId())
                    .slug(p.getSlug())
                    .price(p.getPrice())
                    .image1(p.getImage1())
                    .image2(p.getImage2())
                    .image3(p.getImage3())
                    .name(p.getName())
                    .types(p.getTypes())
                    .build();
            productResponseList.add(productResponse);
        }

        return productResponseList;
    }

    public List<ProductResponse> findByTypeSlug(String slug){
        Type type = typeRepository.findBySlug(slug).orElse(null);
        List<Product> products = productRepository.findByTypeId(type.getId());
        List<ProductResponse> productResponseList = new ArrayList<>();
        for(Product p : products){
            ProductResponse productResponse = ProductResponse.builder()
                    .id(p.getId())
                    .slug(p.getSlug())
                    .price(p.getPrice())
                    .image1(p.getImage1())
                    .image2(p.getImage2())
                    .image3(p.getImage3())
                    .name(p.getName())
                    .types(p.getTypes())
                    .build();
            productResponseList.add(productResponse);
        }

        return productResponseList;
    }
    @Transactional
    public ProductResponse save(ProductRequest productRequest){
        List<Type> types = typeRepository.findByIdIn(productRequest.getTypes());
        Product product = Product.builder()
                .name(productRequest.getName())
                .image1(productRequest.getImage1())
                .image2(productRequest.getImage2())
                .image3(productRequest.getImage3())
                .price(productRequest.getPrice())
                .types(types)
                .sizes(productRequest.getSizes())
                .color(productRequest.getColors())
                .build();
        productRepository.save(product);
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
        return productResponse;
    }

    @Transactional
    public ProductResponse updateProduct(String slug, ProductRequest productRequest){
        Product product = productRepository.findBySlug(slug).orElse(null);
        if(product != null){
            List<Type> types = typeRepository.findByIdIn(productRequest.getTypes());
            product.setName(productRequest.getName());
            product.setPrice(productRequest.getPrice());
            product.setImage1(productRequest.getImage1());
            product.setImage2(productRequest.getImage2());
            product.setImage3(productRequest.getImage3());
            product.setColor(productRequest.getColors());
            product.setSizes(productRequest.getSizes());
            product.setTypes(types);
            productRepository.save(product);
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
            return productResponse;
        }
        return null;
    }

    @Transactional
    public boolean deleteByLug(String slug){
        Product product = productRepository.findBySlug(slug).orElse(null);
        if(product != null){
            productRepository.delete(product);
            return true;
        }
        return false;
    }
}
