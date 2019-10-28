package com.weshare.snow.dao.impl;

import com.weshare.snow.dao.CtcSmsLogDao;
import com.weshare.snow.mapper.CtcSmsLogMapper;
import com.weshare.snow.model.CtcSmsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 10:49
 * @Description:
 */
@Repository
public class CtcSmsLogDaoImpl implements CtcSmsLogDao {

    @Autowired
    private CtcSmsLogMapper ctcSmsLogMapper;

    @Override
    public int insert(CtcSmsLog smsLog) {
        return ctcSmsLogMapper.insertSelective(smsLog);
    }
}
