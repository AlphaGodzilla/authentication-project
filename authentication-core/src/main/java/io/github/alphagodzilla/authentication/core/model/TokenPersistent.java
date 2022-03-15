package io.github.alphagodzilla.authentication.core.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author T.J
 * @date 2021/10/14 16:05
 */
public class TokenPersistent implements Serializable {
    private static final long serialVersionUID = 4375927301465188699L;

    /**
     * 随机签名密钥
     */
    private String secret;
    /**
     * 过期时间戳
     */
    private Long expireAt;

    public TokenPersistent() {
    }

    public TokenPersistent(String secret, long expireAt) {
        this.secret = secret;
        this.expireAt = expireAt;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TokenPersistent)) {
            return false;
        }
        final TokenPersistent that = (TokenPersistent) o;
        return getSecret().equals(that.getSecret()) && getExpireAt().equals(that.getExpireAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSecret(), getExpireAt());
    }
}
