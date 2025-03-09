package com.mozzie.domain.strategy.service;

import com.mozzie.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @author : mozzie
 * @description : 策略奖品接口
 * @date : 2025/2/26 9:14
 */
public interface IRaffleAward {

    /**
     * 根据策略ID查询抽奖奖品列表配置
     *
     * @param strategyId 策略ID
     * @return 奖品列表
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);

}