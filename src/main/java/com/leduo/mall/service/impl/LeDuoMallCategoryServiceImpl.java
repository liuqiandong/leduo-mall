
package com.leduo.mall.service.impl;

import com.leduo.mall.common.Constants;
import com.leduo.mall.common.LeDuoMallCategoryLevelEnum;
import com.leduo.mall.common.ServiceResultEnum;
import com.leduo.mall.controller.vo.LeDuoMallIndexCategoryVO;
import com.leduo.mall.controller.vo.SearchPageCategoryVO;
import com.leduo.mall.controller.vo.SecondLevelCategoryVO;
import com.leduo.mall.controller.vo.ThirdLevelCategoryVO;
import com.leduo.mall.dao.GoodsCategoryMapper;
import com.leduo.mall.entity.GoodsCategory;
import com.leduo.mall.service.LeDuoMallCategoryService;
import com.leduo.mall.util.BeanUtil;
import com.leduo.mall.util.PageQueryUtil;
import com.leduo.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class LeDuoMallCategoryServiceImpl implements LeDuoMallCategoryService {

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public PageResult getCategorisPage(PageQueryUtil pageUtil) {
        List<GoodsCategory> goodsCategories = goodsCategoryMapper.findGoodsCategoryList(pageUtil);
        int total = goodsCategoryMapper.getTotalGoodsCategories(pageUtil);
        PageResult pageResult = new PageResult(goodsCategories, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveCategory(GoodsCategory goodsCategory) {
        GoodsCategory temp = goodsCategoryMapper.selectByLevelAndName(goodsCategory.getCategoryLevel(), goodsCategory.getCategoryName());
        if (temp != null) {
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        if (goodsCategoryMapper.insertSelective(goodsCategory) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateGoodsCategory(GoodsCategory goodsCategory) {
        GoodsCategory temp = goodsCategoryMapper.selectByPrimaryKey(goodsCategory.getCategoryId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        GoodsCategory temp2 = goodsCategoryMapper.selectByLevelAndName(goodsCategory.getCategoryLevel(), goodsCategory.getCategoryName());
        if (temp2 != null && !temp2.getCategoryId().equals(goodsCategory.getCategoryId())) {
            //???????????????id ??????????????????
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        goodsCategory.setUpdateTime(new Date());
        if (goodsCategoryMapper.updateByPrimaryKeySelective(goodsCategory) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public GoodsCategory getGoodsCategoryById(Long id) {
        return goodsCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //??????????????????
        return goodsCategoryMapper.deleteBatch(ids) > 0;
    }

    @Override
    public List<LeDuoMallIndexCategoryVO> getCategoriesForIndex() {
        List<LeDuoMallIndexCategoryVO> leDuoMallIndexCategoryVOS = new ArrayList<>();
        //??????????????????????????????????????????
        List<GoodsCategory> firstLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), LeDuoMallCategoryLevelEnum.LEVEL_ONE.getLevel(), Constants.INDEX_CATEGORY_NUMBER);
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            List<Long> firstLevelCategoryIds = firstLevelCategories.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());
            //???????????????????????????
            List<GoodsCategory> secondLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(firstLevelCategoryIds, LeDuoMallCategoryLevelEnum.LEVEL_TWO.getLevel(), 0);
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                List<Long> secondLevelCategoryIds = secondLevelCategories.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());
                //???????????????????????????
                List<GoodsCategory> thirdLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(secondLevelCategoryIds, LeDuoMallCategoryLevelEnum.LEVEL_THREE.getLevel(), 0);
                if (!CollectionUtils.isEmpty(thirdLevelCategories)) {
                    //?????? parentId ??? thirdLevelCategories ??????
                    Map<Long, List<GoodsCategory>> thirdLevelCategoryMap = thirdLevelCategories.stream().collect(groupingBy(GoodsCategory::getParentId));
                    List<SecondLevelCategoryVO> secondLevelCategoryVOS = new ArrayList<>();
                    //??????????????????
                    for (GoodsCategory secondLevelCategory : secondLevelCategories) {
                        SecondLevelCategoryVO secondLevelCategoryVO = new SecondLevelCategoryVO();
                        BeanUtil.copyProperties(secondLevelCategory, secondLevelCategoryVO);
                        //?????????????????????????????????????????? secondLevelCategoryVOS ?????????
                        if (thirdLevelCategoryMap.containsKey(secondLevelCategory.getCategoryId())) {
                            //?????????????????????id??????thirdLevelCategoryMap????????????????????????list
                            List<GoodsCategory> tempGoodsCategories = thirdLevelCategoryMap.get(secondLevelCategory.getCategoryId());
                            secondLevelCategoryVO.setThirdLevelCategoryVOS((BeanUtil.copyList(tempGoodsCategories, ThirdLevelCategoryVO.class)));
                            secondLevelCategoryVOS.add(secondLevelCategoryVO);
                        }
                    }
                    //??????????????????
                    if (!CollectionUtils.isEmpty(secondLevelCategoryVOS)) {
                        //?????? parentId ??? thirdLevelCategories ??????
                        Map<Long, List<SecondLevelCategoryVO>> secondLevelCategoryVOMap = secondLevelCategoryVOS.stream().collect(groupingBy(SecondLevelCategoryVO::getParentId));
                        for (GoodsCategory firstCategory : firstLevelCategories) {
                            LeDuoMallIndexCategoryVO leDuoMallIndexCategoryVO = new LeDuoMallIndexCategoryVO();
                            BeanUtil.copyProperties(firstCategory, leDuoMallIndexCategoryVO);
                            //?????????????????????????????????????????? newBeeMallIndexCategoryVOS ?????????
                            if (secondLevelCategoryVOMap.containsKey(firstCategory.getCategoryId())) {
                                //?????????????????????id??????secondLevelCategoryVOMap???????????????????????????list
                                List<SecondLevelCategoryVO> tempGoodsCategories = secondLevelCategoryVOMap.get(firstCategory.getCategoryId());
                                leDuoMallIndexCategoryVO.setSecondLevelCategoryVOS(tempGoodsCategories);
                                leDuoMallIndexCategoryVOS.add(leDuoMallIndexCategoryVO);
                            }
                        }
                    }
                }
            }
            return leDuoMallIndexCategoryVOS;
        } else {
            return null;
        }
    }

    @Override
    public SearchPageCategoryVO getCategoriesForSearch(Long categoryId) {
        SearchPageCategoryVO searchPageCategoryVO = new SearchPageCategoryVO();
        GoodsCategory thirdLevelGoodsCategory = goodsCategoryMapper.selectByPrimaryKey(categoryId);
        if (thirdLevelGoodsCategory != null && thirdLevelGoodsCategory.getCategoryLevel() == LeDuoMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
            //???????????????????????????????????????
            GoodsCategory secondLevelGoodsCategory = goodsCategoryMapper.selectByPrimaryKey(thirdLevelGoodsCategory.getParentId());
            if (secondLevelGoodsCategory != null && secondLevelGoodsCategory.getCategoryLevel() == LeDuoMallCategoryLevelEnum.LEVEL_TWO.getLevel()) {
                //??????????????????????????????????????????List
                List<GoodsCategory> thirdLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelGoodsCategory.getCategoryId()), LeDuoMallCategoryLevelEnum.LEVEL_THREE.getLevel(), Constants.SEARCH_CATEGORY_NUMBER);
                searchPageCategoryVO.setCurrentCategoryName(thirdLevelGoodsCategory.getCategoryName());
                searchPageCategoryVO.setSecondLevelCategoryName(secondLevelGoodsCategory.getCategoryName());
                searchPageCategoryVO.setThirdLevelCategoryList(thirdLevelCategories);
                return searchPageCategoryVO;
            }
        }
        return null;
    }

    @Override
    public List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel) {
        return goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(parentIds, categoryLevel, 0);//0??????????????????
    }
}
