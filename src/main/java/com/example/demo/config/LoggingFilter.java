package com.example.demo.config;


import javax.servlet.*;
import javax.servlet.FilterConfig;
import java.io.IOException;

public class LoggingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        // Chuyển tiếp yêu cầu đến filter tiếp theo hoặc controller/servlet
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("Request received at: " + System.currentTimeMillis());
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
