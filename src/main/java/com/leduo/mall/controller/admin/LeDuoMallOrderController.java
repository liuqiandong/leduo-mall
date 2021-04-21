
package com.leduo.mall.controller.admin;

import com.leduo.mall.common.ServiceResultEnum;
import com.leduo.mall.controller.vo.LeDuoMallOrderItemVO;
import com.leduo.mall.entity.LeDuoMallOrder;
import com.leduo.mall.service.LeDuoMallOrderService;
import com.leduo.mall.util.PageQueryUtil;
import com.leduo.mall.util.Result;
import com.leduo.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * dong
 */
@Controller
@RequestMapping("/admin")
public class LeDuoMallOrderController {

    @Resource
    private LeDuoMallOrderService leDuoMallOrderService;

    @GetMapping("/orders")
    public String ordersPage(HttpServletRequest request) {
        request.setAttribute("path", "orders");
        System.out.println(request);
        return "admin/leduo_mall_order";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/orders/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(leDuoMallOrderService.getNewBeeMallOrdersPage(pageUtil));
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/orders/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody LeDuoMallOrder leDuoMallOrder) {
        if (Objects.isNull(leDuoMallOrder.getTotalPrice())
                || Objects.isNull(leDuoMallOrder.getOrderId())
                || leDuoMallOrder.getOrderId() < 1
                || leDuoMallOrder.getTotalPrice() < 1
                || StringUtils.isEmpty(leDuoMallOrder.getUserAddress())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = leDuoMallOrderService.updateOrderInfo(leDuoMallOrder);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/order-items/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        List<LeDuoMallOrderItemVO> orderItems = leDuoMallOrderService.getOrderItems(id);
        if (!CollectionUtils.isEmpty(orderItems)) {
            return ResultGenerator.genSuccessResult(orderItems);
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
    }

    /**
     * 配货
     */
    @RequestMapping(value = "/orders/checkDone", method = RequestMethod.POST)
    @ResponseBody
    public Result checkDone(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = leDuoMallOrderService.checkDone(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 出库
     */
    @RequestMapping(value = "/orders/checkOut", method = RequestMethod.POST)
    @ResponseBody
    public Result checkOut(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = leDuoMallOrderService.checkOut(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/orders/close", method = RequestMethod.POST)
    @ResponseBody
    public Result closeOrder(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = leDuoMallOrderService.closeOrder(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


}