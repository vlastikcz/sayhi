package com.github.vlastikcz.sayhi.backend.service.remote;

import org.springframework.http.HttpEntity;

public interface RemoteBackendService {
    boolean exchange(String endpointUrl, HttpEntity<String> requestEntity);
}
