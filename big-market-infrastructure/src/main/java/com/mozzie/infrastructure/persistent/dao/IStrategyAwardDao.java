package com.mozzie.infrastructure.persistent.dao;

import com.mozzie.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : mozzie
 * @description : 抽奖策略奖品明细配置 - 概率、规则 DAO
 * @date : 2025/2/21 14:43
 */
@Mapper
public interface IStrategyAwardDao {

    List<StrategyAward> queryStrategyAwardList();

    List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);

    String queryStrategyAwardRuleModels(StrategyAward strategyAward);

    void updateStrategyAwardStock(StrategyAward strategyAward);

    StrategyAward queryStrategyAward(StrategyAward strategyAwardReq);
}