package com.weshare.snow.dao.impl;

import com.weshare.snow.dao.CtcStrikingDao;
import com.weshare.snow.mapper.CtcAdvetMapper;
import com.weshare.snow.mapper.CtcStrikingMapper;
import com.weshare.snow.model.CtcAdvet;
import com.weshare.snow.model.CtcAdvetExample;
import com.weshare.snow.model.CtcStriking;
import com.weshare.snow.model.CtcStrikingExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 12:14
 * @Description:
 */
@Repository
public class CtcStrikingDaoImpl implements CtcStrikingDao {

    @Autowired
    private CtcStrikingMapper ctcStrikingMapper;

    @Override
    public List<CtcStriking> getCtcStringList() {

        CtcStrikingExample example = new CtcStrikingExample();
        example.createCriteria().andDelFlagEqualTo("0");

        List<CtcStriking> ctcStrikings = ctcStrikingMapper.selectByExample(example);

        return ctcStrikings;
    }
}
