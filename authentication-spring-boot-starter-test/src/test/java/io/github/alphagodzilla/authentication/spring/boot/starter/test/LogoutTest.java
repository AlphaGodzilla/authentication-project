package io.github.alphagodzilla.authentication.spring.boot.starter.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.alphagodzilla.authentication.core.model.TokenResult;
import org.assertj.core.matcher.AssertionMatcher;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author AlphaGodzilla
 * @date 2022/3/16 16:18
 */
@SpringBootTest(classes = AuthenticationExampleApp.class)
@AutoConfigureMockMvc
public class LogoutTest {
    @Resource
    private MockMvc mockMvc;

    TokenResult tokenResult;

    @BeforeEach
    public void prepareToken() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/api/public/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("username=user1&password=user1");
        String contentAsString = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        tokenResult = new ObjectMapper().readValue(contentAsString, TokenResult.class);
    }

    @Test
    public void revertToken() throws Exception {
        mockMvc.perform(get("/api/logout").header("Authentication", tokenResult.getToken()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/who-am-i")).andExpect(status().is(401));
    }
}
