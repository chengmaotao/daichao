package com.weshare.snow.dao.impl;

import com.weshare.snow.dao.CtcPrroductCategoryDao;
import com.weshare.snow.mapper.CtcPrroductCategoryMapper;
import com.weshare.snow.model.CtcPrroductCategory;
import com.weshare.snow.model.CtcPrroductCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 12:19
 * @Description:
 */
@Repository
public class CtcPrroductCategoryDaoImpl implements CtcPrroductCategoryDao {

    @Autowired
    private CtcPrroductCategoryMapper ctcPrroductCategoryMapper;

    @Override
    public List<CtcPrroductCategory> getCtcPrroductCategoryList() {
        CtcPrroductCategoryExample example = new CtcPrroductCategoryExample();
        example.createCriteria().andDelFlagEqualTo("0");
        List<CtcPrroductCategory> ctcPrroductCategories = ctcPrroductCategoryMapper.selectByExample(example);
        return ctcPrroductCategories;
    }
}
