package com.infra.configuration;


import com.infra.interceptor.CustomMybatisPlusInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomMybatisPlusConfiguration {
    @Bean
    @ConditionalOnProperty(value = {"sql.beauty.show"},havingValue = "true",matchIfMissing = true)
    public CustomMybatisPlusInterceptor mybatisPlusInterceptor() {
        return new CustomMybatisPlusInterceptor();
    }
}
