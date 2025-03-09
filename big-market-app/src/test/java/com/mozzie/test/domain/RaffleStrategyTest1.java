package com.mozzie.test.domain;

import com.alibaba.fastjson.JSON;
import com.mozzie.domain.strategy.model.entity.RaffleAwardEntity;
import com.mozzie.domain.strategy.model.entity.RaffleFactorEntity;
import com.mozzie.domain.strategy.service.IRaffleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author : mozzie
 * @description : 测试 黑名单、权重 规则
 * @date : 2025/2/22 19:51
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleStrategyTest1 {

    @Resource
    private IRaffleStrategy raffleStrategy;

    // @Resource
    // private RuleWeightLogicFilter ruleWeightLogicFilter;

    // @Before
    // public void setUp() {
    //     ReflectionTestUtils.setField(ruleWeightLogicFilter, "userScore", 4500L);
    // }

    @Test
    public void test_performRaffle() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("mozzie")
                .strategyId(100001L)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_performRaffle_blacklist() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("user003")  // 黑名单用户 user001,user002,user003
                .strategyId(100001L)
                .build();

        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

}
