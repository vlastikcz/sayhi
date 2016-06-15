package com.github.vlastikcz.sayhi.api.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.github.vlastikcz.sayhi.Application;
import com.github.vlastikcz.sayhi.ApplicationConfiguration;
import com.github.vlastikcz.sayhi.backend.controller.BackendCallbackController;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ActiveProfiles(ApplicationConfiguration.PROFILE_TEST)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@WebAppConfiguration
public class SayHiControllerTest {
    private final static String BACKEND_SERVER_RESPONSE = "BACKEND_SERVER_RESPONSE";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private RestTemplate restTemplate;

    private MockMvc mockMvc;
    private MockRestServiceServer mockRestServiceServer;

    @Before
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void givenSayHi_whenTheBackEndServiceReturnsError_thenReturnInternalServerError() throws Exception {
        mockRestServiceServer.expect(requestTo("http://127.0.0.1/backend"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        final MvcResult mvcResult = this.mockMvc.perform(get(SayHiController.SAY_HI_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isInternalServerError());
    }

    @Test
    public void givenSayHi_whenTheBackEndServerResponds_thenReturnTheResponse() throws Exception {
        mockRestServiceServer.expect(requestTo("http://127.0.0.1/backend"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.ACCEPTED));

        final MvcResult mvcResult = this.mockMvc.perform(get(SayHiController.SAY_HI_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andReturn();

        final RequestBuilder callbackRequestBuilder = post(
                BackendCallbackController.BACKEND_CALLBACK_CONTROLLER_PATH,
                SayHiControllerTestConfiguration.REQUEST_UUID).content(BACKEND_SERVER_RESPONSE);
        this.mockMvc.perform(callbackRequestBuilder).andExpect(status().isOk());

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string(BACKEND_SERVER_RESPONSE));
        mockRestServiceServer.verify();
    }
}