/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.leduo.mall.dao;

import com.leduo.mall.entity.LeDuoMallOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LeDuoMallOrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);

    int insert(LeDuoMallOrderItem record);

    int insertSelective(LeDuoMallOrderItem record);

    LeDuoMallOrderItem selectByPrimaryKey(Long orderItemId);

    /**
     * 根据订单id获取订单项列表
     *
     * @param orderId
     * @return
     */
    List<LeDuoMallOrderItem> selectByOrderId(Long orderId);

    /**
     * 根据订单ids获取订单项列表
     *
     * @param orderIds
     * @return
     */
    List<LeDuoMallOrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 批量insert订单项数据
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<LeDuoMallOrderItem> orderItems);

    int updateByPrimaryKeySelective(LeDuoMallOrderItem record);

    int updateByPrimaryKey(LeDuoMallOrderItem record);
}