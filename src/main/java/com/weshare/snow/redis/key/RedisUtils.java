package com.weshare.snow.redis.key;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 09:38
 * @Description:
 */
public class RedisUtils {

    private static final String PREX_ = "CTC:";

    public static String getImgCodeKey(String mobile) {
        return new StringBuffer("").append(PREX_).append("IMGCODE:").append(mobile).toString();
    }

    public static String getSmsCodeKey(String mobile,String channel) {
        return new StringBuffer("").append(PREX_).append("SMSCODE:").append(mobile).append(":").append(channel).toString();
    }
}
