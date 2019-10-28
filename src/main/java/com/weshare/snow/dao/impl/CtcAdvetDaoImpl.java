package com.weshare.snow.dao.impl;

import com.weshare.snow.dao.CtcAdvetDao;
import com.weshare.snow.mapper.CtcAdvetMapper;
import com.weshare.snow.model.CtcAdvet;
import com.weshare.snow.model.CtcAdvetExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 12:08
 * @Description:
 */
@Repository
public class CtcAdvetDaoImpl implements CtcAdvetDao {

    @Autowired
    private CtcAdvetMapper ctcAdvetMapper;

    @Override
    public List<CtcAdvet> getCtcAdvetList() {

        CtcAdvetExample example = new CtcAdvetExample();
        example.createCriteria().andDelFlagEqualTo("0");

        List<CtcAdvet> ctcAdvets = ctcAdvetMapper.selectByExample(example);
        return ctcAdvets;
    }
}
