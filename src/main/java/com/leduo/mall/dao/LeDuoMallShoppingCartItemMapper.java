
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