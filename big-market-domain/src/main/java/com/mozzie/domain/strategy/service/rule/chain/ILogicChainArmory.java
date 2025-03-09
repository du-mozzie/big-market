package com.mozzie.domain.strategy.service.rule.chain;

/**
 * @author : mozzie
 * @description : 责任链装配
 * @date : 2025/2/23 13:43
 */
public interface ILogicChainArmory {

    ILogicChain next();

    ILogicChain appendNext(ILogicChain next);
}