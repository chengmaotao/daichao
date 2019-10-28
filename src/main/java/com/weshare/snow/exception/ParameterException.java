package com.weshare.snow.exception;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 09:09
 * @Description:
 */
public class ParameterException extends RuntimeException {
    private int errorCode;
    private String errorMessage;

    public ParameterException() {
        super();
    }

    public ParameterException(int errorCode) {
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
