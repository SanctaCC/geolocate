package com.geolocation.mongodb.user.config;

import com.geolocation.mongodb.user.UserController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class ITServletTest {

    @Autowired
    WebApplicationContext applicationContext;

    MockMvc mockMvc;

    @Before
    public void before(){
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }

    @Test
    public void everythingReturnsIndexHtml() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().is3xxRedirection()).andDo(print())
                .andExpect(redirectedUrl("/index.html"));

        mockMvc.perform(get("/any/url/ocxnwefwe/asdka")).andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(handler().handlerType(ResourceHttpRequestHandler.class));

        mockMvc.perform(patch("/any.sad$23/url/admin/.fas")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(handler().handlerType(ResourceHttpRequestHandler.class));
    }

    @Test
    public void apiWithHigherPrecedence() throws Exception {
        mockMvc.perform(get("/api/404path/url")).andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(handler().handlerType(UserController.class));

        mockMvc.perform(get("/api/users")).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}