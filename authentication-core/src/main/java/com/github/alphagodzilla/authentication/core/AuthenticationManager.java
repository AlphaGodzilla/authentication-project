package com.github.alphagodzilla.authentication.core;

import com.github.alphagodzilla.authentication.core.exception.BannedException;
import com.github.alphagodzilla.authentication.core.exception.IllegalTokenException;
import com.github.alphagodzilla.authentication.core.exception.MistakePasswordException;
import com.github.alphagodzilla.authentication.core.exception.UnknownFormatTokenException;
import com.github.alphagodzilla.authentication.core.model.Token;
import com.github.alphagodzilla.authentication.core.model.TokenResult;
import com.github.alphagodzilla.authentication.core.model.UserInformation;
import com.github.alphagodzilla.authentication.core.service.AuthenticationAntiBruteCrackService;
import com.github.alphagodzilla.authentication.core.service.AuthenticationTokenService;
import com.github.alphagodzilla.authentication.core.service.AuthenticationUserService;

import java.io.Serializable;
import java.time.Duration;

/**
 * @author AlphaGodzilla
 * @date 2022/3/9 17:47
 */
public class AuthenticationManager {
    private final AuthenticationAntiBruteCrackService antiBruteCrackService;
    private final AuthenticationTokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationManager(AuthenticationAntiBruteCrackService antiBruteCrackService,
                                 AuthenticationTokenService tokenService,
                                 PasswordEncoder passwordEncoder) {
        this.antiBruteCrackService = antiBruteCrackService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 颁发认证
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    public TokenResult grant(String username, Duration retention, String password, AuthenticationUserService authenticationUserService) {
        UserInformation userInformation = authenticationUserService.getUserInformation(username);
        Serializable uid = userInformation.getUid();
        antiBruteCrackCheck(uid, () -> {
            String encodedPassword = userInformation.getEncodedPassword();
            boolean matches = passwordEncoder.matches(password, encodedPassword);
            if (!matches) {
                throw new MistakePasswordException();
            }
        });
        return tokenService.grantToken(uid, retention, userInformation.getAttributes(), userInformation.getExtraAttributes());
    }

    /**
     * 检查Token是否为合约的Token
     * @param token 授权凭证
     */
    public void match(String token) {
        if (!tokenService.match(token)) {
            throw new IllegalTokenException();
        }
    }

    /**
     * 刷新凭证
     * @param token 当前颁发的token
     * @param retention 颁发新token的有效期
     * @return 新的token
     */
    public TokenResult refresh(String token, Duration retention) {
        String uid = parseUid(token);
        antiBruteCrackCheck(uid, () -> match(token));
        return tokenService.refreshToken(token, retention);
    }

    /**
     * 吊销认证
     * @return 是否成功
     */
    public boolean revokeByUid(String uid) {
        return tokenService.cleanTokenByUid(uid);
    }

    /**
     * 吊销认证
     * @return 是否成功
     */
    public boolean revoke(String token) {
        return tokenService.cleanTokenByToken(token);
    }

    /**
     * 解析token中的userId
     * @param token token
     * @return 用户ID
     */
    public String parseUid(String token) throws UnknownFormatTokenException {
        Token token1 = tokenService.parseToken(token);
        return token1.getUid();
    }

    /**
     * 检查的暴破禁用，执行过程报异常暴破记录会增加
     * @param uid 用户
     * @param runnable 执行过程
     */
    private void antiBruteCrackCheck(Serializable uid, Runnable runnable) {
        boolean banned = antiBruteCrackService.banned(uid);
        if (banned) {
            throw new BannedException();
        }
        try {
            runnable.run();
            antiBruteCrackService.reset(uid);
        }catch (Exception e) {
            antiBruteCrackService.markCrackTimes(uid, 1);
            throw e;
        }
    }
}
