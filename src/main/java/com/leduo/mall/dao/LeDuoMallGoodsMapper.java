
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