package com.weshare.snow.model.req;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 12:50
 * @Description:
 */
public class ProductReq {
    private String userId;

    private String productCategoryId;

    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    @Override
    public String toString() {
        return "ProductReq{" +
                "userId='" + userId + '\'' +
                ", productCategoryId='" + productCategoryId + '\'' +
                '}';
    }
}
