package com.weshare.snow.controller;

import com.weshare.snow.redis.access.RedisAccess;
import com.weshare.snow.redis.key.RedisUtils;
import com.weshare.snow.util.ImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: CTC
 * @Date: 2019/10/23 22:34
 * @Description:
 */

@Controller
@RequestMapping(value = "/ctc")
public class ImageCodeController {

    private static final Logger logger = LoggerFactory.getLogger(ImageCodeController.class);

    @Autowired
    private RedisAccess redisTemplate;

    @RequestMapping(value = "/img/code")
    public void getCode(HttpServletRequest request, HttpServletResponse response,String mobile,String timestamp ) throws Exception {

        if(StringUtils.isEmpty(mobile)){
            throw new Exception("手机号为空");
        }

        response.setContentType("image/jpeg");
        //禁止图像缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //HttpSession session = request.getSession();
        ImageUtil imageUtil = new ImageUtil(120, 40, 4, 30);

        // 图片验证码
        String code = imageUtil.getCode();

        String imgCodeKey = RedisUtils.getImgCodeKey(mobile);

        logger.info("imgcode key =  {}, value = {}",imgCodeKey,code);

        redisTemplate.setStringTime(imgCodeKey,code,60,TimeUnit.MINUTES);


        String redisImgCode = (String) redisTemplate.getString(imgCodeKey);
        logger.info("redisImgCode key =  {}, value = {}",imgCodeKey,redisImgCode);


        imageUtil.write(response.getOutputStream());
    }
}
