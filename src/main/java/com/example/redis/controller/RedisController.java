package com.example.redis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/redis")
@RestController
@RequiredArgsConstructor
public class RedisController {

    private final RedisTemplate<String, Object> redisTemplate;


    /*
        String: opsForValue() → 일반적인 Key-Value
        List: opsForList() → 리스트 자료구조
        Set: opsForSet() → 집합 자료구조
        Hash: opsForHash() → 해시 자료구조
        Sorted Set: opsForZSet() → 정렬된 집합
    */

    /**
     * Redis에 데이터 저장
     * POST /api/redis?key=name&value=홍길동
     *
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

}
