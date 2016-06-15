package com.github.vlastikcz.sayhi.backend.service;

import java.util.Objects;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;

class BackendRequestEntityFactory {
    static final String CALLBACK_PRAGMA = "redirect";
    static final String CALLBACK_HEADER_KEY = "Callback";

    static HttpEntity<String> createRequestEntity(String callbackUri) {
        Objects.requireNonNull(callbackUri, "'callbackUri' cannot be null");
        final HttpHeaders headers = new HttpHeaders();
        headers.setPragma(CALLBACK_PRAGMA);
        headers.set(CALLBACK_HEADER_KEY, createCallbackHeader(callbackUri));
        return new HttpEntity<>(headers);
    }

    private static String createCallbackHeader(String callbackUri) {
        return String.format("<%s>; method=\"%s\"; rel=\"%s\"", callbackUri, RequestMethod.POST, CALLBACK_PRAGMA);
    }
}
