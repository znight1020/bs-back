package com.bob.support.redis;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@TestConfiguration
public class RedisContainerConfig {

  static {
    RedisContainerProvider.REDIS_CONTAINER.start();
  }

  @DynamicPropertySource
  static void redisProps(DynamicPropertyRegistry registry) {
    registry.add("spring.data.redis.host", RedisContainerProvider.REDIS_CONTAINER::getHost);
    registry.add("spring.data.redis.port", () -> RedisContainerProvider.REDIS_CONTAINER.getMappedPort(6379));
  }

  @Bean
  @Primary
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(
        RedisContainerProvider.REDIS_CONTAINER.getHost(),
        RedisContainerProvider.REDIS_CONTAINER.getMappedPort(6379)
    );
  }

  @Bean
  @Primary
  public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
    return new StringRedisTemplate(redisConnectionFactory);
  }
}
