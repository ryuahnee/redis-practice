package com.example.redis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/redis/hash")
@RequiredArgsConstructor
@Slf4j
public class RedisHashController {

    private final RedisTemplate<String,Object> template;

    /**
     * Hash에 필드 추가/수정
     * POST /api/redis/hash?key=user:1001&field=name&value=홍길동
     */
    @PostMapping
    public ResponseEntity<String> saveField(@RequestParam String key, @RequestParam String field , @RequestParam String value){
        template.opsForHash().put(key,field,value);
        return ResponseEntity.ok("저장 완료: " + key + " [" + field + " = " + value + "]");
    }
    /**
     * Hash의 특정 필드 조회
     * GET /api/redis/hash/field?key=user:1001&field=name
     */
    @GetMapping("/field")
    public ResponseEntity<String> getField(@RequestParam String key, @RequestParam String field){
        Object value = template.opsForHash().get(key, field);
        if(value == null || value.equals("")){
            return ResponseEntity.ok("요청주신 [ "+key+" ] 값이 존재 하지 않습니다");
        }
        return ResponseEntity.ok(value.toString());
    }

    /**
     * Hash의 모든 필드 조회
     * GET /api/redis/hash?key=user:1001
     */
    @GetMapping
    public ResponseEntity<Map<Object, Object>> getAllField(@RequestParam String key){

        List<Object> values = template.opsForHash().values(key);
        Map<Object, Object> entries = template.opsForHash().entries(key);


        log.info("values의 건수={}",values.size());
        log.info("values={}",values);
        log.info("entries={}",entries);
        return ResponseEntity.ok(entries);
    }

    /**
     * Hash의 특정 필드 삭제
     * DELETE /api/redis/hash/field?key=user:1001&field=age
     */
    @DeleteMapping("/field")
    public ResponseEntity<String> deleteField(@RequestParam String key, @RequestParam String field){

        Long delete = template.opsForHash().delete(key, field);

        log.info("삭제 건수={}",delete);
        return ResponseEntity.ok("삭제 완료: " + key + " [" + field + "]");
    }
    /**
     * Hash 전체 삭제
     * DELETE /api/redis/hash?key=user:1001
     */
    @DeleteMapping
    public ResponseEntity<String> deleteAllField(@RequestParam String key){
        Boolean delete = template.delete(key);

        log.info("삭제 여부={}",delete);
        return ResponseEntity.ok("삭제 완료: " + key );
    }

}
