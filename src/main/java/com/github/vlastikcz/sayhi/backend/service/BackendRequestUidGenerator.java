package com.github.vlastikcz.sayhi.backend.service;

import java.util.UUID;

public interface BackendRequestUidGenerator {
    UUID findRandomValue();
}
