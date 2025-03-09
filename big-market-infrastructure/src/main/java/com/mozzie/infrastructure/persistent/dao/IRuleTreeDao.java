package com.mozzie.infrastructure.persistent.dao;

import com.mozzie.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : mozzie
 * @description : 规则树表DAO
 * @date : 2025/2/23 22:29
 */
@Mapper
public interface IRuleTreeDao {

    RuleTree queryRuleTreeByTreeId(String treeId);

}