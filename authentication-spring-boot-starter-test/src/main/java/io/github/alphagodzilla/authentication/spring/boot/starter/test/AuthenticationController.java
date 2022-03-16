package io.github.alphagodzilla.authentication.spring.boot.starter.test;

import io.github.alphagodzilla.authentication.core.AuthenticationManager;
import io.github.alphagodzilla.authentication.core.model.TokenResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author AlphaGodzilla
 * @date 2022/3/16 11:20
 */
@RestController
@RequestMapping("/api")
public class AuthenticationController {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private DefaultUserInfoService defaultUserInfoService;

    @PostMapping("/public/login")
    public TokenResult login(@RequestParam String username, @RequestParam String password) {
        return authenticationManager.grant(username, password, Duration.ofDays(1), defaultUserInfoService);
    }

    @GetMapping("/who-am-i")
    public String whoAmI(@RequestHeader("Authentication") String token) {
        return authenticationManager.parseUid(token);
    }

    @GetMapping("/refresh")
    public TokenResult refresh(@RequestHeader("Authentication") String token) {
        return authenticationManager.refresh(token, Duration.ofDays(1));
    }

    @GetMapping("/logout")
    public boolean logout(@RequestHeader("Authentication") String token) {
        authenticationManager.revoke(token);
        return true;
    }
}
