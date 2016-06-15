package com.github.vlastikcz.sayhi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.github.vlastikcz.sayhi.backend.service.BackendRequestUidGenerator;
import com.github.vlastikcz.sayhi.backend.service.DefaultBackendRequestUidGenerator;

@Configuration
public class ApplicationConfiguration {
    public static final String PROFILE_PRODUCTION = "production";
    public static final String PROFILE_TEST = "test";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Profile(PROFILE_PRODUCTION)
    public BackendRequestUidGenerator requestUidGenerator() {
        return new DefaultBackendRequestUidGenerator();
    }
}
