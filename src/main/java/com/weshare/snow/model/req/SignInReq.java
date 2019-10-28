package com.weshare.snow.model.req;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 11:07
 * @Description:
 */
public class SignInReq {
    private String mobile;
    private String smsCode;
    private String channel;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "signInReq{" +
                "mobile='" + mobile + '\'' +
                ", smsCode='" + smsCode + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
