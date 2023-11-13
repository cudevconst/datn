package com.example.demo.service;

import com.example.demo.dto.response.InvoiceResponse;
import com.example.demo.dto.response.OrderResponse;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.repository.InvoiceRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderSerive orderSerive;


    public List<InvoiceResponse> findByOrderIdIn(List<Long> orderId){
        List<Invoice> invoices = invoiceRepository.findByOrderIdIn(orderId);
        List<InvoiceResponse> invoiceResponses = new ArrayList<>();
        for(Invoice i : invoices){
            Order order = i.getOrder();
            OrderResponse orderResponse = orderSerive.entityToResponse(order);

            InvoiceResponse invoiceResponse = InvoiceResponse.builder()
                    .id(i.getId())
                    .createAt(i.getCreateAt())
                    .amout(i.getAmout())
                    .type(i.getType())
                    .coupon(i.getCoupon())
                    .status(i.getStatus())
                    .transaction(i.getTransaction())
                    .order(orderResponse)
                    .build();
            invoiceResponses.add(invoiceResponse);
        }
        return invoiceResponses;
    }

    public List<InvoiceResponse> findOrderByUser(HttpSession session){
        List<OrderResponse> orderResponses = orderSerive.findAll(session);
        List<Long> orderIds = new ArrayList<>();
        for(OrderResponse o : orderResponses){
            orderIds.add(o.getId());
        }
        List<InvoiceResponse> invoiceResponses = findByOrderIdIn(orderIds);
        return invoiceResponses;
    }

    @Autowired
    private UserRepository userRepository;
    public List<InvoiceResponse> findOrderByUserTest(Long userId, HttpSession session){
        User user = userRepository.findById(userId).orElse(null);
        session.setAttribute("user", user);
        List<OrderResponse> orderResponses = orderSerive.findAll(session);
        List<Long> orderIds = new ArrayList<>();
        for(OrderResponse o : orderResponses){
            orderIds.add(o.getId());
        }
        List<InvoiceResponse> invoiceResponses = findByOrderIdIn(orderIds);
        return invoiceResponses;
    }
}
