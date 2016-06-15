package com.github.vlastikcz.sayhi.backend.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.vlastikcz.sayhi.backend.listener.BackendResponseHolder;

@RestController
public class BackendCallbackController {
    public static final String BACKEND_CALLBACK_CONTROLLER_PATH = "/callback/{responseUid}";

    private final BackendResponseHolder backendResponseHolder;

    public BackendCallbackController(BackendResponseHolder backendResponseHolder) {
        this.backendResponseHolder = backendResponseHolder;
    }

    @RequestMapping(value = BACKEND_CALLBACK_CONTROLLER_PATH, method = RequestMethod.POST)
    public ResponseEntity<String> createNewResponse(@PathVariable UUID responseUid, @RequestBody String requestBody) {
        backendResponseHolder.handleResponse(responseUid, requestBody);
        return new ResponseEntity<>(requestBody, HttpStatus.OK);
    }
}
