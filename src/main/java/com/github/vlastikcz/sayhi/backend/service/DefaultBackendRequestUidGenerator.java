package com.github.vlastikcz.sayhi.backend.service;

import java.util.UUID;

public class DefaultBackendRequestUidGenerator implements BackendRequestUidGenerator {
    @Override
    public UUID findRandomValue() {
        return UUID.randomUUID();
    }
}
