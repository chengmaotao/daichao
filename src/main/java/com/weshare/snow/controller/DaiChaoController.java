package com.weshare.snow.controller;

import com.weshare.sdk.whale.model.LightningResponse;
import com.weshare.snow.exception.DaiChaoException;
import com.weshare.snow.exception.ParameterException;
import com.weshare.snow.model.CtcPrroduct;
import com.weshare.snow.model.CtcUser;
import com.weshare.snow.model.req.*;
import com.weshare.snow.model.rsp.IndexInfoResp;
import com.weshare.snow.service.DaiChaoService;
import com.weshare.snow.util.ErrorCode;
import com.weshare.snow.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 08:54
 * @Description:
 */
@RestController
@RequestMapping(value = "/ctc")
public class DaiChaoController {

    private static final Logger logger = LoggerFactory.getLogger(DaiChaoController.class);

    @Autowired
    private ReloadableResourceBundleMessageSource messageSource;

    @Autowired
    private DaiChaoService daiChaoService;

    /**
     * 校验图片验证码, 短信发送
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/checkImgCode", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public LightningResponse checkImgCode(@RequestBody CheckImgCodeReq req) {
        logger.debug("checkImgCode req = {}", req);
        LightningResponse response = null;

        try {
            daiChaoService.checkImgCode(req);
            logger.info("check img code sucess");

            String verifyCode = String.valueOf((int) (Math.random() * 9000 + 1000));
            daiChaoService.smsCode(req.getMobile(), req.getChannel(), verifyCode);

            response = Utility.getRightResponse("success", null);
        } catch (ParameterException e) {
            logger.warn("checkImgCode: ParameterException:", e);
            response = Utility.getErrorResponse(e.getErrorCode(), messageSource);
        } catch (DaiChaoException e) {
            logger.warn("checkImgCode: DaiChaoException:", e);
            response = Utility.getErrorResponse(e.getErrorCode(), messageSource);
        } catch (Exception e) {
            logger.error("checkImgCode: Exception:", e);
            response = Utility.getErrorResponse(ErrorCode.SYS_FAIL, messageSource);
        }
        return response;
    }


    /**
     * 短信登录
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/signIn", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public LightningResponse signIn(@RequestBody SignInReq req) {
        logger.debug("signIn req = {}", req);
        LightningResponse response = null;

        try {

            CtcUser user = daiChaoService.signIn(req);
            response = Utility.getRightResponse("success", user);
        } catch (ParameterException e) {
            logger.warn("signIn: ParameterException:", e);
            response = Utility.getErrorResponse(e.getErrorCode(), messageSource);
        } catch (DaiChaoException e) {
            logger.warn("signIn: DaiChaoException:", e);
            response = Utility.getErrorResponse(e.getErrorCode(), messageSource);
        } catch (Exception e) {
            logger.error("signIn: Exception:", e);
            response = Utility.getErrorResponse(ErrorCode.SYS_FAIL, messageSource);
        }
        return response;
    }

    /**
     * 完善用户信息（实名/身份证号）
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/perfectUserInfo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public LightningResponse perfectUserInfo(@RequestBody PerfectUserInfoReq req) {
        logger.debug("perfectUserInfo req = {}", req);
        LightningResponse response = null;

        try {
            daiChaoService.perfectUserInfo(req);
            response = Utility.getRightResponse("success", null);
        } catch (ParameterException e) {
            logger.warn("perfectUserInfo: ParameterException:", e);
            response = Utility.getErrorResponse(e.getErrorCode(), messageSource);
        } catch (DaiChaoException e) {
            logger.warn("perfectUserInfo: DaiChaoException:", e);
            response = Utility.getErrorResponse(e.getErrorCode(), messageSource);
        } catch (Exception e) {
            logger.error("perfectUserInfo: Exception:", e);
            response = Utility.getErrorResponse(ErrorCode.SYS_FAIL, messageSource);
        }
        return response;
    }

    /**
     * 首页信息
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/indexInfo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public LightningResponse indexInfo(@RequestBody IndexInfoReq req) {
        logger.debug("indexInfo req = {}", req);
        LightningResponse response = null;

        try {
            IndexInfoResp indexInfoResp = daiChaoService.indexInfo(req);
            response = Utility.getRightResponse("success", indexInfoResp);
        } catch (ParameterException e) {
            logger.warn("indexInfo: ParameterException:", e);
            response = Utility.getErrorResponse(e.getErrorCode(), messageSource);
        } catch (DaiChaoException e) {
            logger.warn("indexInfo: DaiChaoException:", e);
            response = Utility.getErrorResponse(e.getErrorCode(), messageSource);
        } catch (Exception e) {
            logger.error("indexInfo: Exception:", e);
            response = Utility.getErrorResponse(ErrorCode.SYS_FAIL, messageSource);
        }
        return response;
    }

    /**
     * 根据产品分类获取产品列表
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/getProductByCategoryId", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public LightningResponse getProductByCategoryId(@RequestBody ProductReq req) {
        logger.debug("getProductByCategoryId req = {}", req);
        LightningResponse response = null;

        try {
            List<CtcPrroduct> products = daiChaoService.getProductByCategoryId(req);
            response = Utility.getRightResponse("success", products);
        } catch (ParameterException e) {
            logger.warn("getProductByCategoryId: ParameterException:", e);
            response = Utility.getErrorResponse(e.getErrorCode(), messageSource);
        } catch (DaiChaoException e) {
            logger.warn("getProductByCategoryId: DaiChaoException:", e);
            response = Utility.getErrorResponse(e.getErrorCode(), messageSource);
        } catch (Exception e) {
            logger.error("getProductByCategoryId: Exception:", e);
            response = Utility.getErrorResponse(ErrorCode.SYS_FAIL, messageSource);
        }
        return response;
    }

    /**
     * 根据产品Id 获取产品详情
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/getProductById", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public LightningResponse getProductById(@RequestBody ProductReq req) {
        logger.debug("getProductById req = {}", req);
        LightningResponse response = null;

        try {
            CtcPrroduct product = daiChaoService.getProductById(req);
            response = Utility.getRightResponse("success", product);
        } catch (ParameterException e) {
            logger.warn("getProductById: ParameterException:", e);
            response = Utility.getErrorResponse(e.getErrorCode(), messageSource);
        } catch (DaiChaoException e) {
            logger.warn("getProductById: DaiChaoException:", e);
            response = Utility.getErrorResponse(e.getErrorCode(), messageSource);
        } catch (Exception e) {
            logger.error("getProductById: Exception:", e);
            response = Utility.getErrorResponse(ErrorCode.SYS_FAIL, messageSource);
        }
        return response;
    }

    /**
     * 统计uv
     * @param req
     * @param recordUvReq
     * @return
     */
    @RequestMapping(value = "/recordUv", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public LightningResponse recordUv(HttpServletRequest req, @RequestBody RecordUvReq recordUvReq) {
        logger.info("recordUv req = {}", recordUvReq);
        LightningResponse response = null;

        try {
            String url = daiChaoService.recordUv(req, recordUvReq);
            response = Utility.getRightResponse("success", url);
        } catch (ParameterException e) {
            logger.warn("recordUv: ParameterException:", e);
            response = Utility.getErrorResponse(e.getErrorCode(), messageSource);
        } catch (DaiChaoException e) {
            logger.warn("recordUv: DaiChaoException:", e);
            response = Utility.getErrorResponse(e.getErrorCode(), messageSource);
        } catch (Exception e) {
            logger.error("recordUv: Exception:", e);
            response = Utility.getErrorResponse(ErrorCode.SYS_FAIL, messageSource);
        }
        return response;
    }
}
