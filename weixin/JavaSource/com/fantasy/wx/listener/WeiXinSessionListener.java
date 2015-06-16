package com.fantasy.wx.listener;

import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.PropertiesHelper;
import com.fantasy.wx.service.UserInfoWeiXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WeiXinSessionListener implements ApplicationListener<AuthenticationSuccessEvent> {

    public static final String WEIXIN_APPID = "WEIXIN_APPID";

    @Autowired
    private UserInfoWeiXinService userInfoWeiXinService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        UserDetails details = (UserDetails) event.getAuthentication().getPrincipal();
        if (details != null) {
            Map<String, Object> data = ClassUtil.getValue(details, "data");
            if (data != null && !data.containsKey(WEIXIN_APPID)) {
                System.err.println("通过用户名,查询对应的APPID");
                data.put(WEIXIN_APPID, PropertiesHelper.load("props/application.properties").getProperty("weixin.appid"));
            }
        }
    }
}