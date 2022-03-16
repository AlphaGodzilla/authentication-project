package io.github.alphagodzilla.authentication.core.model;

import java.time.Duration;
import java.util.Map;

/**
 * @author T.J
 * @date 2021/10/14 11:19
 */
public class TokenResult {
    private String token;
    private Long expiresSeconds;
    private Long expireAt;
    private Map<String, String> attributes;
    private Map<String, String> extraAttributes;

    public TokenResult() {
    }

    public TokenResult(String token, Duration expire) {
        this(token, expire, null, null);
    }

    public TokenResult(String token, Duration expire, Map<String, String> attributes, Map<String, String> extraAttributes) {
        this.attributes = attributes;
        this.extraAttributes = extraAttributes;
        this.token = token;
        this.expiresSeconds = expire.toSeconds();
        this.expireAt = System.currentTimeMillis() + expire.toMillis();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiresSeconds() {
        return expiresSeconds;
    }

    public void setExpiresSeconds(Long expiresSeconds) {
        this.expiresSeconds = expiresSeconds;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getExtraAttributes() {
        return extraAttributes;
    }

    public void setExtraAttributes(Map<String, String> extraAttributes) {
        this.extraAttributes = extraAttributes;
    }
}
