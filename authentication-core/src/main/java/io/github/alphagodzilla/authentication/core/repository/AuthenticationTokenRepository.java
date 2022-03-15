package io.github.alphagodzilla.authentication.core.repository;

import io.github.alphagodzilla.authentication.core.model.TokenPersistent;

import java.io.Serializable;

/**
 * token存储接口
 * @author AlphaGodzilla
 * @date 2022/3/9 17:34
 */
public interface AuthenticationTokenRepository {
    /**
     * 保存token
     * @param key 键名
     * @param tokenPersistent 数据体
     * @return 是否成功
     */
    boolean putToken(Serializable key, TokenPersistent tokenPersistent);

    /**
     * 获取token
     * @param key 键名
     * @return 数据体
     */
    TokenPersistent getToken(Serializable key);

    /**
     * 删除token
     * @param key 键名
     * @return 是否成功
     */
    boolean deleteToken(Serializable key);
}
