package com.github.vlastikcz.sayhi.backend.service.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.github.vlastikcz.sayhi.backend.service.DefaultBackendRequestService;

@Service
public class DefaultRemoteBackendService implements RemoteBackendService {
    private final static Logger logger = LoggerFactory.getLogger(DefaultBackendRequestService.class);

    private final RestTemplate restTemplate;

    public DefaultRemoteBackendService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean exchange(String endpointUrl, HttpEntity<String> requestEntity) {
        logger.debug("action.call=exchange, arguments=[{}, {}]", endpointUrl, requestEntity);
        boolean result = false;
        try {
            final ResponseEntity<String> exchangeResult = restTemplate.exchange(endpointUrl, HttpMethod.GET, requestEntity, String.class);
            logger.debug("action.message=exchange, message=[status '{}' received]", exchangeResult.getStatusCode());
            result = exchangeResult.getStatusCode() == HttpStatus.ACCEPTED;
        } catch (HttpServerErrorException | ResourceAccessException e) {
            logger.error("action.fail=exchange", e);
        }
        return result;
    }
}
