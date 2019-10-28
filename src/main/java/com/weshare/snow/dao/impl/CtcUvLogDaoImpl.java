package com.weshare.snow.dao.impl;

import com.weshare.snow.dao.CtcUvLogDao;
import com.weshare.snow.mapper.CtcUvLogMapper;
import com.weshare.snow.model.CtcUvLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 15:00
 * @Description:
 */
@Repository
public class CtcUvLogDaoImpl implements CtcUvLogDao {

    @Autowired
    private CtcUvLogMapper ctcUvLogMapper;

    @Override
    public int insert(CtcUvLog record) {

        record.setCreateTime(new Date());
        return ctcUvLogMapper.insertSelective(record);

    }
}
