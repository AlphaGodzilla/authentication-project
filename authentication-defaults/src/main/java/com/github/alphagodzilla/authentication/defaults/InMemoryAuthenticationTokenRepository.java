package com.github.alphagodzilla.authentication.defaults;

import com.github.alphagodzilla.authentication.core.model.TokenPersistent;
import com.github.alphagodzilla.authentication.core.repository.AuthenticationTokenRepository;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author AlphaGodzilla
 * @date 2022/3/14 15:41
 */
public class InMemoryAuthenticationTokenRepository implements AuthenticationTokenRepository {
    private final Map<String, TokenPersistent> inMemoryCache = new ConcurrentHashMap<>();

    @Override
    public boolean putToken(Serializable key, TokenPersistent tokenPersistent) {
        inMemoryCache.put(key.toString(), tokenPersistent);
        return true;
    }

    @Override
    public TokenPersistent getToken(Serializable key) {
        return inMemoryCache.get(key.toString());
    }

    @Override
    public boolean deleteToken(Serializable key) {
        return inMemoryCache.remove(key) != null;
    }
}
