package com.github.alphagodzilla.authentication.core;

import com.github.alphagodzilla.authentication.core.service.AuthenticationAntiBruteCrackService;
import com.github.alphagodzilla.authentication.core.service.AuthenticationTokenService;

/**
 * @author AlphaGodzilla
 * @date 2022/3/11 13:43
 */
public class AuthenticationManagerBuilder {
    private AuthenticationAntiBruteCrackService antiBruteCrackService;
    private AuthenticationTokenService tokenService;
    private PasswordEncoder passwordEncoder;

    public static AuthenticationManagerBuilder builder() {
        return new AuthenticationManagerBuilder();
    }

    public AuthenticationManagerBuilder antiBruteCrackService(AuthenticationAntiBruteCrackService antiBruteCrackService) {
        this.antiBruteCrackService = antiBruteCrackService;
        return this;
    }

    public AuthenticationManagerBuilder tokenService(AuthenticationTokenService tokenService) {
        this.tokenService = tokenService;
        return this;
    }

    public AuthenticationManagerBuilder passwordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        return this;
    }

    public AuthenticationManager build() {
        return new AuthenticationManager(antiBruteCrackService, tokenService, passwordEncoder);
    }
}
