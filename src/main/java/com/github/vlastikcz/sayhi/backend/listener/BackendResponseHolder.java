package com.github.vlastikcz.sayhi.backend.listener;

import java.util.UUID;

public interface BackendResponseHolder {

    void addResponseListener(BackendResponseListener listener);

    void removeResponseListener(BackendResponseListener listener);

    void handleResponse(UUID responseUid, String response);
}
