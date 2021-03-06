
package com.leduo.mall.controller.mall;

import com.leduo.mall.common.Constants;
import com.leduo.mall.common.IndexConfigTypeEnum;
import com.leduo.mall.controller.vo.LeDuoMallIndexCarouselVO;
import com.leduo.mall.controller.vo.LeDuoMallIndexCategoryVO;
import com.leduo.mall.controller.vo.LeDuoMallIndexConfigGoodsVO;
import com.leduo.mall.service.LeDuoMallCarouselService;
import com.leduo.mall.service.LeDuoMallCategoryService;
import com.leduo.mall.service.LeDuoMallIndexConfigService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            return "error/error_5xx";
        }
        List<LeDuoMallIndexCarouselVO> carousels = leDuoMallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<LeDuoMallIndexConfigGoodsVO> hotGoodses = leDuoMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<LeDuoMallIndexConfigGoodsVO> newGoodses = leDuoMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<LeDuoMallIndexConfigGoodsVO> recommendGoodses = leDuoMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        request.setAttribute("categories", categories);//????????????
        request.setAttribute("carousels", carousels);//?????????
        request.setAttribute("hotGoodses", hotGoodses);//????????????
        request.setAttribute("newGoodses", newGoodses);//??????
        request.setAttribute("recommendGoodses", recommendGoodses);//????????????
        log.info("?????????????????????");
        return "mall/index";
    }
}
