package com.example.redis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequestMapping("/api/redis")
@RestController
@RequiredArgsConstructor
@Slf4j
public class RedisController {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis에 데이터 저장
     * POST /api/redis?key=name&value=홍길동
     */
    @PostMapping
    public ResponseEntity<String> redisSave(@RequestParam String key, @RequestParam String value){
        redisTemplate.opsForValue().set(key,value);
        return ResponseEntity.ok("저장 완료: " + key + " = " + value);
    }

    /**
     * Redis에서 데이터 조회
     * GET /api/redis?key=name
     */
    @GetMapping
    public ResponseEntity<String> redisGet(@RequestParam String key){

        Object value = redisTemplate.opsForValue().get(key);

        String responseName =  value != null ? value.toString() : "데이터 없음";
        return ResponseEntity.ok("데이터 조회: " + responseName);
    }

    /**
     * Redis에서 데이터 삭제
     * DELETE /api/redis?key=name
     */
    @DeleteMapping
    public ResponseEntity<String> redisDelete(@RequestParam String key){

        Object value = redisTemplate.opsForValue().get(key);
        if(value == null || value.equals("")){
            return ResponseEntity.ok("요청주신 값이 존재하지 않습니다 : " + key);
        }

        redisTemplate.delete(key);
        return ResponseEntity.ok("삭제 완료 : "+ key);
    }

    /**
     * Redis에 데이터 저장 (만료시간 설정)
     * POST /api/redis/expire?key=session&value=user123&seconds=60
     */
    @PostMapping("/expire")
    public ResponseEntity<String> setValueWithExpire(@RequestParam String key,
                                                     @RequestParam String value,
                                                     @RequestParam Long seconds) {

        redisTemplate.opsForValue().set(key,value, Duration.ofSeconds(seconds));
        log.info("key={},value={},seconds={}",key,value,seconds);
        return ResponseEntity.ok("저장 완료: " + key + " = " + value + " (저장만료시간: " + seconds + ")");
    }


    /**
     * Redis 키의 남은 만료시간 조회
     * GET /api/redis/ttl?key=session
     */
    @GetMapping("/ttl")
    public ResponseEntity<String> getValueWithExpire(@RequestParam String key){
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        String response = "";
        if (ttl == -2) {
            response = "키가 존재하지 않음";
        } else if (ttl == -1) {
            response = "만료시간이 설정되지 않음 (영구 보관)";
        }
        return ResponseEntity.ok(key + " : " + ((ttl > 0) ? ttl : response));
    }
}
