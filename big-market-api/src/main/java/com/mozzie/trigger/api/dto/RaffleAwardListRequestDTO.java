package com.mozzie.trigger.api.dto;

import lombok.Data;

/**
 * @author : mozzie
 * @description : 抽奖奖品列表，请求对象
 * @date : 2025/2/26 9:10
 */
@Data
public class RaffleAwardListRequestDTO {

    // 抽奖策略ID
    private Long strategyId;

}