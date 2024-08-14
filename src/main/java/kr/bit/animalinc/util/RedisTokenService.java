package kr.bit.animalinc.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeAccessToken(String token, Long userNum, Duration duration) {
        redisTemplate.opsForValue().set("accessToken: " + userNum, token, duration);
    }

    public void addToBlacklist(String token) {
        redisTemplate.opsForValue().set("blacklist: " + token, "true", 30, TimeUnit.MINUTES);
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey("blacklist: " + token);
    }

    public void deleteAccessToken(Long userNum) {
        redisTemplate.delete("accessToken: " + userNum);
    }

    public String getAccessToken(Long userNum) {
        return redisTemplate.opsForValue().get("accessToken: " + userNum);
    }
}
