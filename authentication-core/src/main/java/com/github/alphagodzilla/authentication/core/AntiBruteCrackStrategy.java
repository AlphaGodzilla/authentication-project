package com.github.alphagodzilla.authentication.core;

import java.time.Duration;

/**
 * 防暴破策略
 * @author AlphaGodzilla
 * @date 2022/3/11 11:02
 */
public class AntiBruteCrackStrategy {
    /**
     * 最大暴破次数
     */
    private final int maxCrackTimes;
    /**
     * 禁用时长
     */
    private final Duration banDuration;

    public AntiBruteCrackStrategy(int maxCrackTimes, Duration banDuration) {
        if (maxCrackTimes < 0) {
            throw new IllegalArgumentException("maxCrackTimes is negative");
        }
        this.maxCrackTimes = maxCrackTimes;
        this.banDuration = banDuration;
    }

    public int getMaxCrackTimes() {
        return maxCrackTimes;
    }

    public Duration getBanDuration() {
        return banDuration;
    }
}
