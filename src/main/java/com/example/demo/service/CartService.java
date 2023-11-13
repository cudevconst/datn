package com.example.demo.service;

import com.example.demo.dto.request.CartRequest;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<CartResponse> findByUserId(Long userId){
        List<Cart> cartList = cartRepository.findByUserId(userId);
        List<CartResponse> cartResponses = new ArrayList<>();
        for(Cart c : cartList){
            ProductResponse productResponse = ProductResponse.builder()
                    .id(c.getProduct().getId())
                    .slug(c.getProduct().getSlug())
                    .price(c.getProduct().getPrice())
                    .image1(c.getProduct().getImage1())
                    .image2(c.getProduct().getImage2())
                    .image3(c.getProduct().getImage3())
                    .name(c.getProduct().getName())
                    .types(c.getProduct().getTypes())
                    .colors(c.getProduct().getColor())
                    .sizes(c.getProduct().getSizes())
                    .build();
            CartResponse cartResponse = CartResponse.builder()
                    .id(c.getId())
                    .color(c.getColor())
                    .size(c.getSize())
                    .quantity(c.getQuantity())
                    .productResponse(productResponse)
                    .build();
            cartResponses.add(cartResponse);
        }
        return cartResponses;
    }

    @Transactional
    public CartResponse addProductToCart(HttpSession session, CartRequest cartRequest){
        User user = (User) session.getAttribute("user");
//        User user = userRepository.findById(cartRequest.getUserId()).orElse(null);
        Product product = productRepository.findById(cartRequest.getProductId()).orElse(null);
        Cart cart = Cart.builder()
                .color(cartRequest.getColor())
                .size(cartRequest.getSize())
                .quantity(cartRequest.getQuantity())
                .user(user)
                .product(product)
                .build();
        cartRepository.save(cart);
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
        CartResponse cartResponse = CartResponse.builder()
                .id(cart.getId())
                .color(cart.getColor())
                .size(cart.getSize())
                .quantity(cart.getQuantity())
                .productResponse(productResponse)
                .build();
        return cartResponse;
    }

    @Transactional
    public boolean deleteProductToCart(Long cartId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart != null){
            cartRepository.delete(cart);
            return true;
        }
        else{
            return false;
        }

    }
}
