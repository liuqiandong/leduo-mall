/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.leduo.mall.controller.mall;

import com.leduo.mall.common.Constants;
import com.leduo.mall.common.IndexConfigTypeEnum;
import com.leduo.mall.controller.vo.LeDuoMallIndexCarouselVO;
import com.leduo.mall.controller.vo.LeDuoMallIndexCategoryVO;
import com.leduo.mall.controller.vo.LeDuoMallIndexConfigGoodsVO;
import com.leduo.mall.service.LeDuoMallCarouselService;
import com.leduo.mall.service.LeDuoMallCategoryService;
import com.leduo.mall.service.LeDuoMallIndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class IndexController {

    @Resource
    private LeDuoMallCarouselService leDuoMallCarouselService;

    @Resource
    private LeDuoMallIndexConfigService leDuoMallIndexConfigService;

    @Resource
    private LeDuoMallCategoryService leDuoMallCategoryService;

    @GetMapping({"/index", "/", "/index.html"})
    public String indexPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<LeDuoMallIndexCategoryVO> categories = leDuoMallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
//            return "error/error_5xx";
        }
        List<LeDuoMallIndexCarouselVO> carousels = leDuoMallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<LeDuoMallIndexConfigGoodsVO> hotGoodses = leDuoMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<LeDuoMallIndexConfigGoodsVO> newGoodses = leDuoMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<LeDuoMallIndexConfigGoodsVO> recommendGoodses = leDuoMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        request.setAttribute("categories", categories);//分类数据
        request.setAttribute("carousels", carousels);//轮播图
        request.setAttribute("hotGoodses", hotGoodses);//热销商品
        request.setAttribute("newGoodses", newGoodses);//新品
        request.setAttribute("recommendGoodses", recommendGoodses);//推荐商品
//        request.getRequestDispatcher("mall/index").forward(request, response);
//        response.sendRedirect("index");
        return "mall/index";
    }
}
