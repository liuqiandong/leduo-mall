/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.leduo.mall.dao;

import com.leduo.mall.entity.LeDuoMallShoppingCartItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface LeDuoMallShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(LeDuoMallShoppingCartItem record);

    int insertSelective(LeDuoMallShoppingCartItem record);

    LeDuoMallShoppingCartItem selectByPrimaryKey(Long cartItemId);

    LeDuoMallShoppingCartItem selectByUserIdAndGoodsId(@Param("newBeeMallUserId") Long newBeeMallUserId, @Param("goodsId") Long goodsId);

    List<LeDuoMallShoppingCartItem> selectByUserId(@Param("newBeeMallUserId") Long newBeeMallUserId, @Param("number") int number);

    int selectCountByUserId(Long newBeeMallUserId);

    int updateByPrimaryKeySelective(LeDuoMallShoppingCartItem record);

    int updateByPrimaryKey(LeDuoMallShoppingCartItem record);

    int deleteBatch(List<Long> ids);
}