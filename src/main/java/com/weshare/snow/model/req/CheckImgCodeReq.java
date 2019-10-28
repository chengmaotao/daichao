package com.weshare.snow.model.req;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 08:58
 * @Description:
 */
public class CheckImgCodeReq {
    private String mobile;
    private String code;

    private String channel;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "CheckImgCodeReq{" +
                "mobile='" + mobile + '\'' +
                ", code='" + code + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
