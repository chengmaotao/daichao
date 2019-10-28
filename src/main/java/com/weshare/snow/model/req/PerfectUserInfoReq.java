package com.weshare.snow.model.req;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 11:38
 * @Description:
 */
public class PerfectUserInfoReq {
    private String userId;
    private String userName;
    private String userNo; // 身份证号

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    @Override
    public String toString() {
        return "PerfectUserInfoReq{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userNo='" + userNo + '\'' +
                '}';
    }
}
