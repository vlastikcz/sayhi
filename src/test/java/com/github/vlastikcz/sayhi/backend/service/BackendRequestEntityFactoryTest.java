package com.github.vlastikcz.sayhi.backend.service;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static org.junit.Assert.*;

public class BackendRequestEntityFactoryTest {

    @Test(expected = NullPointerException.class)
    public void givenCreateRequestEntity_whenCallbackUriIsNull_thenThrowException() throws Exception {
        BackendRequestEntityFactory.createRequestEntity(null);
    }

    @Test
    public void givenCreateRequestEntity_whenCallbackUriIsProvided_thenSetValidHeaders() throws Exception {
        final String callbackUri = "callbackUri";
        final HttpEntity<String> actual = BackendRequestEntityFactory.createRequestEntity(callbackUri);

        final String expectedCallbackHeader = "<" + callbackUri + ">; method=\"POST\"; rel=\"redirect\"";
        final HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.setPragma(BackendRequestEntityFactory.CALLBACK_PRAGMA);

        assertEquals(actual.getHeaders().getPragma(), expectedHeaders.getPragma());
        assertTrue(actual.getHeaders().get(BackendRequestEntityFactory.CALLBACK_HEADER_KEY).size() == 1);
        assertTrue(actual.getHeaders().get(BackendRequestEntityFactory.CALLBACK_HEADER_KEY).get(0).equalsIgnoreCase(expectedCallbackHeader));
    }
}