package com.weshare.snow.controller;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by lijie on 2019/3/15.
 * <p>
 * query the version and build information of webapp. It should be filled automatically by the build
 * script with information, such as build time, git revision, build host, etc.
 */
@Api(value = "appinfo", description = "系统检测接口", hidden = true)
@RestController
public class AppInfoController {

    private static final Logger logger = LoggerFactory.getLogger(AppInfoController.class);
    private Properties buildInfo = null;
    private static String hostname = "unknown";

    static {
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public AppInfoController() {
        try {
            buildInfo = PropertiesLoaderUtils.loadAllProperties("properties/buildinfo.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/appver")
    public HashMap<String, String> appVersion(HttpServletRequest request) {
        HashMap<String, String> appver = new HashMap<String, String>(10);
        if (buildInfo != null) {
            for (Object key : buildInfo.keySet()) {
                appver.put(key.toString(), buildInfo.getProperty(key.toString()));
            }
        }
        // set runtime information
        appver.put("run.host", hostname);
        if (request.getHeader("X-Real-IP") != null) {
            appver.put("run.remote", request.getHeader("X-Real-IP"));
        } else {
            appver.put("run.remote", request.getRemoteHost());
        }

        return appver;
    }
}
