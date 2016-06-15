package com.github.vlastikcz.sayhi.backend.service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import com.github.vlastikcz.sayhi.backend.controller.BackendCallbackController;
import com.github.vlastikcz.sayhi.backend.service.remote.RemoteBackendService;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@Service
public class DefaultBackendRequestService implements BackendRequestService, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(DefaultBackendRequestService.class);

    private final RemoteBackendService remoteBackendService;
    private final BackendRequestUidGenerator backendRequestUidGenerator;

    @Value("${com.github.vlastikcz.sayhi.backendUrl}")
    private String endpointUrl;

    public DefaultBackendRequestService(RemoteBackendService remoteBackendService, BackendRequestUidGenerator backendRequestUidGenerator) {
        this.remoteBackendService = remoteBackendService;
        this.backendRequestUidGenerator = backendRequestUidGenerator;
    }

    @Override
    public Optional<UUID> sendAsynchronousRequest() {
        logger.debug("action.call=sendAsynchronousRequest");
        final UUID requestUid = backendRequestUidGenerator.findRandomValue();
        final String callbackUri = findCallbackUri(requestUid);
        final HttpEntity<String> requestEntity = BackendRequestEntityFactory.createRequestEntity(callbackUri);
        final boolean exchangeResult = remoteBackendService.exchange(endpointUrl, requestEntity);
        final Optional<UUID> result = exchangeResult == true ? Optional.of(requestUid) : Optional.empty();
        logger.debug("action.result=sendAsynchronousRequest, result=[{}]", result);
        return result;
    }

    private static String findCallbackUri(UUID requestUid) {
        final UriComponents uriComponents = MvcUriComponentsBuilder
                .fromMethodCall(on(BackendCallbackController.class).createNewResponse(requestUid, null))
                .buildAndExpand();
        return uriComponents.encode().toUriString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Objects.requireNonNull(endpointUrl, "'endpointUrl' cannot be null");
    }
}
