
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