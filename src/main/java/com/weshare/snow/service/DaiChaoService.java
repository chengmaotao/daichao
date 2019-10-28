package com.weshare.snow.service;

import com.weshare.snow.model.CtcPrroduct;
import com.weshare.snow.model.CtcUser;
import com.weshare.snow.model.req.*;
import com.weshare.snow.model.rsp.IndexInfoResp;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 09:00
 * @Description:
 */
public interface DaiChaoService {
    void checkImgCode(CheckImgCodeReq req);

    void smsCode(String mobile, String channel,String verifyCode);

    CtcUser signIn(SignInReq req);

    void perfectUserInfo(PerfectUserInfoReq req);

    IndexInfoResp indexInfo(IndexInfoReq req);

    List<CtcPrroduct> getProductByCategoryId(ProductReq req);

    CtcPrroduct getProductById(ProductReq req);

    String recordUv(HttpServletRequest req, RecordUvReq recordUvReq);
}
