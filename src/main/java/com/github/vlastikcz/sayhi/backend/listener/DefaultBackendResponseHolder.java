package com.github.vlastikcz.sayhi.backend.listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class DefaultBackendResponseHolder implements BackendResponseHolder {
    private final Set<BackendResponseListener> listeners = new HashSet<>();

    @Override
    public void addResponseListener(BackendResponseListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeResponseListener(BackendResponseListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    @Override
    public void handleResponse(UUID responseUid, String response) {
        broadcast(responseUid, response);
    }

    private void broadcast(UUID responseUid, String response) {
        createListenerSnapshot().stream().forEach(l -> l.responseReceived(responseUid, response));
    }

    private Set<BackendResponseListener> createListenerSnapshot() {
        Set<BackendResponseListener> snapshot;
        synchronized (listeners) {
            snapshot = new HashSet<>(listeners);
        }
        return snapshot;
    }

}
