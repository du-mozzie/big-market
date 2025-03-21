package com.mozzie.domain.strategy.service.rule.chain;

import com.mozzie.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * @author : mozzie
 * @description : 抽奖策略规则责任链接口
 * @date : 2025/2/23 13:44
 */
public interface ILogicChain extends ILogicChainArmory{

    /**
     * 责任链接口
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);
    
}
