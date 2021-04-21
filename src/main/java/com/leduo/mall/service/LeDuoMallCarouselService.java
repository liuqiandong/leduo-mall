
package com.leduo.mall.service;

import com.leduo.mall.controller.vo.LeDuoMallIndexCarouselVO;
import com.leduo.mall.entity.Carousel;
import com.leduo.mall.util.PageQueryUtil;
import com.leduo.mall.util.PageResult;

import java.util.List;

public interface LeDuoMallCarouselService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getCarouselPage(PageQueryUtil pageUtil);

    String saveCarousel(Carousel carousel);

    String updateCarousel(Carousel carousel);

    Carousel getCarouselById(Integer id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 返回固定数量的轮播图对象(首页调用)
     *
     * @param number
     * @return
     */
    List<LeDuoMallIndexCarouselVO> getCarouselsForIndex(int number);
}
