package me.jun.guestbookservice.core.infra;

import lombok.RequiredArgsConstructor;
import me.jun.guestbookservice.core.application.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Value("${post-page}")
    private int postPage;

    @Value("${post-size}")
    private int postSize;

    @Override
    public void deletePostList() {
        ReactiveValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.delete(String.format("posts:%d:%d", postPage, postSize)).block();
    }
}
