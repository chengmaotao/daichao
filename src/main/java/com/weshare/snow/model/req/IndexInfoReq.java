package com.weshare.snow.model.req;

import com.weshare.snow.model.CtcAdvet;

import java.util.List;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 11:59
 * @Description:
 */
public class IndexInfoReq {
    private String userId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "IndexInfoReq{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
