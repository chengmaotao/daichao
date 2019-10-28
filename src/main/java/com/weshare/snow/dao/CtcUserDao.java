package com.weshare.snow.dao;

import com.weshare.snow.model.CtcUser;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 11:18
 * @Description:
 */
public interface CtcUserDao {
    int insert(CtcUser ctcUser);

    CtcUser findUserByUserId(String userId);

    void updateData(CtcUser updataUser);

    CtcUser findUserByMobile(String mobile);
}
