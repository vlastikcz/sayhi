package com.github.vlastikcz.sayhi.backend.service;

import java.util.Optional;
import java.util.UUID;

public interface BackendRequestService {
    Optional<UUID> sendAsynchronousRequest();
}
