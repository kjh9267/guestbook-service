package me.jun.guestbookservice.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract public class RedisFixture {

    public static final int REDIS_PORT = 6379;

    public static final int POST_SIZE = 10;
}
