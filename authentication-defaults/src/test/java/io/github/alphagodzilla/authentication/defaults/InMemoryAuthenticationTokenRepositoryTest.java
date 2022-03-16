package io.github.alphagodzilla.authentication.defaults;

import io.github.alphagodzilla.authentication.core.model.TokenPersistent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryAuthenticationTokenRepositoryTest {
    InMemoryAuthenticationTokenRepository repository;
    TokenPersistent tokenPersistent;
    String key = "defaultKey";

    @BeforeEach
    public void setUp() throws Exception {
        repository = new InMemoryAuthenticationTokenRepository();
        tokenPersistent = new TokenPersistent();
        tokenPersistent.setSecret(UUID.randomUUID().toString());
        tokenPersistent.setExpireAt(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20));
    }

    @Test
    public void putNullToken() {
        assertThrows(NullPointerException.class, () -> repository.putToken(key, null));
    }

    @Test
    public void putToken() {
        repository.putToken(key, tokenPersistent);
        assertEquals(tokenPersistent, repository.getToken(key));
    }

    @Test
    public void getToken() {
        repository.putToken(key, tokenPersistent);
        assertEquals(tokenPersistent, repository.getToken(key));
    }

    @Test
    public void deleteToken() {
        repository.putToken(key, tokenPersistent);
        repository.deleteToken(key);
        assertNull(repository.getToken(key));
    }
}
