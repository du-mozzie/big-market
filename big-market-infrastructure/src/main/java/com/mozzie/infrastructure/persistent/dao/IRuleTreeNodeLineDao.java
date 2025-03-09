package com.mozzie.infrastructure.persistent.dao;

import com.mozzie.infrastructure.persistent.po.RuleTreeNodeLine;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : mozzie
 * @description : 规则树节点连线表DAO
 * @date : 2025/2/23 22:29
 */
@Mapper
public interface IRuleTreeNodeLineDao {

    List<RuleTreeNodeLine> queryRuleTreeNodeLineListByTreeId(String treeId);
}
