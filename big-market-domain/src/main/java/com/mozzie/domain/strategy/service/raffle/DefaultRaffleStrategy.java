package com.mozzie.domain.strategy.service.raffle;

import com.mozzie.domain.strategy.model.entity.StrategyAwardEntity;
import com.mozzie.domain.strategy.model.valobj.RuleTreeVO;
import com.mozzie.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.mozzie.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.mozzie.domain.strategy.repository.IStrategyRepository;
import com.mozzie.domain.strategy.service.AbstractRaffleStrategy;
import com.mozzie.domain.strategy.service.IRaffleAward;
import com.mozzie.domain.strategy.service.IRaffleStock;
import com.mozzie.domain.strategy.service.armory.IStrategyDispatch;
import com.mozzie.domain.strategy.service.rule.chain.ILogicChain;
import com.mozzie.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.mozzie.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.mozzie.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : mozzie
 * @description : 默认的抽奖策略实现
 * @date : 2025/2/22 19:37
 */
@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleStock, IRaffleAward {

    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        super(repository, strategyDispatch, defaultChainFactory, defaultTreeFactory);
    }

    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);
        return logicChain.logic(userId, strategyId);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);
        if (null == strategyAwardRuleModelVO) {
            return DefaultTreeFactory.StrategyAwardVO.builder().awardId(awardId).build();
        }
        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if (null == ruleTreeVO) {
            throw new RuntimeException("存在抽奖策略配置的规则模型 Key，未在库表 rule_tree、rule_tree_node、rule_tree_line 配置对应的规则树信息 " + strategyAwardRuleModelVO.getRuleModels());
        }
        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return treeEngine.process(userId, strategyId, awardId);
    }


    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
        return repository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        repository.updateStrategyAwardStock(strategyId, awardId);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId) {
        return repository.queryStrategyAwardList(strategyId);
    }
}
