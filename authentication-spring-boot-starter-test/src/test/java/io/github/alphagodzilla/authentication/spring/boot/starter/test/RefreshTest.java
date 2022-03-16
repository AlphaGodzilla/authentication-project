package io.github.alphagodzilla.authentication.spring.boot.starter.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.alphagodzilla.authentication.core.model.TokenResult;
import org.hamcrest.core.IsNot;
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
 * @date 2022/3/16 16:12
 */
@SpringBootTest(classes = AuthenticationExampleApp.class)
@AutoConfigureMockMvc
public class RefreshTest {
    @Resource
    private MockMvc mockMvc;

    @Test
    public void refreshToken() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/api/public/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("username=user1&password=user1");
        String contentAsString = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        TokenResult grant = new ObjectMapper().readValue(contentAsString, TokenResult.class);

        mockMvc.perform(get("/api/refresh").header("Authentication", grant.getToken()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.attributes.uid").value("user1"))
                .andExpect(jsonPath("$.token").value(IsNot.not(grant.getToken())));
    }
}
