
package com.leduo.mall.controller.mall;

import com.leduo.mall.common.Constants;
import com.leduo.mall.common.ServiceResultEnum;
import com.leduo.mall.controller.vo.LeDuoMallShoppingCartItemVO;
import com.leduo.mall.controller.vo.LeDuoMallUserVO;
import com.leduo.mall.entity.LeDuoMallShoppingCartItem;
import com.leduo.mall.service.LeDuoMallShoppingCartService;
import com.leduo.mall.util.Result;
import com.leduo.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShoppingCartController {

    @Resource
    private LeDuoMallShoppingCartService leDuoMallShoppingCartService;

    @GetMapping("/shop-cart")
    public String cartListPage(HttpServletRequest request,
                               HttpSession httpSession) {
        LeDuoMallUserVO user = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        int itemsTotal = 0;
        int priceTotal = 0;
        List<LeDuoMallShoppingCartItemVO> myShoppingCartItems = leDuoMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (!CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物项总数
            itemsTotal = myShoppingCartItems.stream().mapToInt(LeDuoMallShoppingCartItemVO::getGoodsCount).sum();
            if (itemsTotal < 1) {
                return "error/error_5xx";
            }
            //总价
            for (LeDuoMallShoppingCartItemVO leDuoMallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += leDuoMallShoppingCartItemVO.getGoodsCount() * leDuoMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("itemsTotal", itemsTotal);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/cart";
    }

    @PostMapping("/shop-cart")
    @ResponseBody
    public Result saveNewBeeMallShoppingCartItem(@RequestBody LeDuoMallShoppingCartItem leDuoMallShoppingCartItem,
                                                 HttpSession httpSession) {
        LeDuoMallUserVO user = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        leDuoMallShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String saveResult = leDuoMallShoppingCartService.saveNewBeeMallCartItem(leDuoMallShoppingCartItem);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ResponseBody
    public Result updateNewBeeMallShoppingCartItem(@RequestBody LeDuoMallShoppingCartItem leDuoMallShoppingCartItem,
                                                   HttpSession httpSession) {
        LeDuoMallUserVO user = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        leDuoMallShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String updateResult = leDuoMallShoppingCartService.updateNewBeeMallCartItem(leDuoMallShoppingCartItem);
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{newBeeMallShoppingCartItemId}")
    @ResponseBody
    public Result updateNewBeeMallShoppingCartItem(@PathVariable("newBeeMallShoppingCartItemId") Long newBeeMallShoppingCartItemId,
                                                   HttpSession httpSession) {
        LeDuoMallUserVO user = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Boolean deleteResult = leDuoMallShoppingCartService.deleteById(newBeeMallShoppingCartItemId);
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    public String settlePage(HttpServletRequest request,
                             HttpSession httpSession) {
        int priceTotal = 0;
        LeDuoMallUserVO user = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<LeDuoMallShoppingCartItemVO> myShoppingCartItems = leDuoMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //无数据则不跳转至结算页
            return "/shop-cart";
        } else {
            //总价
            for (LeDuoMallShoppingCartItemVO leDuoMallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += leDuoMallShoppingCartItemVO.getGoodsCount() * leDuoMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/order-settle";
    }

    /*
    * 秒杀提交订单接口
    * */
    @GetMapping("/shop-cart/settlem")
    public String settlePagem(HttpServletRequest request,
                              HttpSession httpSession) {
        int priceTotal = 0;
        LeDuoMallUserVO user = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<LeDuoMallShoppingCartItemVO> myShoppingCartItems = leDuoMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //无数据则不跳转至结算页
            return "/shop-cart";
        } else {
            //总价
            for (LeDuoMallShoppingCartItemVO leDuoMallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += leDuoMallShoppingCartItemVO.getGoodsCount() * leDuoMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/miaosha-settle";
    }


}
