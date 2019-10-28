package com.weshare.snow.service.impl;

import com.weshare.sdk.whale.core.util.SecureCipherCoreHelper;
import com.weshare.sdk.whale.util.SdkUtil;
import com.weshare.snow.config.LogAnnotation;
import com.weshare.snow.config.UserStateEm;
import com.weshare.snow.dao.*;
import com.weshare.snow.exception.DaiChaoException;
import com.weshare.snow.exception.ParameterException;
import com.weshare.snow.model.*;
import com.weshare.snow.model.req.*;
import com.weshare.snow.model.rsp.IndexInfoResp;
import com.weshare.snow.model.rsp.SmsResp;
import com.weshare.snow.redis.access.RedisAccess;
import com.weshare.snow.redis.key.RedisUtils;
import com.weshare.snow.service.DaiChaoService;
import com.weshare.snow.util.ErrorCode;
import com.weshare.snow.util.Utility;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 09:01
 * @Description:
 */
@Service
public class DaiChaoServiceImpl implements DaiChaoService {

    private static final Logger logger = LoggerFactory.getLogger(DaiChaoServiceImpl.class);

    @Autowired
    private RedisAccess redisTemplate;


    @Value("${tian.sms.user.name}")
    private String smsUserName;

    @Value("${tian.sms.pwd}")
    private String smsPwd;

    @Value("${tian.sms.url}")
    private String smsUrl;

    @Value("${tian.sms.content}")
    private String smsContent;

    @Value("${tian.sms.valid.time}")
    private int codVaildTime;

    @Autowired
    private CtcUserDao ctcUserDao;

    @Autowired
    private CtcAdvetDao ctcAdvetDao;

    @Autowired
    private CtcStrikingDao ctcStrikingDao;

    @Autowired
    private CtcPrroductCategoryDao ctcPrroductCategoryDao;

    @Autowired
    private CtcPrroductDao ctcPrroductDao;

    @Autowired
    private CtcUvLogDao ctcUvLogDao;

    /**
     * 校验图片验证码
     *
     * @param req
     */
    @Override
    public void checkImgCode(CheckImgCodeReq req) {

        if (StringUtils.isEmpty(req.getMobile()) || StringUtils.isEmpty(req.getCode())) {
            logger.warn("checkImgCode req = {}", req);
            throw new ParameterException(ErrorCode.API_PARAM_ERROR);
        }

        String redisImgCode = (String) redisTemplate.getString(RedisUtils.getImgCodeKey(req.getMobile()));

        if (StringUtils.isEmpty(redisImgCode) || !StringUtils.equals(req.getCode().toUpperCase(), redisImgCode)) {
            logger.warn("redisImgCode = {}", redisImgCode);
            throw new DaiChaoException(ErrorCode.API_IMGCODE_ERROR);
        }

        // 验证通过以后  清除缓存
        redisTemplate.removeString(RedisUtils.getImgCodeKey(req.getMobile()));
    }

    /**
     * 发送短信验证码
     *
     * @param mobile
     * @param channel
     */
    @Override
    @LogAnnotation
    public void smsCode(String mobile, String channel, String verifyCode) {

        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(channel)) {
            logger.warn("smsCode mobile = {},channel = {}", mobile, channel);
            throw new ParameterException(ErrorCode.API_PARAM_ERROR);
        }

        // 短信验证码
        //String verifyCode = String.valueOf((int) (Math.random() * 9000 + 1000));

        // 5分钟内有效
        redisTemplate.setStringTime(RedisUtils.getSmsCodeKey(mobile, channel), verifyCode, codVaildTime, TimeUnit.MINUTES);

        RestTemplate restTemplate = new RestTemplate();
        //请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("clientid", smsUserName);
        requestParam.put("password", SecureCipherCoreHelper.getMd5Key(smsPwd));
        requestParam.put("mobile", mobile);
        requestParam.put("smstype", "4");
        requestParam.put("content", String.format(smsContent, verifyCode, codVaildTime));
        //封装成一个请求对象
        HttpEntity entity = new HttpEntity(requestParam, headers);

        SmsResp responseEntity = restTemplate.postForObject(smsUrl, entity, SmsResp.class);

        logger.info("smsCode responseEntity = {}", responseEntity);
        if (responseEntity == null || SdkUtil.isEmpty(responseEntity.getData()) || responseEntity.getData().get(0).getCode() != 0) {
            logger.error("验证码发送失败");
            throw new DaiChaoException(ErrorCode.API_SMS_SEND_ERROR);
        }

    }

    /**
     * 短信登录
     *
     * @param req
     * @return
     */
    @Override
    public CtcUser signIn(SignInReq req) {
        logger.debug("signIn req = {}", req);
        if (StringUtils.isEmpty(req.getMobile()) || StringUtils.isEmpty(req.getChannel()) || StringUtils.isEmpty(req.getSmsCode())) {
            throw new ParameterException(ErrorCode.API_PARAM_ERROR);
        }

        String redisSmsCode = (String) redisTemplate.getString(RedisUtils.getSmsCodeKey(req.getMobile(), req.getChannel()));

        if (StringUtils.isEmpty(redisSmsCode) || !StringUtils.equals(redisSmsCode, req.getSmsCode())) {
            logger.warn("redisSmsCode = {}", redisSmsCode);
            throw new DaiChaoException(ErrorCode.API_SMSCODE_ERROR);
        }

        CtcUser userByUserId = ctcUserDao.findUserByMobile(req.getMobile());

        if(userByUserId == null){
            CtcUser ctcUser = new CtcUser();
            String uuid = Utility.get32UUID();
            ctcUser.setUserId(uuid);
            ctcUser.setUserPhone(req.getMobile());
            ctcUser.setChannelId(req.getChannel());
            ctcUser.setUserState(UserStateEm.REGISTER0.getCode());
            ctcUserDao.insert(ctcUser);
            return ctcUser;
        }else{
            return userByUserId;
        }
    }

    /**
     * 完善用户信息
     *
     * @param req
     */
    @Override
    public void perfectUserInfo(PerfectUserInfoReq req) {
        logger.debug("perfectUserInfo req = {}", req);

        if (StringUtils.isEmpty(req.getUserId()) || StringUtils.isEmpty(req.getUserName()) || StringUtils.isEmpty(req.getUserNo())) {
            throw new ParameterException(ErrorCode.API_PARAM_ERROR);
        }

        if (!Utility.isIDNumber(req.getUserNo())) {
            throw new ParameterException(ErrorCode.API_USER_NO);
        }

        CtcUser ctcUser = ctcUserDao.findUserByUserId(req.getUserId());
        if (ctcUser == null) {
            logger.warn("ctcuser is null userId = {}", req.getUserId());
            throw new ParameterException(ErrorCode.SYS_FAIL);
        }

        CtcUser updateRecord = new CtcUser();
        updateRecord.setUserId(ctcUser.getUserId());
        updateRecord.setUserName(req.getUserName());
        updateRecord.setUserSfz(req.getUserNo());
        updateRecord.setUserState(UserStateEm.REALNAME1.getCode());
        updateRecord.setUserRealNameTime(new Date());

        ctcUserDao.updateData(updateRecord);
    }

    @Override
    public IndexInfoResp indexInfo(IndexInfoReq req) {
        logger.debug("indexInfo req = {}", req);

        if (StringUtils.isEmpty(req.getUserId())) {
            throw new ParameterException(ErrorCode.API_PARAM_ERROR);
        }

        CtcUser ctcUser = ctcUserDao.findUserByUserId(req.getUserId());
        if (ctcUser == null) {
            logger.warn("ctcuser is null userId = {}", req.getUserId());
            throw new ParameterException(ErrorCode.SYS_FAIL);
        }

        //
        if (!StringUtils.equals(ctcUser.getUserState(), UserStateEm.REALNAME1.getCode())) {
            logger.warn("ctcuser is not realName userId = {}", req.getUserId());
            throw new DaiChaoException(ErrorCode.API_USERSTATE_REALNAME);
        }


        IndexInfoResp resp = new IndexInfoResp();
        // 获取广告轮播去
        List<CtcAdvet> ctcAdvetList = ctcAdvetDao.getCtcAdvetList();
        resp.setCtcadvets(ctcAdvetList);

        // 获取走马灯
        List<CtcStriking> ctcStringList = ctcStrikingDao.getCtcStringList();
        resp.setCtcStrikings(ctcStringList);

        // 获取产品分类
        List<CtcPrroductCategory> ctcPrroductCategoryList = ctcPrroductCategoryDao.getCtcPrroductCategoryList();
        resp.setCtcPrroductCategorys(ctcPrroductCategoryList);


        // 获取新产品
        List<CtcPrroduct> newProductList = ctcPrroductDao.getNewProductList();
        resp.setNewProducts(newProductList);

        // 获取所有产品
        List<CtcPrroduct> allProductList = ctcPrroductDao.getAllProductList();
        resp.setProducts(allProductList);

        return resp;
    }

    /**
     * 根据产品分类Id 获取产品列表
     *
     * @param req
     * @return
     */
    @Override
    public List<CtcPrroduct> getProductByCategoryId(ProductReq req) {

        logger.debug("getProductByCategoryId req = {}", req);

        if (StringUtils.isEmpty(req.getUserId()) || StringUtils.isEmpty(req.getProductCategoryId())) {
            throw new ParameterException(ErrorCode.API_PARAM_ERROR);
        }

        CtcUser ctcUser = ctcUserDao.findUserByUserId(req.getUserId());
        if (ctcUser == null) {
            logger.warn("ctcuser is null userId = {}", req.getUserId());
            throw new ParameterException(ErrorCode.SYS_FAIL);
        }

        //
        if (!StringUtils.equals(ctcUser.getUserState(), UserStateEm.REALNAME1.getCode())) {
            logger.warn("ctcuser is not realName userId = {}", req.getUserId());
            throw new DaiChaoException(ErrorCode.API_USERSTATE_REALNAME);
        }

        List<CtcPrroduct> productList = ctcPrroductDao.getProductListByCategoryId(req.getProductCategoryId());

        return productList;
    }

    /**
     * @param req
     * @return
     */
    @Override
    public CtcPrroduct getProductById(ProductReq req) {
        logger.debug("getProductByCategoryId req = {}", req);

        if (StringUtils.isEmpty(req.getUserId()) || StringUtils.isEmpty(req.getProductId())) {
            throw new ParameterException(ErrorCode.API_PARAM_ERROR);
        }

        CtcUser ctcUser = ctcUserDao.findUserByUserId(req.getUserId());
        if (ctcUser == null) {
            logger.warn("ctcuser is null userId = {}", req.getUserId());
            throw new ParameterException(ErrorCode.SYS_FAIL);
        }

        //
        if (!StringUtils.equals(ctcUser.getUserState(), UserStateEm.REALNAME1.getCode())) {
            logger.warn("ctcuser is not realName userId = {}", req.getUserId());
            throw new DaiChaoException(ErrorCode.API_USERSTATE_REALNAME);
        }

        CtcPrroduct ctcPrroduct = ctcPrroductDao.getProductById(req.getProductId());
        return ctcPrroduct;
    }

    /**
     * @param req
     * @param recordUvReq
     */
    @Override
    public String recordUv(HttpServletRequest req, RecordUvReq recordUvReq) {

        logger.info("getProductByCategoryId req = {}", recordUvReq);

        if (StringUtils.isEmpty(recordUvReq.getUserId()) || StringUtils.isEmpty(recordUvReq.getProductId())) {
            throw new ParameterException(ErrorCode.API_PARAM_ERROR);
        }

        CtcUser ctcUser = ctcUserDao.findUserByUserId(recordUvReq.getUserId());
        if (ctcUser == null) {
            logger.warn("ctcuser is null userId = {}", recordUvReq.getUserId());
            throw new ParameterException(ErrorCode.SYS_FAIL);
        }

        //
        if (!StringUtils.equals(ctcUser.getUserState(), UserStateEm.REALNAME1.getCode())) {
            logger.warn("ctcuser is not realName userId = {}", recordUvReq.getUserId());
            throw new DaiChaoException(ErrorCode.API_USERSTATE_REALNAME);
        }

        CtcPrroduct product = ctcPrroductDao.getProductById(recordUvReq.getProductId());
        if(product == null){
            logger.warn("product is null productId = {}", recordUvReq.getProductId());
            throw new ParameterException(ErrorCode.API_PARAM_ERROR);
        }

        // 统计uv
        String ipAddr = Utility.getIpAddr(req);

        CtcUvLog record = new CtcUvLog();
        record.setProductid(product.getProductId());
        record.setMobile(ctcUser.getUserPhone());
        record.setIp(ipAddr);
        ctcUvLogDao.insert(record);

        return product.getJumpUrl();
    }
}
