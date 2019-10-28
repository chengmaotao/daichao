package com.weshare.snow.dao;

import com.weshare.snow.model.CtcPrroduct;

import java.util.List;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 12:26
 * @Description:
 */
public interface CtcPrroductDao {
    List<CtcPrroduct> getNewProductList();
    List<CtcPrroduct> getAllProductList();
    List<CtcPrroduct> getProductListByCategoryId(String categoryId);

    CtcPrroduct getProductById(String productId);
}
