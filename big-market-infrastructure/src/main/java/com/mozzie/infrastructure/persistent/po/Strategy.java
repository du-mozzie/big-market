package com.mozzie.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @author : mozzie
 * @description : 策略表
 * @date : 2025/2/19 16:34
 */
@Data
public class Strategy {

    /** 自增ID */
    private Long id;
    /** 抽奖策略ID */
    private Long strategyId;
    /** 抽奖策略描述 */
    private String strategyDesc;
    /** 抽奖规则模型 */
    private String ruleModels;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}