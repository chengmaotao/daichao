package com.weshare.snow.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorCode {
    private static final Logger logger = LoggerFactory.getLogger(ErrorCode.class);

    public static final int SYS_SUCCESS = 0;
    public static final int SYS_FAIL = 1000;

    public static final int API_PARAM_ERROR=10001;
    public static final int API_IMGCODE_ERROR=10002;
    public static final int API_SMS_SEND_ERROR=10003;

    public static final int API_SMSCODE_ERROR=10004;

    public static final int API_USERSTATE_REALNAME=10005;
    public static final int API_USER_NO=10006;



}
