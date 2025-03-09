package com.mozzie.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : mozzie
 * @description : 策略结果实体
 * @date : 2025/2/21 16:03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardEntity {

    /** 用户ID */
    private String userId;
    /** 奖品ID */
    private Integer awardId;
    
}
