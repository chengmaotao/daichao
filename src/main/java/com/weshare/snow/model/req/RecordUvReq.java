package com.weshare.snow.model.req;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 14:49
 * @Description:
 */
public class RecordUvReq {
    private String userId;
    private String productId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    @Override
    public String toString() {
        return "RecordUvReq{" +
                "userId='" + userId + '\'' +
                ", productId='" + productId +
                '}';
    }
}
