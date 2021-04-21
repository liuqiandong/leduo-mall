
package com.leduo.mall.dao;

import com.leduo.mall.entity.LeDuoMallOrder;
import com.leduo.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LeDuoMallOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(LeDuoMallOrder record);

    int insertSelective(LeDuoMallOrder record);

    LeDuoMallOrder selectByPrimaryKey(Long orderId);

    LeDuoMallOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(LeDuoMallOrder record);

    int updateByPrimaryKey(LeDuoMallOrder record);

    List<LeDuoMallOrder> findNewBeeMallOrderList(PageQueryUtil pageUtil);

    int getTotalNewBeeMallOrders(PageQueryUtil pageUtil);

    List<LeDuoMallOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);
}