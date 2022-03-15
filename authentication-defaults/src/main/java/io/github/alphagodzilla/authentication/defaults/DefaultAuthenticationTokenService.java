package io.github.alphagodzilla.authentication.defaults;

import io.github.alphagodzilla.authentication.core.HmacAlgorithm;
import io.github.alphagodzilla.authentication.core.JsonParser;
import io.github.alphagodzilla.authentication.core.exception.UnknownFormatTokenException;
import io.github.alphagodzilla.authentication.core.model.Token;
import io.github.alphagodzilla.authentication.core.model.TokenPersistent;
import io.github.alphagodzilla.authentication.core.model.TokenResult;
import io.github.alphagodzilla.authentication.core.repository.AuthenticationTokenRepository;
import io.github.alphagodzilla.authentication.core.service.AuthenticationTokenService;
import io.github.alphagodzilla.authentication.defaults.util.Base64Util;
import io.github.alphagodzilla.authentication.defaults.util.HexEncoder;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

/**
 * 默认Token服务
 * @author AlphaGodzilla
 * @date 2022/3/11 11:06
 */
public class DefaultAuthenticationTokenService implements AuthenticationTokenService {
    private final HmacAlgorithm tokenHmacAlgorithm;
    private final AuthenticationTokenRepository authenticationTokenRepository;
    private final JsonParser jsonParser;

    public DefaultAuthenticationTokenService(HmacAlgorithm tokenHmacAlgorithm,
                                             AuthenticationTokenRepository authenticationTokenRepository,
                                             JsonParser jsonParser
    ) {
        this.tokenHmacAlgorithm = tokenHmacAlgorithm;
        this.authenticationTokenRepository = authenticationTokenRepository;
        this.jsonParser = jsonParser;
    }

    @Override
    public Token parseToken(String token) throws UnknownFormatTokenException {
        boolean isBase64 = Base64Util.isBase64(token);
        if (!isBase64) {
            throw new UnknownFormatTokenException();
        }
        String decodedTokenStr = new String(Base64.getDecoder().decode(token.getBytes(StandardCharsets.UTF_8)));
        try {
            return jsonParser.toBean(decodedTokenStr, Token.class);
        }catch (JsonParser.JsonParseException exception) {
            throw new UnknownFormatTokenException(exception);
        }
    }

    @Override
    public boolean match(String token) throws UnknownFormatTokenException {
        Token decodedToken = parseToken(token);
        String uid = decodedToken.getUid();
        String storageSign = decodedToken.getSign();
        TokenPersistent tokenPersistent = authenticationTokenRepository.getToken(uid);
        if (tokenPersistent == null) {
            return false;
        }
        String secret = tokenPersistent.getSecret();
        byte[] digest = tokenHmacAlgorithm.digest(
                secret.getBytes(StandardCharsets.UTF_8),
                uid.getBytes(StandardCharsets.UTF_8)
        );
        String sign = HexEncoder.encodeHexStr(digest);
        return sign.equals(storageSign);
    }

    @Override
    public TokenResult grantToken(Serializable uid,
                                  Duration retention,
                                  Map<String, String> attributes,
                                  Map<String, String> extraAttributes) {
        String userId = String.valueOf(uid);
        String secret = UUID.randomUUID().toString();
        byte[] digest = tokenHmacAlgorithm.digest(secret.getBytes(StandardCharsets.UTF_8), userId.getBytes(StandardCharsets.UTF_8));
        String sign = HexEncoder.encodeHexStr(digest);
        Token token = new Token(userId, sign, attributes);
        String tokenJsonStr = jsonParser.toJsonString(token);
        String tokenEncoded = Base64.getEncoder().encodeToString(tokenJsonStr.getBytes(StandardCharsets.UTF_8));
        // 放入缓存
        long expireAt = System.currentTimeMillis() + retention.toMillis();
        TokenPersistent tokenPersistent = new TokenPersistent(secret, expireAt);
        authenticationTokenRepository.putToken(userId, tokenPersistent);
        return new TokenResult(tokenEncoded, retention, attributes, extraAttributes);
    }

    @Override
    public TokenResult refreshToken(String token, Duration retention) throws UnknownFormatTokenException {
        Token decodedToken = parseToken(token);
        String uid = decodedToken.getUid();
        cleanTokenByUid(uid);
        return grantToken(uid, retention, decodedToken.getAttributes(), null);
    }

    @Override
    public boolean cleanTokenByToken(String token) throws UnknownFormatTokenException {
        Token decodedToken = parseToken(token);
        return cleanTokenByUid(decodedToken.getUid());
    }

    @Override
    public boolean cleanTokenByUid(Serializable uid) {
        authenticationTokenRepository.deleteToken(String.valueOf(uid));
        return true;
    }
}
