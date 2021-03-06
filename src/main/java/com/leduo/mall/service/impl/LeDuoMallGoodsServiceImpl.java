
package com.leduo.mall.service.impl;

import com.leduo.mall.common.ServiceResultEnum;
import com.leduo.mall.controller.vo.LeDuoMallSearchGoodsVO;
import com.leduo.mall.dao.LeDuoMallGoodsMapper;
import com.leduo.mall.entity.LeDuoMallGoods;
import com.leduo.mall.service.LeDuoMallGoodsService;
import com.leduo.mall.util.BeanUtil;
import com.leduo.mall.util.PageQueryUtil;
import com.leduo.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LeDuoMallGoodsServiceImpl implements LeDuoMallGoodsService {

    @Autowired
    private LeDuoMallGoodsMapper goodsMapper;

    @Override
    public PageResult getNewBeeMallGoodsPage(PageQueryUtil pageUtil) {
        List<LeDuoMallGoods> goodsList = goodsMapper.findNewBeeMallGoodsList(pageUtil);
        int total = goodsMapper.getTotalNewBeeMallGoods(pageUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveNewBeeMallGoods(LeDuoMallGoods goods) {
        if (goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void batchSaveNewBeeMallGoods(List<LeDuoMallGoods> leDuoMallGoodsList) {
        if (!CollectionUtils.isEmpty(leDuoMallGoodsList)) {
            goodsMapper.batchInsert(leDuoMallGoodsList);
        }
    }

    @Override
    public String updateNewBeeMallGoods(LeDuoMallGoods goods) {
        LeDuoMallGoods temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public LeDuoMallGoods getNewBeeMallGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }
    
    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }

    @Override
    public PageResult searchNewBeeMallGoods(PageQueryUtil pageUtil) {
        List<LeDuoMallGoods> goodsList = goodsMapper.findNewBeeMallGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalNewBeeMallGoodsBySearch(pageUtil);
        List<LeDuoMallSearchGoodsVO> leDuoMallSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            leDuoMallSearchGoodsVOS = BeanUtil.copyList(goodsList, LeDuoMallSearchGoodsVO.class);
            for (LeDuoMallSearchGoodsVO leDuoMallSearchGoodsVO : leDuoMallSearchGoodsVOS) {
                String goodsName = leDuoMallSearchGoodsVO.getGoodsName();
                String goodsIntro = leDuoMallSearchGoodsVO.getGoodsIntro();
                // ??????????????????????????????????????????
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    leDuoMallSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    leDuoMallSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(leDuoMallSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
