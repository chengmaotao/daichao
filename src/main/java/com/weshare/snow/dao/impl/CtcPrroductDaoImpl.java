package com.weshare.snow.dao.impl;

import com.weshare.snow.dao.CtcPrroductDao;
import com.weshare.snow.mapper.CtcPrroductMapper;
import com.weshare.snow.model.CtcPrroduct;
import com.weshare.snow.model.CtcPrroductExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 12:28
 * @Description:
 */
@Repository
public class CtcPrroductDaoImpl implements CtcPrroductDao {

    @Autowired
    private CtcPrroductMapper ctcPrroductMapper;

    @Override
    public List<CtcPrroduct> getNewProductList() {

        CtcPrroductExample example = new CtcPrroductExample();
        example.createCriteria().andNewProductEqualTo("0").andDelFlagEqualTo("0");
        List<CtcPrroduct> ctcPrroducts = ctcPrroductMapper.selectByExample(example);
        return ctcPrroducts;
    }

    @Override
    public List<CtcPrroduct> getAllProductList() {

        CtcPrroductExample example = new CtcPrroductExample();
        example.createCriteria().andDelFlagEqualTo("0");
        List<CtcPrroduct> ctcPrroducts = ctcPrroductMapper.selectByExample(example);
        return ctcPrroducts;
    }

    @Override
    public List<CtcPrroduct> getProductListByCategoryId(String categoryId) {

        CtcPrroductExample example = new CtcPrroductExample();
        example.createCriteria().andDelFlagEqualTo("0").andProductCategoryIdEqualTo(categoryId);
        List<CtcPrroduct> ctcPrroducts = ctcPrroductMapper.selectByExample(example);
        return ctcPrroducts;

    }

    @Override
    public CtcPrroduct getProductById(String productId) {
        CtcPrroductExample example = new CtcPrroductExample();
        example.createCriteria().andDelFlagEqualTo("0").andProductIdEqualTo(productId);
        List<CtcPrroduct> ctcPrroducts = ctcPrroductMapper.selectByExample(example);
        if (ctcPrroducts != null && ctcPrroducts.size() > 0) {
            return ctcPrroducts.get(0);
        }
        return null;
    }
}
