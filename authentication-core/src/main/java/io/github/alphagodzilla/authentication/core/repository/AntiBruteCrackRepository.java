package io.github.alphagodzilla.authentication.core.repository;

import java.io.Serializable;
import java.time.Duration;

/**
 * 防爆破状态管理器
 * @author AlphaGodzilla
 * @date 2022/3/9 17:33
 */
public interface AntiBruteCrackRepository {
    /**
     * 增加爆破记录数
     * @param key 键名
     * @param times 需要增加次数
     * @param retention 缓存时长
     * @return 增加后最新的总次数
     */
    int plusAndGetCrackTimes(Serializable key, int times, Duration retention);

    /**
     * 从禁用名单中删除
     * @param key 键名
     * @return 是否删除成功
     */
    boolean deleteBan(Serializable key);

    /**
     * 是否在禁用中
     * @param key 键名
     * @param maxCrackTimes 最大可破解次数
     * @return 是否存在
     */
    boolean hasBan(Serializable key, int maxCrackTimes);
}
