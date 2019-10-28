package com.weshare.snow.exception;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 09:16
 * @Description:
 */
public class DaiChaoException extends RuntimeException {

    private int errorCode;
    private String errorMessage;

    public DaiChaoException() {
        super();
    }

    public DaiChaoException(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
