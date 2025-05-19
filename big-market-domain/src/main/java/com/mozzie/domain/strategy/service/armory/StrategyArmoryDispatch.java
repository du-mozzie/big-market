package com.mozzie.domain.strategy.service.armory;

import com.mozzie.domain.strategy.model.entity.StrategyAwardEntity;
import com.mozzie.domain.strategy.model.entity.StrategyEntity;
import com.mozzie.domain.strategy.model.entity.StrategyRuleEntity;
import com.mozzie.domain.strategy.repository.IStrategyRepository;
import com.mozzie.types.common.Constants;
import com.mozzie.types.enums.ResponseCode;
import com.mozzie.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author : mozzie
 * @description : 策略装配库(兵工厂)，负责初始化策略计算
 * @date : 2025/2/21 15:10
 */
@Slf4j
@Service
public class StrategyArmoryDispatch implements IStrategyArmory, IStrategyDispatch {

    @Resource
    private IStrategyRepository repository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 1. 查询出对应策略的奖品进行装配
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);

        // 2 缓存奖品库存【用于decr扣减库存使用】
        for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
            Integer awardId = strategyAward.getAwardId();
            Integer awardCount = strategyAward.getAwardCount();
            cacheStrategyAwardCount(strategyId, awardId, awardCount);
        }

        // 3.1 默认装配配置【全量抽奖概率】
        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);

        // 3.2 权重策略配置 - 适用于 rule_weight 权重规则配置
        // 【4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109】
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        String ruleWeight = strategyEntity.getRuleWeight();
        if (ruleWeight == null) {
            return true;
        }

        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleWeight);
        if (null == strategyRuleEntity) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }
        // 拿到策略规则对应的奖品id 4000(积分)<101,102...奖品id>
        Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
        Set<String> keys = ruleWeightValueMap.keySet();
        for (String key : keys) {
            List<Integer> ruleWeightValues = ruleWeightValueMap.get(key);
            // 拿到当前策略的完整奖品备份
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            // 从所有奖品中，过滤出对应规则的奖品id
            strategyAwardEntitiesClone.removeIf(entity -> !ruleWeightValues.contains(entity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat("_").concat(key.split(":")[0]), strategyAwardEntitiesClone);
        }
        return true;
    }

    /**
     * 缓存奖品库存到Redis
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @param awardCount 奖品库存
     */
    private void cacheStrategyAwardCount(Long strategyId, Integer awardId, Integer awardCount) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        repository.cacheStrategyAwardCount(cacheKey, awardCount);
    }

    private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        int n = strategyAwardEntities.size();
        int[] awardIds = new int[n];
        BigDecimal[] probabilities = new BigDecimal[n];

        // 1. 提取奖项ID和概率，并计算总概率
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < n; i++) {
            StrategyAwardEntity entity = strategyAwardEntities.get(i);
            awardIds[i] = entity.getAwardId();
            probabilities[i] = entity.getAwardRate();
            total = total.add(entity.getAwardRate());
        }

        // 2. 标准化概率并放大N倍（使用高精度计算）
        BigDecimal[] scaled = new BigDecimal[n];
        for (int i = 0; i < n; i++) {
            scaled[i] = probabilities[i].divide(total, 10, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(n));
        }

        // 3. 初始化队列
        Queue<Integer> small = new LinkedList<>();
        Queue<Integer> large = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (scaled[i].compareTo(BigDecimal.ONE) < 0) {
                small.offer(i);
            } else {
                large.offer(i);
            }
        }

        // 4. 构建 Alias 表
        double[] prob = new double[n];
        int[] alias = new int[n];
        while (!small.isEmpty() && !large.isEmpty()) {
            int s = small.poll();
            int l = large.poll();

            // 记录概率和别名索引
            prob[s] = scaled[s].doubleValue();
            alias[s] = l; // 关键修复：存储索引而非奖项ID

            // 更新被补充的概率
            BigDecimal remaining = scaled[l].subtract(BigDecimal.ONE.subtract(scaled[s]));
            scaled[l] = remaining;
            if (remaining.compareTo(BigDecimal.ONE) < 0) {
                small.offer(l);
            } else {
                large.offer(l);
            }
        }

        // 5. 处理剩余元素（保持自环）
        while (!large.isEmpty()) {
            int l = large.poll();
            prob[l] = 1.0;
            alias[l] = l; // 自环
        }
        while (!small.isEmpty()) {
            int s = small.poll();
            prob[s] = 1.0;
            alias[s] = s; // 自环
        }

        // 6. 存储到Redis（需包含 awardIds 映射表）
        Map<String, Object> aliasTable = new HashMap<>();
        aliasTable.put("awardIds", awardIds);
        aliasTable.put("prob", prob);
        aliasTable.put("alias", alias);
        
        repository.storeStrategyAliasTable(key, aliasTable);
    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        return repository.getStrategyAwardByAlias(strategyId);
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(ruleWeightValue);
        return getRandomAwardId(key);
    }

    @Override
    public Integer getRandomAwardId(String key) {
        // 使用别名算法进行随机抽样
        return repository.getStrategyAwardByAlias(key);
    }

    @Override
    public Boolean subtractionAwardStock(Long strategyId, Integer awardId) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        return repository.subtractionAwardStock(cacheKey);
    }
}