package com.example.demo.service;

import com.example.demo.dto.request.InvoiceRequest;
import com.example.demo.dto.request.OrderProductRequest;
import com.example.demo.dto.request.OrderRequest;
import com.example.demo.dto.response.InvoiceResponse;
import com.example.demo.dto.response.OrderProductResponse;
import com.example.demo.dto.response.OrderResponse;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

@Service
public class OrderSerive {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;
    public List<OrderResponse> findAll(HttpSession session){
        User user = (User) session.getAttribute("user");
        List<Order> orders = orderRepository.findAllByUserId(user.getId());
        List<OrderResponse> orderResponses = new ArrayList<>();
        for(int i = orders.size() - 1; i>=0; i--){
            orderResponses.add(entityToResponse(orders.get(i)));
        }
        return orderResponses;
    }
    @Transactional
    public InvoiceResponse addNewOrder(InvoiceRequest invoiceRequest, HttpSession session){
        User user = (User) session.getAttribute("user");
        List<Long> prodcutIds = new ArrayList<>();
        OrderRequest orderRequest = invoiceRequest.getOrder();
        for(OrderProductRequest orderprodcut : orderRequest.getOrderProduct()){
            prodcutIds.add(orderprodcut.getProductId());
        }
        List<Product> products = productRepository.findByIdIn(prodcutIds);
        double amout = 0;
        for(int i = 0; i < products.size(); i ++){
            amout += products.get(i).getPrice() * invoiceRequest.getOrder().getOrderProduct().get(i).getQuantity();
        }
        Order order = Order.builder()
                .user(user)
                .city(orderRequest.getCity())
                .district(orderRequest.getDistrict())
                .createAt(LocalDateTime.now())
                .wards(orderRequest.getWards())
                .addressInfo(orderRequest.getAddressInfo())
                .status("1")
                .amount(amout)
                .phoneNumber(orderRequest.getPhoneNumber())
                .build();
        orderRepository.save(order);
        List<OrderProduct> orderProducts = new ArrayList<>();
        for(OrderProductRequest o : orderRequest.getOrderProduct()){
            Product product = productRepository.findById(o.getProductId()).orElse(null);
            OrderProduct orderProduct = OrderProduct.builder()
                    .order(order)
                    .product(product)
                    .quantity(o.getQuantity())
                    .color(o.getColor())
                    .size(o.getSize())
                    .build();
            orderProducts.add(orderProduct);
        }
        orderProductRepository.saveAll(orderProducts);
        order.setOrderProducts(orderProducts);
        Invoice invoice = Invoice.builder()
                .amout(amout)
                .order(order)
                .coupon(invoiceRequest.getCoupon())
                .transaction(invoiceRequest.getTransaction())
                .type(invoiceRequest.getType())
                .status(invoiceRequest.getStatus())
                .createAt(LocalDateTime.now())
                .build();
        invoiceRepository.save(invoice);
        InvoiceResponse invoiceResponse = InvoiceResponse.builder()
                .id(invoice.getId())
                .amout(invoice.getAmout())
                .status(invoice.getStatus())
                .coupon(invoice.getCoupon())
                .type(invoice.getType())
                .transaction(invoice.getTransaction())
                .createAt(invoice.getCreateAt())
                .order(entityToResponse(order))
                .build();
        return invoiceResponse;
    }

    @Transactional
    public boolean deleteOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order != null){
            orderRepository.delete(order);
            return true;
        }
        else{
            return false;
        }

    }

    @Transactional
    public OrderResponse updateStatus(Long id, String status){
        Order order = orderRepository.findById(id).orElse(null);
        order.setStatus(status);
        orderRepository.save(order);
        OrderResponse orderResponse = entityToResponse(order);
        return orderResponse;

    }
    public List<OrderResponse> findByStatus(String status){
        List<Order> orders = orderRepository.findAllByStatus(status);
        List<OrderResponse> orderResponses = new ArrayList<>();
        for(Order order : orders){
            orderResponses.add(entityToResponse(order));
        }
        return orderResponses;
    }
    protected OrderResponse entityToResponse(Order order){
        List<OrderProductResponse> orderProductResponses = new ArrayList<>();
        for(OrderProduct o : order.getOrderProducts()){
            Product product = o.getProduct();
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
            OrderProductResponse orderProductResponse = OrderProductResponse.builder()
                    .id(o.getId())
                    .quantity(o.getQuantity())
                    .product(productResponse)
                    .size(o.getSize())
                    .color(o.getColor())
                    .build();
            orderProductResponses.add(orderProductResponse);
        }
        OrderResponse orderResponse = OrderResponse.builder()
                .id(order.getId())
                .amout(order.getAmount())
                .createAt(order.getCreateAt())
                .status(order.getStatus())
                .product(orderProductResponses)
                .city(order.getCity())
                .district(order.getDistrict())
                .wards(order.getWards())
                .addressInfo(order.getAddressInfo())
                .phoneNumber(order.getPhoneNumber())
                .build();

        return orderResponse;
    }

}
