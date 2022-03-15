package io.github.alphagodzilla.authentication.core.service;

import java.io.Serializable;

/**
 * 反暴力破解服务
 * @author AlphaGodzilla
 * @date 2022/3/9 17:34
 */
public interface AuthenticationAntiBruteCrackService {
    /**
     * 标记破解次数，如果累计的破解次数大于了最大破解次数，则会调用ban()方法禁用该用户
     * @param uid 用户ID
     * @param times 增加的破解次数
     * @return 是否标记成功
     */
    boolean markCrackTimes(Serializable uid, int times);

    /**
     * 重置防爆破器状态
     * @param uid 用户ID
     * @return 是否成功
     */
    boolean reset(Serializable uid);

    /**
     * 检查用户是否被禁用
     * @param uid 用户ID
     * @return 是否被禁用，如果是true表示用户被禁用，如果是false表示用户未被禁用
     */
    boolean banned(Serializable uid);
}
