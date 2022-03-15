package io.github.alphagodzilla.authentication.core.service;

import io.github.alphagodzilla.authentication.core.exception.UnknownFormatTokenException;
import io.github.alphagodzilla.authentication.core.model.Token;
import io.github.alphagodzilla.authentication.core.model.TokenResult;

import java.io.Serializable;
import java.time.Duration;
import java.util.Map;

/**
 * @author AlphaGodzilla
 * @date 2021/10/14 11:31
 */
public interface AuthenticationTokenService {
    /**
     * 解析token为对象
     * @param token token
     * @return Token对象
     */
    Token parseToken(String token) throws UnknownFormatTokenException;
    /**
     * 校验token是否有效
     * @param token 待检查的token
     * @return true：合法token；false：非法token
     */
    boolean match(String token) throws UnknownFormatTokenException;

    /**
     * 颁发新token
     * @param uid 用户ID
     * @param retention 授权时长
     * @param attributes
     * @param extraAttributes
     * @return token结果
     */
    TokenResult grantToken(Serializable uid, Duration retention, Map<String, String> attributes, Map<String, String> extraAttributes);

    /**
     * 刷新token
     * @param token 待刷新的token
     * @return token结果
     */
    TokenResult refreshToken(String token, Duration retention) throws UnknownFormatTokenException;

    /**
     * 清除指定token
     * @param token 待清除token
     * @return 是否清除成功
     */
    boolean cleanTokenByToken(String token) throws UnknownFormatTokenException;

    /**
     * 清除指定用户的token
     * @param uid 用户ID
     * @return 是否清除成功
     */
    boolean cleanTokenByUid(Serializable uid);
}
