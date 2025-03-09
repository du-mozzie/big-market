package com.mozzie.domain.strategy.service.rule.tree.factory.engine;

import com.mozzie.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @author : mozzie
 * @description : 规则树组合接口
 * @date : 2025/2/23 16:57
 */
public interface IDecisionTreeEngine {

    DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId);

}