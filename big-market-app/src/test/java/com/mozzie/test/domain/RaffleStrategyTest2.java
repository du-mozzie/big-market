package com.mozzie.test.domain;

import com.alibaba.fastjson.JSON;
import com.mozzie.domain.strategy.model.entity.RaffleAwardEntity;
import com.mozzie.domain.strategy.model.entity.RaffleFactorEntity;
import com.mozzie.domain.strategy.service.IRaffleStrategy;
import com.mozzie.domain.strategy.service.armory.StrategyArmoryDispatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author : mozzie
 * @description : 测试抽奖中规则，次数拦截测试
 * @date : 2025/2/22 22:35
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleStrategyTest2 {

    private final Long strategyId = 100003L;
    @Resource
    private IRaffleStrategy raffleStrategy;
    @Resource
    private StrategyArmoryDispatch strategyArmory;

    @Before
    public void setUp() {
        boolean success = strategyArmory.assembleLotteryStrategy(strategyId);
    }

    @Test
    public void test_performRaffle() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("mozzie")
                .strategyId(strategyId)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

}
