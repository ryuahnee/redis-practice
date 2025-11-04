package com.example.redis.service;

import com.example.redis.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ProductService {

    // DB 대신 사용할 메모리 저장소 (실습용)
    private final Map<Long, Product> database = new HashMap<>();

    // 생성자에서 초기 데이터 추가
    ProductService(){
        database.put(1L, new Product(1L, "맥북 프로", 2500000, "Apple 노트북"));
        database.put(2L, new Product(2L, "아이폰 15", 1200000, "Apple 스마트폰"));
        database.put(3L, new Product(3L, "에어팟 프로", 350000, "무선 이어폰"));
    }

    /**
     * 상품 조회 - 캐싱 적용
     * 첫 조회: DB에서 가져와서 Redis에 저장
     * 이후 조회: Redis에서 바로 반환 (DB 접근 X)
     */
    @Cacheable(value="products", key = "#id")
    public Product getProduct(Long id) {
        log.info("DB에서 상품 조회: id={}", id);
        try {
            Thread.sleep(1000); // 1초 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return database.get(id);
    }

    /**
     * 상품 수정 - 캐시 갱신
     * DB 업데이트하고 Redis 캐시도 업데이트
     */
    @CachePut(value="products", key = "#id")
    public Product updateProduct(Long id, Product product){
        log.info("DB에서 상품 수정: id={}", id);
        product.setId(id);
        database.put(id,product);
        return product;
    }

    /**
     * 상품 삭제 - 캐시 제거
     * DB에서 삭제하고 Redis 캐시도 삭제
     */
    @CacheEvict(value="products", key = "#id")
    public void deleteProduct(Long id){
        log.info("DB에서 상품 삭제: id={}", id);
        database.remove(id);
    }
    /**
     * 전체 캐시 삭제
     */
    @CacheEvict(value = "products", allEntries = true)
    public void clearCache() {
        log.info("전체 캐시 삭제");
    }
}
