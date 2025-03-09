package com.mozzie.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : mozzie
 * @description : 抽奖策略规则规则值对象；值对象，没有唯一ID，仅限于从数据库查询对象
 * @date : 2025/2/22 22:00
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {

    private String ruleModels;
}