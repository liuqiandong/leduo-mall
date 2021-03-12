package com.leduo.mall.entity;

import java.io.Serializable;

public class MiaoShaRedis implements Serializable {
    private Long goodsId;
    private int stockNum;

    public MiaoShaRedis() {
    }

    public MiaoShaRedis(Long goodsId, int stockNum) {
        this.goodsId = goodsId;
        this.stockNum = stockNum;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public int getStockNum() {
        return stockNum;
    }

    public void setStockNum(int stockNum) {
        this.stockNum = stockNum;
    }
}
