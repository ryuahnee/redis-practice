package com.example.redis.controller;

import com.example.redis.domain.Product;
import com.example.redis.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 조회 (캐싱 적용)
     * GET /api/products/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id){
        log.info("상품 조회 시작: id={}", id);
        long startTime = System.currentTimeMillis();

        Product product = productService.getProduct(id);

        long endTime = System.currentTimeMillis();
        log.info("조회 완료: {}ms", endTime - startTime);

        return ResponseEntity.ok(product);
    }

    /**
     * 상품 수정 (캐시 갱신)
     * PUT /api/products/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    }

    /**
     * 상품 삭제 (캐시 제거)
     * DELETE /api/products/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("삭제 완료: " + id);
    }

    /**
     * 전체 캐시 삭제
     * DELETE /api/products/cache
     */
    @DeleteMapping("/cache")
    public ResponseEntity<String> clearCache() {
        productService.clearCache();
        return ResponseEntity.ok("전체 캐시 삭제 완료");
    }

}
