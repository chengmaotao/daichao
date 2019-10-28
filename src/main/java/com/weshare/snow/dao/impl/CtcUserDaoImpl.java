package com.weshare.snow.dao.impl;

import com.weshare.snow.dao.CtcUserDao;
import com.weshare.snow.mapper.CtcUserMapper;
import com.weshare.snow.model.CtcUser;
import com.weshare.snow.model.CtcUserExample;
import com.weshare.snow.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 11:18
 * @Description:
 */
@Repository
public class CtcUserDaoImpl implements CtcUserDao {

    @Autowired
    private CtcUserMapper ctcUserMapper;

    @Override
    public int insert(CtcUser ctcUser) {

        ctcUser.setCreateBy("1");
        ctcUser.setUpdateBy("1");
        ctcUser.setCreateDate(new Date());
        ctcUser.setUpdateDate(ctcUser.getCreateDate());
        ctcUser.setDelFlag("0");
        ctcUser.setId(Utility.generateUUID());

        return ctcUserMapper.insertSelective(ctcUser);
    }

    @Override
    public CtcUser findUserByUserId(String userId) {
        CtcUserExample ctcUserExample = new CtcUserExample();
        ctcUserExample.createCriteria().andUserIdEqualTo(userId).andDelFlagEqualTo("0");

        List<CtcUser> ctcUsers = ctcUserMapper.selectByExample(ctcUserExample);

        if (ctcUsers != null && ctcUsers.size() > 0) {
            return ctcUsers.get(0);
        }
        return null;
    }

    @Override
    public void updateData(CtcUser updataRecord) {
        updataRecord.setUpdateDate(new Date());

        CtcUserExample example = new CtcUserExample();
        example.createCriteria().andUserIdEqualTo(updataRecord.getUserId());

        ctcUserMapper.updateByExampleSelective(updataRecord,example);
    }

    @Override
    public CtcUser findUserByMobile(String mobile) {
        CtcUserExample ctcUserExample = new CtcUserExample();
        ctcUserExample.createCriteria().andUserPhoneEqualTo(mobile).andDelFlagEqualTo("0");

        List<CtcUser> ctcUsers = ctcUserMapper.selectByExample(ctcUserExample);

        if (ctcUsers != null && ctcUsers.size() > 0) {
            return ctcUsers.get(0);
        }
        return null;
    }
}
