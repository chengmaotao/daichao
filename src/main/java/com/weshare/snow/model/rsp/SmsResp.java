package com.weshare.snow.model.rsp;

import java.util.List;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 09:48
 * @Description:
 */
public class SmsResp {
    private int total_fee;

    private List<RecordSmsResp> data;

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public List<RecordSmsResp> getData() {
        return data;
    }

    public void setData(List<RecordSmsResp> data) {
        this.data = data;
    }

    public static class RecordSmsResp {
        private int code;
        private int fee;

        private String mobile;
        private String msg;
        private String sid;
        private String uid;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        @Override
        public String toString() {
            return "RecordSmsResp{" +
                    "code=" + code +
                    ", fee=" + fee +
                    ", mobile='" + mobile + '\'' +
                    ", msg='" + msg + '\'' +
                    ", sid='" + sid + '\'' +
                    ", uid='" + uid + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RecordTianJiResp{" +
                "total_fee=" + total_fee +
                ", data=" + data +
                '}';
    }
}
