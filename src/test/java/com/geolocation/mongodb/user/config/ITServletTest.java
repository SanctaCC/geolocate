package com.geolocation.mongodb.user.config;

import com.geolocation.mongodb.location.LocationProvider;
import com.geolocation.mongodb.user.UserController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ITServletTest {

    @Autowired
    WebApplicationContext applicationContext;

    @MockBean private LocationProvider locationProvider;

    MockMvc mockMvc;

    final Point point = new Point(30, 50);

    @Before
    public void before(){
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        Mockito.when(locationProvider.getOne("8.8.8.8")).thenReturn(point);
    }


    @Test
    public void locationIsSet() throws Exception {
        //test location for 1st request
        mockMvc.perform(get("/any-url").with(getRequestPostProcessor()))
                .andExpect(request().attribute("location",point));
        //test for subsequent requests
        mockMvc.perform(get("/any-url").with(getRequestPostProcessor()))
                .andExpect(request().attribute("location",point));

        Mockito.verify(locationProvider,times(1)).getOne("8.8.8.8");
    }

    @Test
    public void everythingReturnsIndexHtml() throws Exception {

        mockMvc.perform(get("/").with(getRequestPostProcessor()))
                .andExpect(status().is3xxRedirection()).andDo(print())
                .andExpect(redirectedUrl("/index.html"));

        mockMvc.perform(get("/any/url/ocxnwefwe/asdka").with(getRequestPostProcessor()))
                .andExpect(status().isOk()).andDo(print()).andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(handler().handlerType(ResourceHttpRequestHandler.class));

        mockMvc.perform(patch("/any.sad$23/url/admin/.fas").with(getRequestPostProcessor()))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(handler().handlerType(ResourceHttpRequestHandler.class));
    }

    private RequestPostProcessor getRequestPostProcessor() {
        return request -> {
                request.setRemoteAddr("8.8.8.8");
                return request;
            };
    }

    @Test
    public void apiWithHigherPrecedence() throws Exception {
        mockMvc.perform(get("/api/404path/url").with(getRequestPostProcessor()))
                .andExpect(status().isNotFound()).andDo(print())
                .andExpect(handler().handlerType(UserController.class));

        mockMvc.perform(get("/api/users").with(getRequestPostProcessor()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}