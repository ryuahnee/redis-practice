package com.example.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * Redis에 객체를 저장하려면 직렬화(Serialization) 필요
 * 객체를 바이트 배열로 변환하여 저장
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    private Long id;
    private String name;
    private Integer price;
    private String description;
}