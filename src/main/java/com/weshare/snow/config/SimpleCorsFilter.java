package com.weshare.snow.config;

import com.weshare.sdk.whale.config.ds.BizAndDsHolder;
import com.weshare.sdk.whale.config.ds.DsRouterConfig;
import com.weshare.sdk.whale.util.SdkUtil;
import com.weshare.snow.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lishaoyan on 2015/5/19.
 */
@Component
public class SimpleCorsFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(SimpleCorsFilter.class);
    private static final String CORS_FORBIDDEN_INFO = "cors_forbidden_info";
    private final static String DEFAULT_ORIGIN = "*";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${cors_access.max_age}")
    private String maxAge;

    @Value("${cors_access.allow_credentials}")
    private String allowCredentials;

    @Value("${cors_access.allow_method}")
    private String allowMethod;

    @Value("${cors_access.allow_origins}")
    private String[] allowOrigins;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (Utility.isBlank(maxAge)) {
            maxAge = "0";
        }
        if (Utility.isBlank(allowCredentials)) {
            allowCredentials = "true";
        }
        if (Utility.isBlank(allowMethod)) {
            allowMethod = "POST,GET,OPTIONS";
        }
        logger.debug("corsFilter-init: maxAge='{}',allowCredentials='{}',allowMethod='{}',allowOrigins='{}' ", //
                maxAge, allowCredentials, allowMethod, allowOrigins);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rsp, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rsp;
        try {
            final String origin = request.getHeader("Origin");
//            String acReqMethod = request.getHeader("Access-Control-Request-Method");
//            logger.debug("doFilter reqmethod: {}, origin:{}, accesscontrolRequestMethod:{}",
//                    ((HttpServletRequest) req).getMethod(),origin, acReqMethod);

            if (origin == null) {
                return;  // access
            }

            if (allowOrigins == null || allowOrigins.length == 0) { // default allow

                response.setHeader("Access-Control-Allow-Origin", DEFAULT_ORIGIN);
                response.setHeader("Access-Control-Allow-Methods", allowMethod);
                response.setHeader("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept,X-DEV-ID,X-WeshareAuth-Token");
                response.setHeader("Access-Control-Allow-Credentials", allowCredentials);
                response.setHeader("Access-Control-Max-Age", maxAge);

            } else {
                boolean bAllow = false;

                for (String tmp : allowOrigins) {
                    if (Utility.isBlank(tmp)) {
                        continue;
                    }
                    if (equalsAddress(origin, tmp)) {
                        bAllow = true;
                        break;
                    }
                }

                if (bAllow) {
                    if (request.getMethod().equals("OPTIONS") &&
                            !Utility.isEmpty(request.getHeader("Access-Control-Request-Method"))) {

                        //logger.debug("doFilter templog: OPTIONS: {}", origin);

                        response.setHeader("Access-Control-Allow-Origin", origin);
                        response.setHeader("Access-Control-Allow-Methods", allowMethod);
                        response.setHeader("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept,X-DEV-ID,X-WeshareAuth-Token");
                        response.setHeader("Access-Control-Allow-Credentials", allowCredentials);
                        response.setHeader("Access-Control-Max-Age", maxAge);

                    } else {

                        //logger.debug("doFilter templog: NOT OPTIONS: {}", origin);

                        response.setHeader("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept,X-DEV-ID,X-WeshareAuth-Token");
                        response.setHeader("Access-Control-Allow-Credentials", allowCredentials);
                        response.setHeader("Access-Control-Allow-Origin", origin);
                    }

                } else {
                    String uri = request.getServletPath().concat("_").concat(origin);
                    logger.debug("SimpleCorsFilter: DO NOT ALLOW: origin: {}, uri: {}", origin, uri);
                    saveForbinddenInfo(uri);
                }

            }
        } finally {
            // save invalid extend args
            this.saveInvalidExtArgs(request);
            //
            chain.doFilter(request, response);
        }
    }

    public static boolean equalsAddress(String address, String regex) {
        regex = regex.replace(".", "\\.");
        regex = regex.replace("*", "(.*)");
        Pattern pattern = Pattern.compile("^" + regex + "$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(address);
        return matcher.find();
    }

    private void saveForbinddenInfo(String uri) {
        if (Utility.isEmpty(uri)) {
            logger.warn("saveForbinddenInfo: uri is empty");
        }

        try {
            final String cacheKey = Utility.getCacheKey(CORS_FORBIDDEN_INFO) + BizAndDsHolder.getBizt();
            HashOperations<String, String, Integer> hashOperations = redisTemplate.opsForHash();
            long latestCount = hashOperations.increment(cacheKey, uri, 1);
            logger.debug("saveForbinddenInfo: uri is: {}, latestCount is: {}", uri, latestCount);
            // 2day
            redisTemplate.expire(cacheKey, SdkUtil.getDays23HourTs(2) - SdkUtil.getCurrTimestamp(), TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("pushUserLoanInfoToRedis: catch e.msg={}", e.getMessage());
        }
    }

    /**
     * save invalid extend args
     *
     * @param request
     */
    private void saveInvalidExtArgs(HttpServletRequest request) {
        final long bt = System.currentTimeMillis();
        try {
            final HashOperations<String, String, Integer> hashOpt = redisTemplate.opsForHash();
            final String cacheKey = Utility.getCacheKey("invalid_ext_args_list");
            final String uri = request.getServletPath();
            // biz type
            final String bizType = request.getParameter("b");
            if (!SdkUtil.isInt(bizType)) {
                hashOpt.increment(cacheKey, uri + "_b", 1);
                this.setInvalidEaExpire(cacheKey);
            } else {
                Map<String, String> tgBiztMap = DsRouterConfig.getTgBiztMap();
                if (tgBiztMap != null && tgBiztMap.size() > 0 && !tgBiztMap.containsKey(bizType)) {
                    hashOpt.increment(cacheKey, uri + "_b", 1);
                    this.setInvalidEaExpire(cacheKey);
                }
            }
            // channel
            final String channel = request.getParameter("c");
            if (!SdkUtil.isInt(channel)) {
                hashOpt.increment(cacheKey, uri + "_c", 1);
                this.setInvalidEaExpire(cacheKey);
            }
        } catch (Exception e) {
            logger.error("saveInvalidExtArgs: catch e.msg={}", e.getMessage());
        } finally {
            logger.debug("saveInvalidExtArgs: end,cost time={} ms.", (System.currentTimeMillis() - bt));
        }
    }

    private void setInvalidEaExpire(final String cacheKey) {
        Long expire = redisTemplate.getExpire(cacheKey);
        if (expire != null && expire < 1) {
            redisTemplate.expire(cacheKey, SdkUtil.getDays23HourTs(1) - SdkUtil.getCurrTimestamp(), TimeUnit.SECONDS);
        }
    }
}


