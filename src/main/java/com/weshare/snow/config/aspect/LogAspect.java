package com.weshare.snow.config.aspect;

import com.weshare.snow.dao.CtcSmsLogDao;
import com.weshare.snow.model.CtcSmsLog;
import com.weshare.snow.util.Utility;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 10:06
 * @Description:
 */
@Aspect //该注解标示该类为切面类
@Component //注入依赖
public class LogAspect {

    @Autowired
    private CtcSmsLogDao ctcSmsLogDao;

    @Pointcut("@annotation(com.weshare.snow.config.LogAnnotation)")
    public void smsLog() {

    }

    @After("smsLog()")
    public void doAfter(JoinPoint jp) {

        CtcSmsLog smsLog = new CtcSmsLog();
        Object[] parames = jp.getArgs();//获取目标方法体参数
        for (int i = 0; i < parames.length; i++) {
            System.out.println("nnnnnnnnnnnnnnnnnnnnnnnnnnnn:" + parames[i]);
        }
        smsLog.setMobile(String.valueOf(parames[0]));
        smsLog.setChannel(String.valueOf(parames[1]));
        smsLog.setCode(String.valueOf(parames[2]));
        smsLog.setCreateTime(Utility.getCurrentTimeStamp());
        ctcSmsLogDao.insert(smsLog);
    }
}
