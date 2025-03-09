package com.mozzie.infrastructure.persistent.dao;

import com.mozzie.infrastructure.persistent.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : mozzie
 * @description : 奖品表DAO
 * @date : 2025/2/21 14:42
 */
@Mapper
public interface IAwardDao {

    List<Award> queryAwardList();
    
}
