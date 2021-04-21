
package com.leduo.mall.controller.mall;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.leduo.mall.common.Constants;
import com.leduo.mall.common.LeDuoMallException;
import com.leduo.mall.common.PayTypeEnum;
import com.leduo.mall.common.ServiceResultEnum;
import com.leduo.mall.config.AlipayConfig;
import com.leduo.mall.controller.vo.LeDuoMallIndexConfigGoodsVO;
import com.leduo.mall.controller.vo.LeDuoMallOrderDetailVO;
import com.leduo.mall.controller.vo.LeDuoMallShoppingCartItemVO;
import com.leduo.mall.controller.vo.LeDuoMallUserVO;
import com.leduo.mall.dao.LeDuoMallOrderItemMapper;
import com.leduo.mall.entity.LeDuoMallOrder;
import com.leduo.mall.entity.LeDuoMallOrderItem;
import com.leduo.mall.service.LeDuoMallIndexConfigService;
import com.leduo.mall.service.LeDuoMallOrderService;
import com.leduo.mall.service.LeDuoMallShoppingCartService;
import com.leduo.mall.util.PageQueryUtil;
import com.leduo.mall.util.Result;
import com.leduo.mall.util.ResultGenerator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Resource
    private LeDuoMallShoppingCartService leDuoMallShoppingCartService;
    @Resource
    private LeDuoMallOrderService leDuoMallOrderService;
    @Resource
    private LeDuoMallIndexConfigService leDuoMallIndexConfigService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/orders/{orderNo}")
    public String orderDetailPage(HttpServletRequest request, @PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        LeDuoMallUserVO user = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        LeDuoMallOrderDetailVO orderDetailVO = leDuoMallOrderService.getOrderDetailByOrderNo(orderNo, user.getUserId());
        if (orderDetailVO == null) {
            return "error/error_5xx";
        }
        request.setAttribute("orderDetailVO", orderDetailVO);
        return "mall/order-detail";
    }

    @GetMapping("/orders")
    public String orderListPage(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpSession httpSession) {
        LeDuoMallUserVO user = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        params.put("userId", user.getUserId());
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        //封装我的订单数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("orderPageResult", leDuoMallOrderService.getMyOrders(pageUtil));
        request.setAttribute("path", "orders");
        return "mall/my-orders";
    }

    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession httpSession) {
        LeDuoMallUserVO user = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<LeDuoMallShoppingCartItemVO> myShoppingCartItems = leDuoMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (StringUtils.isEmpty(user.getAddress().trim())) {
            //无收货地址
            LeDuoMallException.fail(ServiceResultEnum.NULL_ADDRESS_ERROR.getResult());
        }
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物车中无数据则跳转至错误页
            LeDuoMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        }
        //保存订单并返回订单号
        String saveOrderResult = leDuoMallOrderService.saveOrder(user, myShoppingCartItems);
        //跳转到订单详情页
        return "redirect:/orders/" + saveOrderResult;
    }

    @PostConstruct
    public void init(){
        List<LeDuoMallIndexConfigGoodsVO> recommendGoodses = leDuoMallIndexConfigService.getConfigGoodsesForIndex(6, Constants.INDEX_GOODS_RECOMMOND_SECKILL);
        for (LeDuoMallIndexConfigGoodsVO r : recommendGoodses) {
            stringRedisTemplate.opsForValue().set(Constants.REDIS_PRODUCT_PERFIX+r.getGoodsId(),1000 + "");
        }
    }

    @GetMapping("/saveOrderm")
    public String saveOrderm(HttpSession httpSession) {
        LeDuoMallUserVO user = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<LeDuoMallShoppingCartItemVO> myShoppingCartItems = leDuoMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (StringUtils.isEmpty(user.getAddress().trim())) {
            //无收货地址
            LeDuoMallException.fail(ServiceResultEnum.NULL_ADDRESS_ERROR.getResult());
        }
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物车中无数据则跳转至错误页
            LeDuoMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        }
        //保存订单并返回订单号
        String saveOrderResult = leDuoMallOrderService.saveOrderm(user, myShoppingCartItems);
        //跳转到订单详情页
        return "redirect:/orders/" + saveOrderResult;
    }

    @PutMapping("/orders/{orderNo}/cancel")
    @ResponseBody
    public Result cancelOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        LeDuoMallUserVO user = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String cancelOrderResult = leDuoMallOrderService.cancelOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }

    @PutMapping("/orders/{orderNo}/finish")
    @ResponseBody
    public Result finishOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        LeDuoMallUserVO user = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String finishOrderResult = leDuoMallOrderService.finishOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }

    @GetMapping("/selectPayType")
    public String selectPayType(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession) {
        LeDuoMallUserVO user = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        LeDuoMallOrder leDuoMallOrder = leDuoMallOrderService.getNewBeeMallOrderByOrderNo(orderNo);
        //todo 判断订单userId
        //todo 判断订单状态
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", leDuoMallOrder.getTotalPrice());
        return "mall/pay-select";
    }

    @GetMapping("/payPage")
    public void payOrder(@RequestParam("orderNo") String orderNo, HttpSession httpSession, HttpServletResponse response, @RequestParam("payType") int payType) throws Exception {
        LeDuoMallUserVO user = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        LeDuoMallOrder leDuoMallOrder = leDuoMallOrderService.getNewBeeMallOrderByOrderNo(orderNo);
        //todo 判断订单userId
        //todo 判断订单状态
        httpSession.setAttribute("totalPrice", leDuoMallOrder.getTotalPrice());
        httpSession.setAttribute("orderNo",orderNo);
        if (payType == 1) {
//支付宝支付
            System.out.println("正在支付");
            DefaultAlipayClient client = new DefaultAlipayClient(AlipayConfig.gatewayUrl,AlipayConfig.app_id,AlipayConfig.merchant_private_key,"json",AlipayConfig.charset,AlipayConfig.alipay_public_key,AlipayConfig.sign_type);

            AlipayTradePagePayRequest alipayTradePagePayRequest = new AlipayTradePagePayRequest();
            //alipayTradePagePayRequest.setNotifyUrl(AlipayConfig.notify_url);//调方法
            alipayTradePagePayRequest.setReturnUrl(AlipayConfig.return_url);//跳页面
            Map<String,Object> map = new HashMap<>();
            map.put("out_trade_no",httpSession.getAttribute("orderNo"));//订单号
            map.put("product_code","FAST_INSTANT_TRADE_PAY");//PC端支付方式
            map.put("total_amount",httpSession.getAttribute("totalPrice")+".00");//交易金额
            map.put("subject","乐多电商");
            String string = JSONObject.toJSONString(map);
            alipayTradePagePayRequest.setBizContent(string);
            String body = client.pageExecute(alipayTradePagePayRequest).getBody();
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(body);
            response.getWriter().flush();
            response.getWriter().close();
        } else {
            response.sendRedirect("/weixin/pay");
        }
    }

    @GetMapping("/weixin/pay")
    public String pay(HttpSession session,HttpServletRequest request){
        request.setAttribute("orderNo",session.getAttribute("orderNo"));
        request.setAttribute("totalPrice",session.getAttribute("totalPrice"));
        leDuoMallOrderService.paySuccess((String) session.getAttribute("orderNo"), PayTypeEnum.WEIXIN_PAY.getPayType());
        return "mall/wxpay";
    }

    @GetMapping("/alipay")
    public void alipay(HttpSession session,HttpServletResponse response) throws IOException {
        leDuoMallOrderService.paySuccess((String) session.getAttribute("orderNo"), PayTypeEnum.ALI_PAY.getPayType());
        response.sendRedirect("/index");
    }

    @GetMapping("/paySuccess")
    @ResponseBody
    public Result paySuccess(@RequestParam("orderNo") String orderNo, @RequestParam("payType") int payType) {
        String payResult = leDuoMallOrderService.paySuccess(orderNo, payType);
        if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(payResult);
        }
    }

}
