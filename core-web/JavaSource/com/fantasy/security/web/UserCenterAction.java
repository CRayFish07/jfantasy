package com.fantasy.security.web;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

public class UserCenterAction extends ActionSupport{

    @Autowired
    private UserService userService;

    public String retrievePassword(String email){
        this.attrs.put("massige",this.userService.retrievePassword(email));
        return JSONDATA;
    }
}