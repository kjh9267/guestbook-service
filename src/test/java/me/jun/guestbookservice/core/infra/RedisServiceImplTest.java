package me.jun.guestbookservice.core.infra;

import me.jun.guestbookservice.core.application.RedisService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.RedisServer;

import static me.jun.guestbookservice.support.RedisFixture.REDIS_PORT;

@ActiveProfiles("test")
@SpringBootTest(properties = "spring.cloud.config.enabled=false")
class RedisServiceImplTest {

    @Autowired
    private RedisService redisServiceImpl;

    private RedisServer redisServer;

    @BeforeEach
    void setUp() {
        redisServer = new RedisServer(REDIS_PORT);
        redisServer.start();
    }

    @AfterEach
    void tearDown() {
        redisServer.stop();
    }

    @Test
    void deletePostList() {
        redisServiceImpl.deletePostList();
    }
}