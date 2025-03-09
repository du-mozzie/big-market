package com.mozzie.domain.strategy.service.rule.tree;

import com.mozzie.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @author : mozzie
 * @description :
 * @date : 2025/2/23 16:03
 */
public interface ILogicTreeNode {

    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue);

}