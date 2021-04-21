
package com.leduo.mall.service;

import com.leduo.mall.controller.vo.LeDuoMallOrderDetailVO;
import com.leduo.mall.controller.vo.LeDuoMallOrderItemVO;
import com.leduo.mall.controller.vo.LeDuoMallShoppingCartItemVO;
import com.leduo.mall.controller.vo.LeDuoMallUserVO;
import com.leduo.mall.entity.LeDuoMallOrder;
import com.leduo.mall.util.PageQueryUtil;
import com.leduo.mall.util.PageResult;

import java.util.List;

public interface LeDuoMallOrderService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getNewBeeMallOrdersPage(PageQueryUtil pageUtil);

    /**
     * 订单信息修改
     *
     * @param leDuoMallOrder
     * @return
     */
    String updateOrderInfo(LeDuoMallOrder leDuoMallOrder);

    /**
     * 配货
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    /**
     * 保存订单
     *
     * @param user
     * @param myShoppingCartItems
     * @return
     */
    String saveOrder(LeDuoMallUserVO user, List<LeDuoMallShoppingCartItemVO> myShoppingCartItems);

    /**
     * 秒杀保存订单
     *
     * @param user
     * @param myShoppingCartItems
     * @return
     */
    String saveOrderm(LeDuoMallUserVO user, List<LeDuoMallShoppingCartItemVO> myShoppingCartItems);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @param userId
     * @return
     */
    LeDuoMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @return
     */
    LeDuoMallOrder getNewBeeMallOrderByOrderNo(String orderNo);

    /**
     * 我的订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    List<LeDuoMallOrderItemVO> getOrderItems(Long id);
}
