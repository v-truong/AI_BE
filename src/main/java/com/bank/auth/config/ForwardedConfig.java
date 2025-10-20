package com.bank.auth.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class ForwardedConfig {
    @Bean
public ForwardedHeaderFilter forwardedHeaderFilter() {
    // Cho phép Spring hiểu đúng URL public khi chạy sau reverse proxy
    return new ForwardedHeaderFilter();
}
    
}
