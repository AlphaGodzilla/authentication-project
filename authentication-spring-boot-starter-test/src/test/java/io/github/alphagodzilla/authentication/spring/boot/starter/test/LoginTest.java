package io.github.alphagodzilla.authentication.spring.boot.starter.test;

import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author AlphaGodzilla
 * @date 2022/3/16 11:45
 */
@SpringBootTest(classes = AuthenticationExampleApp.class)
@AutoConfigureMockMvc
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    private MockHttpServletRequestBuilder requestBuilder;

    @BeforeEach
    public void setUp() throws Exception {
        requestBuilder = post("/api/public/login").contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    @Test
    public void normalLogin() throws Exception {
        requestBuilder.content("username=user1&password=user1");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attributes.uid").value("user1"));
    }

    @Test
    public void notExistUserLoginTest() throws Exception {
        requestBuilder.content("username=notExistUserName&password=user");
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(401))
                .andExpect(jsonPath("$.error").value(
                        StringContains.containsString("subject not exist"))
                );
    }

    @Test
    public void mistakePasswordLoginTest() throws Exception {
        requestBuilder.content("username=user1&password=mistakePassword");
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(401))
                .andExpect(jsonPath("$.error").value(
                        StringContains.containsString("mistake password")
                ));
    }
}
