package com.github.vlastikcz.sayhi.api.controller;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.vlastikcz.sayhi.ApplicationConfiguration;
import com.github.vlastikcz.sayhi.backend.service.BackendRequestUidGenerator;

@Configuration
public class SayHiControllerTestConfiguration {
    final static UUID REQUEST_UUID = UUID.fromString("0a401dd6-7e09-4e86-a300-bc660a554c23");

    @Bean
    @Profile(ApplicationConfiguration.PROFILE_TEST)
    public BackendRequestUidGenerator requestUidGenerator() {
        return () -> REQUEST_UUID;
    }
}
