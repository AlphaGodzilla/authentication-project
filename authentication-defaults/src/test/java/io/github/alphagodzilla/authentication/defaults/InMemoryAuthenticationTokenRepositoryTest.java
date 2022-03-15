package io.github.alphagodzilla.authentication.defaults;

import io.github.alphagodzilla.authentication.core.model.TokenPersistent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InMemoryAuthenticationTokenRepositoryTest {
    InMemoryAuthenticationTokenRepository repository;
    TokenPersistent tokenPersistent;
    String key = "defaultKey";

    @Before
    public void setUp() throws Exception {
        repository = new InMemoryAuthenticationTokenRepository();
        tokenPersistent = new TokenPersistent();
        tokenPersistent.setSecret(UUID.randomUUID().toString());
        tokenPersistent.setExpireAt(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20));
    }

    @Test(expected = NullPointerException.class)
    public void putNullToken() {
        repository.putToken(key, null);
    }

    @Test
    public void putToken() {
        repository.putToken(key, tokenPersistent);
        Assert.assertEquals(tokenPersistent, repository.getToken(key));
    }

    @Test
    public void getToken() {
        repository.putToken(key, tokenPersistent);
        Assert.assertEquals(tokenPersistent, repository.getToken(key));
    }

    @Test
    public void deleteToken() {
        repository.putToken(key, tokenPersistent);
        repository.deleteToken(key);
        Assert.assertNull(repository.getToken(key));
    }
}
