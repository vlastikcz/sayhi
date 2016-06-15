package com.github.vlastikcz.sayhi.backend.service.remote;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

public class DefaultRemoteBackendServiceTest {
    DefaultRemoteBackendService defaultRemoteBackendService;
    RestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate = EasyMock.createMock(RestTemplate.class);
        defaultRemoteBackendService = new DefaultRemoteBackendService(restTemplate);
    }

    @Test
    public void givenExchange_whenResponseStatusIsAccepted_thenReturnTrue() throws Exception {
        final String url = "url";
        final HttpEntity<String> httpEntity = new HttpEntity<>("httpEntity");
        final ResponseEntity<String> responseEntity = new ResponseEntity<>("responseEntity", HttpStatus.ACCEPTED);
        expect(restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class)).andReturn(responseEntity);
        replay();
        final boolean actual = defaultRemoteBackendService.exchange(url, httpEntity);
        assertTrue(actual);
    }

    @Test
    public void givenExchange_whenResponseStatusNotIsAccepted_thenReturnFalse() throws Exception {
        final String url = "url";
        final HttpEntity<String> httpEntity = new HttpEntity<>("httpEntity");
        final ResponseEntity<String> responseEntity = new ResponseEntity<>("responseEntity", HttpStatus.BAD_REQUEST);
        expect(restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class)).andReturn(responseEntity);
        replay();
        final boolean actual = defaultRemoteBackendService.exchange(url, httpEntity);
        assertFalse(actual);
    }

    public void replay() {
        EasyMock.replay(restTemplate);
    }

    @After
    public void verify() {
        EasyMock.verify(restTemplate);
    }
}