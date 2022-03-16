package io.github.alphagodzilla.authentication.spring.boot.starter.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.alphagodzilla.authentication.core.model.TokenResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author AlphaGodzilla
 * @date 2022/3/16 15:16
 */
@Slf4j
@SpringBootTest(classes = AuthenticationExampleApp.class)
@AutoConfigureMockMvc
public class WhoAmiTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void normalRequestWhoAmI() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/api/public/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("username=user1&password=user1");
        String contentAsString = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        TokenResult grant = new ObjectMapper().readValue(contentAsString, TokenResult.class);
        log.info("{}", grant.getToken());
        MockHttpServletRequestBuilder request = get("/api/who-am-i")
                .header("Authentication", grant.getToken());
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("user1"))
        ;
    }

    @Test
    public void requestWhoAmIWithEmptyToken() throws Exception {
        MockHttpServletRequestBuilder request = get("/api/who-am-i");
        mockMvc.perform(request)
                .andExpect(status().is(401));
    }

    @Test
    public void requestWhoAmIWithIllegalToken() throws Exception {
        MockHttpServletRequestBuilder request = get("/api/who-am-i")
                .header("Authentication", "");
        mockMvc.perform(request)
                .andExpect(status().is(401));
    }
}
