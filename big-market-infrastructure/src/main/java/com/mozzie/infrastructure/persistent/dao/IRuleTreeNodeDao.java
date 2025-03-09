package com.mozzie.infrastructure.persistent.dao;

import com.mozzie.infrastructure.persistent.po.RuleTreeNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : mozzie
 * @description : 规则树节点表DAO
 * @date : 2025/2/23 22:29
 */
@Mapper
public interface IRuleTreeNodeDao {

    List<RuleTreeNode> queryRuleTreeNodeListByTreeId(String treeId);

}