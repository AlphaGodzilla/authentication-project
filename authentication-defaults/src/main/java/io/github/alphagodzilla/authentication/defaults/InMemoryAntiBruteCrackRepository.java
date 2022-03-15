package io.github.alphagodzilla.authentication.defaults;

import io.github.alphagodzilla.authentication.core.repository.AntiBruteCrackRepository;

import java.io.Serializable;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author AlphaGodzilla
 * @date 2022/3/14 15:32
 */
public class InMemoryAntiBruteCrackRepository implements AntiBruteCrackRepository {
    private final Map<String, ExpirationCounter> memoryCache = new ConcurrentHashMap<>();

    @Override
    public int plusAndGetCrackTimes(Serializable key, int times, Duration retention) {
        String objKey = key.toString();
        ExpirationCounter cacheCounter = memoryCache.get(objKey);
        if (cacheCounter == null) {
            cacheCounter = new ExpirationCounter(retention, 1);
            memoryCache.putIfAbsent(objKey, cacheCounter);
            return cacheCounter.getCount();
        }
        cacheCounter.refreshDeadline(retention);
        return cacheCounter.addAndGetCount(1);
    }

    @Override
    public boolean deleteBan(Serializable key) {
        return memoryCache.remove(key.toString()) != null;
    }

    @Override
    public  boolean hasBan(Serializable key, int maxCrackTimes) {
        ExpirationCounter counter = memoryCache.get(key.toString());
        if (counter == null) {
            return false;
        }
        if (counter.expired()) {
            memoryCache.remove(key.toString());
            return false;
        }
        return counter.getCount() >= maxCrackTimes;
    }

    public static class ExpirationCounter {
        private long deadlineTimestamp;
        private final AtomicInteger counter;

        public ExpirationCounter(Duration duration, int initCountValue) {
            this.counter = new AtomicInteger(initCountValue);
            refreshDeadline(duration);
        }

        public int getCount() {
            return counter.get();
        }

        public int addAndGetCount(int delta) {
            return counter.addAndGet(delta);
        }

        public void refreshDeadline(Duration duration) {
            this.deadlineTimestamp = System.currentTimeMillis() + duration.toMillis();
        }

        public boolean expired() {
            return System.currentTimeMillis() >= deadlineTimestamp;
        }
    }
}
