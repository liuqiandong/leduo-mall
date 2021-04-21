
package com.leduo.mall.controller.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单列表页面VO
 */
public class LeDuoMallOrderListVO implements Serializable {

    private Long orderId;

    private String orderNo;

    private Integer totalPrice;

    private Byte payType;

    private Byte orderStatus;

    private String orderStatusString;

    private String userAddress;

    private Date createTime;

    public List<LeDuoMallOrderItemVO> leDuoMallOrderItemVOS;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusString() {
        return orderStatusString;
    }

    public void setOrderStatusString(String orderStatusString) {
        this.orderStatusString = orderStatusString;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<LeDuoMallOrderItemVO> getNewBeeMallOrderItemVOS() {
        return leDuoMallOrderItemVOS;
    }

    public void setNewBeeMallOrderItemVOS(List<LeDuoMallOrderItemVO> leDuoMallOrderItemVOS) {
        this.leDuoMallOrderItemVOS = leDuoMallOrderItemVOS;
    }
}
