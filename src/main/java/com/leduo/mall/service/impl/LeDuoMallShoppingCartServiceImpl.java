/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.leduo.mall.service.impl;

import com.leduo.mall.common.Constants;
import com.leduo.mall.common.ServiceResultEnum;
import com.leduo.mall.controller.vo.LeDuoMallShoppingCartItemVO;
import com.leduo.mall.dao.LeDuoMallGoodsMapper;
import com.leduo.mall.dao.LeDuoMallShoppingCartItemMapper;
import com.leduo.mall.entity.LeDuoMallGoods;
import com.leduo.mall.entity.LeDuoMallShoppingCartItem;
import com.leduo.mall.service.LeDuoMallShoppingCartService;
import com.leduo.mall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LeDuoMallShoppingCartServiceImpl implements LeDuoMallShoppingCartService {

    @Autowired
    private LeDuoMallShoppingCartItemMapper leDuoMallShoppingCartItemMapper;

    @Autowired
    private LeDuoMallGoodsMapper leDuoMallGoodsMapper;

    //todo 修改session中购物项数量

    @Override
    public String saveNewBeeMallCartItem(LeDuoMallShoppingCartItem leDuoMallShoppingCartItem) {
        LeDuoMallShoppingCartItem temp = leDuoMallShoppingCartItemMapper.selectByUserIdAndGoodsId(leDuoMallShoppingCartItem.getUserId(), leDuoMallShoppingCartItem.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            //todo count = tempCount + 1
            temp.setGoodsCount(leDuoMallShoppingCartItem.getGoodsCount());
            return updateNewBeeMallCartItem(temp);
        }
        LeDuoMallGoods leDuoMallGoods = leDuoMallGoodsMapper.selectByPrimaryKey(leDuoMallShoppingCartItem.getGoodsId());
        //商品为空
        if (leDuoMallGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = leDuoMallShoppingCartItemMapper.selectCountByUserId(leDuoMallShoppingCartItem.getUserId()) + 1;
        //超出单个商品的最大数量
        if (leDuoMallShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        //保存记录
        if (leDuoMallShoppingCartItemMapper.insertSelective(leDuoMallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateNewBeeMallCartItem(LeDuoMallShoppingCartItem leDuoMallShoppingCartItem) {
        LeDuoMallShoppingCartItem leDuoMallShoppingCartItemUpdate = leDuoMallShoppingCartItemMapper.selectByPrimaryKey(leDuoMallShoppingCartItem.getCartItemId());
        if (leDuoMallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出单个商品的最大数量
        if (leDuoMallShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //todo 数量相同不会进行修改
        //todo userId不同不能修改
        leDuoMallShoppingCartItemUpdate.setGoodsCount(leDuoMallShoppingCartItem.getGoodsCount());
        leDuoMallShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (leDuoMallShoppingCartItemMapper.updateByPrimaryKeySelective(leDuoMallShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public LeDuoMallShoppingCartItem getNewBeeMallCartItemById(Long newBeeMallShoppingCartItemId) {
        return leDuoMallShoppingCartItemMapper.selectByPrimaryKey(newBeeMallShoppingCartItemId);
    }

    @Override
    public Boolean deleteById(Long newBeeMallShoppingCartItemId) {
        //todo userId不同不能删除
        return leDuoMallShoppingCartItemMapper.deleteByPrimaryKey(newBeeMallShoppingCartItemId) > 0;
    }

    @Override
    public List<LeDuoMallShoppingCartItemVO> getMyShoppingCartItems(Long newBeeMallUserId) {
        List<LeDuoMallShoppingCartItemVO> leDuoMallShoppingCartItemVOS = new ArrayList<>();
        List<LeDuoMallShoppingCartItem> leDuoMallShoppingCartItems = leDuoMallShoppingCartItemMapper.selectByUserId(newBeeMallUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        if (!CollectionUtils.isEmpty(leDuoMallShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> leDuoMallGoodsIds = leDuoMallShoppingCartItems.stream().map(LeDuoMallShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<LeDuoMallGoods> leDuoMallGoods = leDuoMallGoodsMapper.selectByPrimaryKeys(leDuoMallGoodsIds);
            Map<Long, LeDuoMallGoods> leDuoMallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(leDuoMallGoods)) {
                leDuoMallGoodsMap = leDuoMallGoods.stream().collect(Collectors.toMap(LeDuoMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (LeDuoMallShoppingCartItem leDuoMallShoppingCartItem : leDuoMallShoppingCartItems) {
                LeDuoMallShoppingCartItemVO leDuoMallShoppingCartItemVO = new LeDuoMallShoppingCartItemVO();
                BeanUtil.copyProperties(leDuoMallShoppingCartItem, leDuoMallShoppingCartItemVO);
                if (leDuoMallGoodsMap.containsKey(leDuoMallShoppingCartItem.getGoodsId())) {
                    LeDuoMallGoods leDuoMallGoodsTemp = leDuoMallGoodsMap.get(leDuoMallShoppingCartItem.getGoodsId());
                    leDuoMallShoppingCartItemVO.setGoodsCoverImg(leDuoMallGoodsTemp.getGoodsCoverImg());
                    String goodsName = leDuoMallGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    leDuoMallShoppingCartItemVO.setGoodsName(goodsName);
                    leDuoMallShoppingCartItemVO.setSellingPrice(leDuoMallGoodsTemp.getSellingPrice());
                    leDuoMallShoppingCartItemVOS.add(leDuoMallShoppingCartItemVO);
                }
            }
        }
        return leDuoMallShoppingCartItemVOS;
    }
}
