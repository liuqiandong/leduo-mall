
package com.leduo.mall.service.impl;

import com.leduo.mall.common.ServiceResultEnum;
import com.leduo.mall.controller.vo.LeDuoMallIndexConfigGoodsVO;
import com.leduo.mall.dao.IndexConfigMapper;
import com.leduo.mall.dao.LeDuoMallGoodsMapper;
import com.leduo.mall.entity.IndexConfig;
import com.leduo.mall.entity.LeDuoMallGoods;
import com.leduo.mall.service.LeDuoMallIndexConfigService;
import com.leduo.mall.util.BeanUtil;
import com.leduo.mall.util.PageQueryUtil;
import com.leduo.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeDuoMallIndexConfigServiceImpl implements LeDuoMallIndexConfigService {

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Autowired
    private LeDuoMallGoodsMapper goodsMapper;

    @Override
    public PageResult getConfigsPage(PageQueryUtil pageUtil) {
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageUtil);
        int total = indexConfigMapper.getTotalIndexConfigs(pageUtil);
        PageResult pageResult = new PageResult(indexConfigs, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        if (indexConfigMapper.insertSelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public IndexConfig getIndexConfigById(Long id) {
        return null;
    }

    @Override
    public List<LeDuoMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<LeDuoMallIndexConfigGoodsVO> leDuoMallIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<LeDuoMallGoods> leDuoMallGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
            leDuoMallIndexConfigGoodsVOS = BeanUtil.copyList(leDuoMallGoods, LeDuoMallIndexConfigGoodsVO.class);
            for (LeDuoMallIndexConfigGoodsVO leDuoMallIndexConfigGoodsVO : leDuoMallIndexConfigGoodsVOS) {
                String goodsName = leDuoMallIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = leDuoMallIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    leDuoMallIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    leDuoMallIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return leDuoMallIndexConfigGoodsVOS;
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除数据
        return indexConfigMapper.deleteBatch(ids) > 0;
    }
}
