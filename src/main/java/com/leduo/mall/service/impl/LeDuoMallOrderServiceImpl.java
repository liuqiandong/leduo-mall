/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.leduo.mall.service.impl;

import com.leduo.mall.common.*;
import com.leduo.mall.controller.vo.*;
import com.leduo.mall.dao.LeDuoMallGoodsMapper;
import com.leduo.mall.dao.LeDuoMallOrderItemMapper;
import com.leduo.mall.dao.LeDuoMallOrderMapper;
import com.leduo.mall.dao.LeDuoMallShoppingCartItemMapper;
import com.leduo.mall.entity.LeDuoMallGoods;
import com.leduo.mall.entity.LeDuoMallOrder;
import com.leduo.mall.entity.LeDuoMallOrderItem;
import com.leduo.mall.entity.StockNumDTO;
import com.leduo.mall.service.LeDuoMallOrderService;
import com.leduo.mall.util.BeanUtil;
import com.leduo.mall.util.NumberUtil;
import com.leduo.mall.util.PageQueryUtil;
import com.leduo.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class LeDuoMallOrderServiceImpl implements LeDuoMallOrderService {

    @Autowired
    private LeDuoMallOrderMapper leDuoMallOrderMapper;
    @Autowired
    private LeDuoMallOrderItemMapper leDuoMallOrderItemMapper;
    @Autowired
    private LeDuoMallShoppingCartItemMapper leDuoMallShoppingCartItemMapper;
    @Autowired
    private LeDuoMallGoodsMapper leDuoMallGoodsMapper;

    @Override
    public PageResult getNewBeeMallOrdersPage(PageQueryUtil pageUtil) {
        List<LeDuoMallOrder> leDuoMallOrders = leDuoMallOrderMapper.findNewBeeMallOrderList(pageUtil);
        int total = leDuoMallOrderMapper.getTotalNewBeeMallOrders(pageUtil);
        PageResult pageResult = new PageResult(leDuoMallOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(LeDuoMallOrder leDuoMallOrder) {
        LeDuoMallOrder temp = leDuoMallOrderMapper.selectByPrimaryKey(leDuoMallOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(leDuoMallOrder.getTotalPrice());
            temp.setUserAddress(leDuoMallOrder.getUserAddress());
            temp.setUpdateTime(new Date());
            if (leDuoMallOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<LeDuoMallOrder> orders = leDuoMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (LeDuoMallOrder leDuoMallOrder : orders) {
                if (leDuoMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += leDuoMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (leDuoMallOrder.getOrderStatus() != 1) {
                    errorOrderNos += leDuoMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (leDuoMallOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<LeDuoMallOrder> orders = leDuoMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (LeDuoMallOrder leDuoMallOrder : orders) {
                if (leDuoMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += leDuoMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (leDuoMallOrder.getOrderStatus() != 1 && leDuoMallOrder.getOrderStatus() != 2) {
                    errorOrderNos += leDuoMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (leDuoMallOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<LeDuoMallOrder> orders = leDuoMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (LeDuoMallOrder leDuoMallOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (leDuoMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += leDuoMallOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (leDuoMallOrder.getOrderStatus() == 4 || leDuoMallOrder.getOrderStatus() < 0) {
                    errorOrderNos += leDuoMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
                if (leDuoMallOrderMapper.closeOrder(Arrays.asList(ids), LeDuoMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(LeDuoMallUserVO user, List<LeDuoMallShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(LeDuoMallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(LeDuoMallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<LeDuoMallGoods> leDuoMallGoods = leDuoMallGoodsMapper.selectByPrimaryKeys(goodsIds);
        //检查是否包含已下架商品
        List<LeDuoMallGoods> goodsListNotSelling = leDuoMallGoods.stream()
                .filter(newBeeMallGoodsTemp -> newBeeMallGoodsTemp.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
            //goodsListNotSelling 对象非空则表示有下架商品
            LeDuoMallException.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
        }
        Map<Long, LeDuoMallGoods> newBeeMallGoodsMap = leDuoMallGoods.stream().collect(Collectors.toMap(LeDuoMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (LeDuoMallShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!newBeeMallGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                LeDuoMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > newBeeMallGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                LeDuoMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(leDuoMallGoods)) {
            if (leDuoMallShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = leDuoMallGoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    LeDuoMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                LeDuoMallOrder leDuoMallOrder = new LeDuoMallOrder();
                leDuoMallOrder.setOrderNo(orderNo);
                leDuoMallOrder.setUserId(user.getUserId());
                leDuoMallOrder.setUserAddress(user.getAddress());
                //总价
                for (LeDuoMallShoppingCartItemVO leDuoMallShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += leDuoMallShoppingCartItemVO.getGoodsCount() * leDuoMallShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    LeDuoMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                leDuoMallOrder.setTotalPrice(priceTotal);
                //todo 订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串
                String extraInfo = "";
                leDuoMallOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (leDuoMallOrderMapper.insertSelective(leDuoMallOrder) > 0) {
                    //生成所有的订单项快照，并保存至数据库
                    List<LeDuoMallOrderItem> leDuoMallOrderItems = new ArrayList<>();
                    for (LeDuoMallShoppingCartItemVO leDuoMallShoppingCartItemVO : myShoppingCartItems) {
                        LeDuoMallOrderItem leDuoMallOrderItem = new LeDuoMallOrderItem();
                        //使用BeanUtil工具类将newBeeMallShoppingCartItemVO中的属性复制到newBeeMallOrderItem对象中
                        BeanUtil.copyProperties(leDuoMallShoppingCartItemVO, leDuoMallOrderItem);
                        //NewBeeMallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        leDuoMallOrderItem.setOrderId(leDuoMallOrder.getOrderId());
                        leDuoMallOrderItems.add(leDuoMallOrderItem);
                    }
                    //保存至数据库
                    if (leDuoMallOrderItemMapper.insertBatch(leDuoMallOrderItems) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    LeDuoMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                LeDuoMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            LeDuoMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        LeDuoMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }

    /*
    * 秒杀独立接口
    * */
    @Override
    @Transactional
    public String saveOrderm(LeDuoMallUserVO user, List<LeDuoMallShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(LeDuoMallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(LeDuoMallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<LeDuoMallGoods> leDuoMallGoods = leDuoMallGoodsMapper.selectByPrimaryKeys(goodsIds);
        //检查是否包含已下架商品
        List<LeDuoMallGoods> goodsListNotSelling = leDuoMallGoods.stream()
                .filter(newBeeMallGoodsTemp -> newBeeMallGoodsTemp.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
            //goodsListNotSelling 对象非空则表示有下架商品
            LeDuoMallException.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
        }
        Map<Long, LeDuoMallGoods> newBeeMallGoodsMap = leDuoMallGoods.stream().collect(Collectors.toMap(LeDuoMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (LeDuoMallShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!newBeeMallGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                LeDuoMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > newBeeMallGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                LeDuoMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(leDuoMallGoods)) {
            if (leDuoMallShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = leDuoMallGoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    LeDuoMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                LeDuoMallOrder leDuoMallOrder = new LeDuoMallOrder();
                leDuoMallOrder.setOrderNo(orderNo);
                leDuoMallOrder.setUserId(user.getUserId());
                leDuoMallOrder.setUserAddress(user.getAddress());
                //总价
                for (LeDuoMallShoppingCartItemVO leDuoMallShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += leDuoMallShoppingCartItemVO.getGoodsCount() * leDuoMallShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    LeDuoMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                leDuoMallOrder.setTotalPrice(priceTotal);
                //todo 订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串
                String extraInfo = "";
                leDuoMallOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (leDuoMallOrderMapper.insertSelective(leDuoMallOrder) > 0) {
                    //生成所有的订单项快照，并保存至数据库
                    List<LeDuoMallOrderItem> leDuoMallOrderItems = new ArrayList<>();
                    for (LeDuoMallShoppingCartItemVO leDuoMallShoppingCartItemVO : myShoppingCartItems) {
                        LeDuoMallOrderItem leDuoMallOrderItem = new LeDuoMallOrderItem();
                        //使用BeanUtil工具类将newBeeMallShoppingCartItemVO中的属性复制到newBeeMallOrderItem对象中
                        BeanUtil.copyProperties(leDuoMallShoppingCartItemVO, leDuoMallOrderItem);
                        //NewBeeMallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        leDuoMallOrderItem.setOrderId(leDuoMallOrder.getOrderId());
                        leDuoMallOrderItems.add(leDuoMallOrderItem);
                    }
                    //保存至数据库
                    if (leDuoMallOrderItemMapper.insertBatch(leDuoMallOrderItems) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    LeDuoMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                LeDuoMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            LeDuoMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        LeDuoMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }

    @Override
    public LeDuoMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        LeDuoMallOrder leDuoMallOrder = leDuoMallOrderMapper.selectByOrderNo(orderNo);
        if (leDuoMallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            List<LeDuoMallOrderItem> orderItems = leDuoMallOrderItemMapper.selectByOrderId(leDuoMallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<LeDuoMallOrderItemVO> leDuoMallOrderItemVOS = BeanUtil.copyList(orderItems, LeDuoMallOrderItemVO.class);
                LeDuoMallOrderDetailVO leDuoMallOrderDetailVO = new LeDuoMallOrderDetailVO();
                BeanUtil.copyProperties(leDuoMallOrder, leDuoMallOrderDetailVO);
                leDuoMallOrderDetailVO.setOrderStatusString(LeDuoMallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(leDuoMallOrderDetailVO.getOrderStatus()).getName());
                leDuoMallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(leDuoMallOrderDetailVO.getPayType()).getName());
                leDuoMallOrderDetailVO.setNewBeeMallOrderItemVOS(leDuoMallOrderItemVOS);
                return leDuoMallOrderDetailVO;
            }
        }
        return null;
    }

    @Override
    public LeDuoMallOrder getNewBeeMallOrderByOrderNo(String orderNo) {
        return leDuoMallOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = leDuoMallOrderMapper.getTotalNewBeeMallOrders(pageUtil);
        List<LeDuoMallOrder> leDuoMallOrders = leDuoMallOrderMapper.findNewBeeMallOrderList(pageUtil);
        List<LeDuoMallOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(leDuoMallOrders, LeDuoMallOrderListVO.class);
            //设置订单状态中文显示值
            for (LeDuoMallOrderListVO leDuoMallOrderListVO : orderListVOS) {
                leDuoMallOrderListVO.setOrderStatusString(LeDuoMallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(leDuoMallOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = leDuoMallOrders.stream().map(LeDuoMallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<LeDuoMallOrderItem> orderItems = leDuoMallOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<LeDuoMallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(LeDuoMallOrderItem::getOrderId));
                for (LeDuoMallOrderListVO leDuoMallOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(leDuoMallOrderListVO.getOrderId())) {
                        List<LeDuoMallOrderItem> orderItemListTemp = itemByOrderIdMap.get(leDuoMallOrderListVO.getOrderId());
                        //将NewBeeMallOrderItem对象列表转换成leDuoMallOrderItemVO对象列表
                        List<LeDuoMallOrderItemVO> leDuoMallOrderItemVOS = BeanUtil.copyList(orderItemListTemp, LeDuoMallOrderItemVO.class);
                        leDuoMallOrderListVO.setNewBeeMallOrderItemVOS(leDuoMallOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        LeDuoMallOrder leDuoMallOrder = leDuoMallOrderMapper.selectByOrderNo(orderNo);
        if (leDuoMallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            if (leDuoMallOrderMapper.closeOrder(Collections.singletonList(leDuoMallOrder.getOrderId()), LeDuoMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        LeDuoMallOrder leDuoMallOrder = leDuoMallOrderMapper.selectByOrderNo(orderNo);
        if (leDuoMallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            leDuoMallOrder.setOrderStatus((byte) LeDuoMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            leDuoMallOrder.setUpdateTime(new Date());
            if (leDuoMallOrderMapper.updateByPrimaryKeySelective(leDuoMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        LeDuoMallOrder leDuoMallOrder = leDuoMallOrderMapper.selectByOrderNo(orderNo);
        if (leDuoMallOrder != null) {
            //todo 订单状态判断 非待支付状态下不进行修改操作
            leDuoMallOrder.setOrderStatus((byte) LeDuoMallOrderStatusEnum.OREDER_PAID.getOrderStatus());
            leDuoMallOrder.setPayType((byte) payType);
            leDuoMallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            leDuoMallOrder.setPayTime(new Date());
            leDuoMallOrder.setUpdateTime(new Date());
            if (leDuoMallOrderMapper.updateByPrimaryKeySelective(leDuoMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public List<LeDuoMallOrderItemVO> getOrderItems(Long id) {
        LeDuoMallOrder leDuoMallOrder = leDuoMallOrderMapper.selectByPrimaryKey(id);
        if (leDuoMallOrder != null) {
            List<LeDuoMallOrderItem> orderItems = leDuoMallOrderItemMapper.selectByOrderId(leDuoMallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<LeDuoMallOrderItemVO> leDuoMallOrderItemVOS = BeanUtil.copyList(orderItems, LeDuoMallOrderItemVO.class);
                return leDuoMallOrderItemVOS;
            }
        }
        return null;
    }
}