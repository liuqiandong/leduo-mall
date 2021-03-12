package com.leduo.mall.controller.mall;

import com.leduo.mall.common.Constants;
import com.leduo.mall.controller.vo.LeDuoMallIndexConfigGoodsVO;
import com.leduo.mall.service.LeDuoMallIndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/*
* 秒杀接口
* */
@Controller
public class MiaoShaController {

    @Resource
    private LeDuoMallIndexConfigService leDuoMallIndexConfigService;

    @GetMapping("/miaosha")
    public String miaosha(HttpServletRequest request){
        List<LeDuoMallIndexConfigGoodsVO> recommendGoodses = leDuoMallIndexConfigService.getConfigGoodsesForIndex(6, Constants.INDEX_GOODS_RECOMMOND_SECKILL);
        request.setAttribute("recommendGoodses", recommendGoodses);//秒杀商品
    return "mall/miaosha";
    }
}
