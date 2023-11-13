package com.example.demo.api;

import com.example.demo.dto.request.InvoiceRequest;
import com.example.demo.service.OrderSerive;
import com.example.demo.service.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(path = "/api/payment")
public class VNPayAPI {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private OrderSerive orderSerive;
    @PostMapping("/create")
    private ResponseEntity<?> createPayment(@RequestBody InvoiceRequest invoiceRequest, HttpSession session, HttpServletRequest request) throws UnsupportedEncodingException {
        return ResponseEntity.ok(vnPayService.createPayment(invoiceRequest, session, request));
    }

    @GetMapping("/payment-info")
    private ResponseEntity<?> paymentInfo(@RequestParam("vnp_ResponseCode") String responseCode,
                                          @RequestParam("vnp_TransactionStatus") String transactionStatus,
                                          @RequestParam("vnp_OrderInfo") String orderId){
        Map<Object, Object> mapResults = vnPayService.paymentInfo(responseCode, transactionStatus, orderId);
        Integer statusCode = (Integer) mapResults.get("status");
        if(statusCode == 200){
            return ResponseEntity.ok(mapResults);
        }
        else {
            return ResponseEntity.badRequest().body(mapResults);
        }
    }
}
