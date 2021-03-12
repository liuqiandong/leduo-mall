/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.leduo.mall.dao;

import com.leduo.mall.entity.LeDuoMallGoods;
import com.leduo.mall.entity.StockNumDTO;
import com.leduo.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LeDuoMallGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(LeDuoMallGoods record);

    int insertSelective(LeDuoMallGoods record);

    LeDuoMallGoods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(LeDuoMallGoods record);

    int updateByPrimaryKeyWithBLOBs(LeDuoMallGoods record);

    int updateByPrimaryKey(LeDuoMallGoods record);

    List<LeDuoMallGoods> findNewBeeMallGoodsList(PageQueryUtil pageUtil);

    int getTotalNewBeeMallGoods(PageQueryUtil pageUtil);

    List<LeDuoMallGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<LeDuoMallGoods> findNewBeeMallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalNewBeeMallGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("newBeeMallGoodsList") List<LeDuoMallGoods> leDuoMallGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

}