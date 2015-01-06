package com.fantasy.wx.framework.factory;

import com.fantasy.wx.framework.exception.NoSessionException;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.session.WeiXinSession;

public class WeiXinSessionUtils {

    /**
     * 当前 session 对象
     */
    private static ThreadLocal<WeiXinSession> current = new ThreadLocal<WeiXinSession>();

    public static WeiXinSession getCurrentSession() throws WeiXinException {
        if (current.get() == null) {
            throw new NoSessionException("未初始化 WeiXinSession 对象");
        }
        return current.get();
    }

    protected static WeiXinSession saveSession(WeiXinSession session) {
        current.set(session);
        return session;
    }
}