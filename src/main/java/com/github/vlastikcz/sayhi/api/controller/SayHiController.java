package com.github.vlastikcz.sayhi.api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.github.vlastikcz.sayhi.backend.listener.BackendResponseHolder;
import com.github.vlastikcz.sayhi.backend.listener.BackendResponseListener;
import com.github.vlastikcz.sayhi.backend.listener.DefaultBackendResponseListener;
import com.github.vlastikcz.sayhi.backend.service.BackendRequestService;

@RestController
public class SayHiController {
    private static final long MILLISECONDS_IN_A_SECOND = 1000;

    @Value("${com.github.vlastikcz.sayhi.requestTimeoutSeconds}")
    private long deferredResultTimeoutInSeconds;

    private final BackendRequestService backendRequestService;
    private final BackendResponseHolder backendResponseHolder;

    final static String SAY_HI_CONTROLLER_PATH = "/api/say-hi";

    @Autowired
    public SayHiController(BackendRequestService backendRequestService, BackendResponseHolder backendResponseHolder) {
        this.backendRequestService = backendRequestService;
        this.backendResponseHolder = backendResponseHolder;
    }

    @RequestMapping(value = SAY_HI_CONTROLLER_PATH)
    public DeferredResult<ResponseEntity<String>> sayHi() {
        return backendRequestService.sendAsynchronousRequest()
                .map(uuid -> waitForBackEndResponse(uuid))
                .orElseGet(() -> backendError());
    }

    private DeferredResult<ResponseEntity<String>> waitForBackEndResponse(UUID requestUid) {
        final long deferredResultTimeout = findDeferredResultTimeout();
        final DeferredResult<ResponseEntity<String>> response = new DeferredResult<>(deferredResultTimeout, responseTimeout());
        final BackendResponseListener backendResponseListener = new DefaultBackendResponseListener(response, requestUid);

        backendResponseHolder.addResponseListener(backendResponseListener);
        response.onCompletion(() -> backendResponseHolder.removeResponseListener(backendResponseListener));
        return response;
    }

    private DeferredResult<ResponseEntity<String>> backendError() {
        final long deferredResultTimeout = findDeferredResultTimeout();
        final DeferredResult<ResponseEntity<String>> response = new DeferredResult<>(deferredResultTimeout, responseTimeout());
        response.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        return response;
    }

    private static ResponseEntity<String> responseTimeout() {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private long findDeferredResultTimeout() {
        return MILLISECONDS_IN_A_SECOND * deferredResultTimeoutInSeconds;
    }
}
