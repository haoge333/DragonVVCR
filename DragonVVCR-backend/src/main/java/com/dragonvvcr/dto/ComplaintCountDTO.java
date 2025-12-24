package com.dragonvvcr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于统计被吐槽最多的玩家和副本的DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintCountDTO {
    private String name;
    private Long count;
}
