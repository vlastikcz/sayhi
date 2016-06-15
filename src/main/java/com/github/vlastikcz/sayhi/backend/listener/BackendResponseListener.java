package com.github.vlastikcz.sayhi.backend.listener;

import java.util.UUID;

public interface BackendResponseListener {
    void responseReceived(UUID responseUid, String response);
}
