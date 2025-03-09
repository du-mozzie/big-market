package com.mozzie.infrastructure.persistent.dao;

import com.mozzie.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : mozzie
 * @description : 策略规则 DAO
 * @date : 2025/2/21 14:45
 */
@Mapper
public interface IStrategyRuleDao {

    List<StrategyRule> queryStrategyRuleList();

    StrategyRule queryStrategyRule(StrategyRule strategyRuleReq);

    String queryStrategyRuleValue(StrategyRule strategyRule);
}
