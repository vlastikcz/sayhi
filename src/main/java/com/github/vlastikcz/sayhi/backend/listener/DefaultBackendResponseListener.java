package com.github.vlastikcz.sayhi.backend.listener;

import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public class DefaultBackendResponseListener implements BackendResponseListener {
    private final Logger logger = LoggerFactory.getLogger(DefaultBackendResponseListener.class);

    private final DeferredResult<ResponseEntity<String>> deferredResult;
    private final UUID operationUid;

    public DefaultBackendResponseListener(DeferredResult<ResponseEntity<String>> deferredResult, UUID operationUid) {
        this.deferredResult = Objects.requireNonNull(deferredResult, "'deferredResult' cannot be null");
        this.operationUid = Objects.requireNonNull(operationUid, "'operationUid' cannot be null");
    }

    @Override
    public void responseReceived(UUID responseUid, String response) {
        if (Objects.equals(operationUid, responseUid)) {
            finishDeferredResult(response);
        }
    }

    private void finishDeferredResult(String response) {
        logger.debug("action.call=finishDeferredResult, arguments=[{}]", response);
        deferredResult.setResult(new ResponseEntity<>(response, HttpStatus.OK));
    }
}
