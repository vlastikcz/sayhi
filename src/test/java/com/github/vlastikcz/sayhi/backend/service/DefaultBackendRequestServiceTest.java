package com.github.vlastikcz.sayhi.backend.service;

import java.util.Optional;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.vlastikcz.sayhi.backend.service.remote.RemoteBackendService;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

public class DefaultBackendRequestServiceTest {
    DefaultBackendRequestService defaultBackendRequestService;
    RemoteBackendService remoteBackendService;

    @Before
    public void setup() {
        final BackendRequestUidGenerator backendRequestUidGenerator = new DefaultBackendRequestUidGenerator();
        remoteBackendService = EasyMock.createMock(RemoteBackendService.class);
        defaultBackendRequestService = new DefaultBackendRequestService(remoteBackendService, backendRequestUidGenerator);
    }

    @Test
    public void giveSendAsynchronousRequest_whenTheRemoteBackendResponds_thenReturnUUID() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        expect(remoteBackendService.exchange(anyString(), anyObject())).andReturn(true);
        replay();
        final Optional<UUID> actual = defaultBackendRequestService.sendAsynchronousRequest();
        assertTrue(actual.isPresent());
    }

    @Test
    public void giveSendAsynchronousRequest_whenTheRemoteBackendDoesNotRespondsCorrectly_thenReturnEmptyOptional() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        expect(remoteBackendService.exchange(anyString(), anyObject())).andReturn(false);
        replay();
        final Optional<UUID> actual = defaultBackendRequestService.sendAsynchronousRequest();
        assertFalse(actual.isPresent());
    }

    public void replay() {
        EasyMock.replay(remoteBackendService);
    }

    @After
    public void verify() {
        EasyMock.verify(remoteBackendService);
    }
}