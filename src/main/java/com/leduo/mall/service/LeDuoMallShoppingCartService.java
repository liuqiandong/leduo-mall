
package com.leduo.mall.service;

import com.leduo.mall.controller.vo.LeDuoMallShoppingCartItemVO;
import com.leduo.mall.entity.LeDuoMallShoppingCartItem;

import java.util.List;

public interface LeDuoMallShoppingCartService {

    /**
     * 保存商品至购物车中
     *
     * @param leDuoMallShoppingCartItem
     * @return
     */
    String saveNewBeeMallCartItem(LeDuoMallShoppingCartItem leDuoMallShoppingCartItem);

    /**
     * 修改购物车中的属性
     *
     * @param leDuoMallShoppingCartItem
     * @return
     */
    String updateNewBeeMallCartItem(LeDuoMallShoppingCartItem leDuoMallShoppingCartItem);

    /**
     * 获取购物项详情
     *
     * @param newBeeMallShoppingCartItemId
     * @return
     */
    LeDuoMallShoppingCartItem getNewBeeMallCartItemById(Long newBeeMallShoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     * @param newBeeMallShoppingCartItemId
     * @return
     */
    Boolean deleteById(Long newBeeMallShoppingCartItemId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param newBeeMallUserId
     * @return
     */
    List<LeDuoMallShoppingCartItemVO> getMyShoppingCartItems(Long newBeeMallUserId);
}
