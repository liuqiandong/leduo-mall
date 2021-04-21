
package com.leduo.mall.controller.admin;

import com.leduo.mall.common.Constants;
import com.leduo.mall.common.LeDuoMallCategoryLevelEnum;
import com.leduo.mall.common.ServiceResultEnum;
import com.leduo.mall.entity.GoodsCategory;
import com.leduo.mall.entity.LeDuoMallGoods;
import com.leduo.mall.service.LeDuoMallCategoryService;
import com.leduo.mall.service.LeDuoMallGoodsService;
import com.leduo.mall.util.PageQueryUtil;
import com.leduo.mall.util.Result;
import com.leduo.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * dong
 */
@Controller
@RequestMapping("/admin")
public class LeDuoMallGoodsController {

    @Resource
    private LeDuoMallGoodsService leDuoMallGoodsService;
    @Resource
    private LeDuoMallCategoryService leDuoMallCategoryService;

    @GetMapping("/goods")
    public String goodsPage(HttpServletRequest request) {
        request.setAttribute("path", "leduo_mall_goods");
        return "admin/leduo_mall_goods";
    }

    @GetMapping("/goods/edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        //查询所有的一级分类
        List<GoodsCategory> firstLevelCategories = leDuoMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), LeDuoMallCategoryLevelEnum.LEVEL_ONE.getLevel());
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            //查询一级分类列表中第一个实体的所有二级分类
            List<GoodsCategory> secondLevelCategories = leDuoMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), LeDuoMallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //查询二级分类列表中第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories = leDuoMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), LeDuoMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                request.setAttribute("firstLevelCategories", firstLevelCategories);
                request.setAttribute("secondLevelCategories", secondLevelCategories);
                request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                request.setAttribute("path", "goods-edit");
                return "admin/leduo_mall_goods_edit";
            }
        }
        return "error/error_5xx";
    }

    @GetMapping("/goods/edit/{goodsId}")
    public String edit(HttpServletRequest request, @PathVariable("goodsId") Long goodsId) {
        request.setAttribute("path", "edit");
        LeDuoMallGoods leDuoMallGoods = leDuoMallGoodsService.getNewBeeMallGoodsById(goodsId);
        if (leDuoMallGoods == null) {
            return "error/error_400";
        }
        if (leDuoMallGoods.getGoodsCategoryId() > 0) {
            if (leDuoMallGoods.getGoodsCategoryId() != null || leDuoMallGoods.getGoodsCategoryId() > 0) {
                //有分类字段则查询相关分类数据返回给前端以供分类的三级联动显示
                GoodsCategory currentGoodsCategory = leDuoMallCategoryService.getGoodsCategoryById(leDuoMallGoods.getGoodsCategoryId());
                //商品表中存储的分类id字段为三级分类的id，不为三级分类则是错误数据
                if (currentGoodsCategory != null && currentGoodsCategory.getCategoryLevel() == LeDuoMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
                    //查询所有的一级分类
                    List<GoodsCategory> firstLevelCategories = leDuoMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), LeDuoMallCategoryLevelEnum.LEVEL_ONE.getLevel());
                    //根据parentId查询当前parentId下所有的三级分类
                    List<GoodsCategory> thirdLevelCategories = leDuoMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(currentGoodsCategory.getParentId()), LeDuoMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    //查询当前三级分类的父级二级分类
                    GoodsCategory secondCategory = leDuoMallCategoryService.getGoodsCategoryById(currentGoodsCategory.getParentId());
                    if (secondCategory != null) {
                        //根据parentId查询当前parentId下所有的二级分类
                        List<GoodsCategory> secondLevelCategories = leDuoMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondCategory.getParentId()), LeDuoMallCategoryLevelEnum.LEVEL_TWO.getLevel());
                        //查询当前二级分类的父级一级分类
                        GoodsCategory firestCategory = leDuoMallCategoryService.getGoodsCategoryById(secondCategory.getParentId());
                        if (firestCategory != null) {
                            //所有分类数据都得到之后放到request对象中供前端读取
                            request.setAttribute("firstLevelCategories", firstLevelCategories);
                            request.setAttribute("secondLevelCategories", secondLevelCategories);
                            request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                            request.setAttribute("firstLevelCategoryId", firestCategory.getCategoryId());
                            request.setAttribute("secondLevelCategoryId", secondCategory.getCategoryId());
                            request.setAttribute("thirdLevelCategoryId", currentGoodsCategory.getCategoryId());
                        }
                    }
                }
            }
        }
        if (leDuoMallGoods.getGoodsCategoryId() == 0) {
            //查询所有的一级分类
            List<GoodsCategory> firstLevelCategories = leDuoMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), LeDuoMallCategoryLevelEnum.LEVEL_ONE.getLevel());
            if (!CollectionUtils.isEmpty(firstLevelCategories)) {
                //查询一级分类列表中第一个实体的所有二级分类
                List<GoodsCategory> secondLevelCategories = leDuoMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), LeDuoMallCategoryLevelEnum.LEVEL_TWO.getLevel());
                if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                    //查询二级分类列表中第一个实体的所有三级分类
                    List<GoodsCategory> thirdLevelCategories = leDuoMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), LeDuoMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    request.setAttribute("firstLevelCategories", firstLevelCategories);
                    request.setAttribute("secondLevelCategories", secondLevelCategories);
                    request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                }
            }
        }
        request.setAttribute("goods", leDuoMallGoods);
        request.setAttribute("path", "goods-edit");
        return "admin/leduo_mall_goods_edit";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(leDuoMallGoodsService.getNewBeeMallGoodsPage(pageUtil));
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/goods/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody LeDuoMallGoods leDuoMallGoods) {
        if (StringUtils.isEmpty(leDuoMallGoods.getGoodsName())
                || StringUtils.isEmpty(leDuoMallGoods.getGoodsIntro())
                || StringUtils.isEmpty(leDuoMallGoods.getTag())
                || Objects.isNull(leDuoMallGoods.getOriginalPrice())
                || Objects.isNull(leDuoMallGoods.getGoodsCategoryId())
                || Objects.isNull(leDuoMallGoods.getSellingPrice())
                || Objects.isNull(leDuoMallGoods.getStockNum())
                || Objects.isNull(leDuoMallGoods.getGoodsSellStatus())
                || StringUtils.isEmpty(leDuoMallGoods.getGoodsCoverImg())
                || StringUtils.isEmpty(leDuoMallGoods.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = leDuoMallGoodsService.saveNewBeeMallGoods(leDuoMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * 修改
     */
    @RequestMapping(value = "/goods/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody LeDuoMallGoods leDuoMallGoods) {
        if (Objects.isNull(leDuoMallGoods.getGoodsId())
                || StringUtils.isEmpty(leDuoMallGoods.getGoodsName())
                || StringUtils.isEmpty(leDuoMallGoods.getGoodsIntro())
                || StringUtils.isEmpty(leDuoMallGoods.getTag())
                || Objects.isNull(leDuoMallGoods.getOriginalPrice())
                || Objects.isNull(leDuoMallGoods.getSellingPrice())
                || Objects.isNull(leDuoMallGoods.getGoodsCategoryId())
                || Objects.isNull(leDuoMallGoods.getStockNum())
                || Objects.isNull(leDuoMallGoods.getGoodsSellStatus())
                || StringUtils.isEmpty(leDuoMallGoods.getGoodsCoverImg())
                || StringUtils.isEmpty(leDuoMallGoods.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = leDuoMallGoodsService.updateNewBeeMallGoods(leDuoMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/goods/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        LeDuoMallGoods goods = leDuoMallGoodsService.getNewBeeMallGoodsById(id);
        if (goods == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(goods);
    }

    /**
     * 批量修改销售状态
     */
    @RequestMapping(value = "/goods/status/{sellStatus}", method = RequestMethod.PUT)
    @ResponseBody
    public Result delete(@RequestBody Long[] ids, @PathVariable("sellStatus") int sellStatus) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
            return ResultGenerator.genFailResult("状态异常！");
        }
        if (leDuoMallGoodsService.batchUpdateSellStatus(ids, sellStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }

}