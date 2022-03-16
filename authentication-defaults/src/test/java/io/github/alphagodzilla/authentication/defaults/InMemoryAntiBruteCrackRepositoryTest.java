package io.github.alphagodzilla.authentication.defaults;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class InMemoryAntiBruteCrackRepositoryTest {
    String key = "key";
    InMemoryAntiBruteCrackRepository antiBruteCrackRepository;

    @BeforeEach
    public void setUp() throws Exception {
        antiBruteCrackRepository = new InMemoryAntiBruteCrackRepository();
    }

    @Test
    public void plusAndGetCrackTimes() {
        Duration duration = Duration.ofDays(1);
        int firstCount = antiBruteCrackRepository.plusAndGetCrackTimes(key, 1, duration);
        Assertions.assertSame(1, firstCount);
        int secondCount = antiBruteCrackRepository.plusAndGetCrackTimes(key, 1, duration);
        Assertions.assertSame(2, secondCount);
        int fourCount = antiBruteCrackRepository.plusAndGetCrackTimes(key, 2, duration);
        Assertions.assertSame(4, fourCount);
    }

    @Test
    public void deleteBan() {
        boolean successDeleteBan = antiBruteCrackRepository.deleteBan(key);
        Assertions.assertFalse(successDeleteBan, "success delete empty ban");
        antiBruteCrackRepository.plusAndGetCrackTimes(key, 1, Duration.ofDays(1));
        boolean successDeleteBan2 = antiBruteCrackRepository.deleteBan(key);
        Assertions.assertTrue(successDeleteBan2, "success delete exist ban");
    }

    @Test
    public void hasBan() throws InterruptedException {
        boolean hasBan = antiBruteCrackRepository.hasBan(key, 1);
        Assertions.assertFalse(hasBan, "test first call hasBan expect false");
        antiBruteCrackRepository.plusAndGetCrackTimes(key,1, Duration.ofMillis(100));
        boolean hasBan2 = antiBruteCrackRepository.hasBan(key, 1);
        Assertions.assertTrue(hasBan2, "test second call hasBan expect true");
        // 强制暂停线程200毫秒
        Thread.sleep(200);
        boolean hasBan3 = antiBruteCrackRepository.hasBan(key, 1);
        Assertions.assertFalse(hasBan3, "test expire ban expect false");
    }
}
