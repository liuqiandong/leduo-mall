
package com.leduo.mall.service;

import com.leduo.mall.entity.LeDuoMallGoods;
import com.leduo.mall.util.PageQueryUtil;
import com.leduo.mall.util.PageResult;

import java.util.List;

public interface LeDuoMallGoodsService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getNewBeeMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String saveNewBeeMallGoods(LeDuoMallGoods goods);

    /**
     * 批量新增商品数据
     *
     * @param leDuoMallGoodsList
     * @return
     */
    void batchSaveNewBeeMallGoods(List<LeDuoMallGoods> leDuoMallGoodsList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateNewBeeMallGoods(LeDuoMallGoods goods);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    LeDuoMallGoods getNewBeeMallGoodsById(Long id);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids,int sellStatus);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchNewBeeMallGoods(PageQueryUtil pageUtil);
}
