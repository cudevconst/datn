package com.example.demo.service;

import com.example.demo.config.VNPayConfig;
import com.example.demo.dto.request.InvoiceRequest;
import com.example.demo.dto.response.InvoiceResponse;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class VNPayService {
    @Autowired
    private OrderSerive orderSerive;

    public String createPayment(InvoiceRequest invoiceRequest, HttpSession session,  HttpServletRequest request) throws UnsupportedEncodingException {
        InvoiceResponse invoiceResponse = orderSerive.addNewOrder(invoiceRequest, session);
        Long amout = invoiceResponse.getAmout().longValue();
        String serverUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String returnUrl = serverUrl + VNPayConfig.vnp_Returnurl;
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amout * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", invoiceResponse.getOrder().getId().toString());
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_OrderType", "order-type");
        Instant currentTimestamp = Instant.now();
        ZoneId zoneId = ZoneId.systemDefault(); // Múi giờ tại máy tính
        ZoneId targetZoneId = ZoneId.of("GMT+7"); // Múi giờ +7
        ZonedDateTime currentDateTime = currentTimestamp.atZone(zoneId);
        ZonedDateTime createDateTime = currentDateTime.withZoneSameInstant(targetZoneId);
        ZonedDateTime expireDateTime = createDateTime.plusMinutes(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = createDateTime.format(formatter);
        String vnp_ExpireDate = expireDateTime.format(formatter);

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
    }
    public Map<Object, Object> paymentInfo(String responseCode, String transactionStatus, String orderInfo){
        Map<Object, Object> map = new HashMap<>();
        if(responseCode.equals("00") && transactionStatus.equals("00")){
            map.put("message", "Payment success");
            map.put("paymentOrder", orderInfo);
            map.put("status", 200);
        }
        else{
            map.put("message", "Payment fail");
            map.put("status", 500);
        }

        return map;
    }
}
