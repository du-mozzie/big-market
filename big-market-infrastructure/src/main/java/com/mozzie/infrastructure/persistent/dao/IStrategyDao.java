package com.mozzie.infrastructure.persistent.dao;

import com.mozzie.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : mozzie
 * @description : 抽奖策略 DAO
 * @date : 2025/2/21 14:43
 */
@Mapper
public interface IStrategyDao {

    List<Strategy> queryStrategyList();

    Strategy queryStrategyByStrategyId(Long strategyId);
    
}