package com.github.alphagodzilla.authentication.defaults;

import com.github.alphagodzilla.authentication.core.AntiBruteCrackStrategy;
import com.github.alphagodzilla.authentication.core.repository.AntiBruteCrackRepository;
import com.github.alphagodzilla.authentication.core.service.AuthenticationAntiBruteCrackService;

import java.io.Serializable;

/**
 * 默认防暴破服务
 * @author AlphaGodzilla
 * @date 2022/3/11 11:00
 */
public class DefaultAuthenticationAntiBruteCrackService implements AuthenticationAntiBruteCrackService {
    private final AntiBruteCrackRepository repository;
    private final AntiBruteCrackStrategy strategy;

    public DefaultAuthenticationAntiBruteCrackService(AntiBruteCrackRepository repository, AntiBruteCrackStrategy strategy) {
        this.repository = repository;
        this.strategy = strategy;
    }

    @Override
    public synchronized boolean markCrackTimes(Serializable uid, int times) {
        repository.plusAndGetCrackTimes(uid, times, strategy.getBanDuration());
        return true;
    }

    @Override
    public boolean reset(Serializable uid) {
        return repository.deleteBan(uid);
    }

    @Override
    public boolean banned(Serializable uid) {
        return repository.hasBan(uid, strategy.getMaxCrackTimes());
    }
}
