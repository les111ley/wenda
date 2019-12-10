package com.wenda.async.handler;

import com.wenda.async.EventHandler;
import com.wenda.async.EventModel;
import com.wenda.async.EventType;
import com.wenda.utils.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by xrh
 * 4:19 PM on 12/10/19 2019
 * 异常登录发送邮件
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MailSender mailSender;


    @Override
    public void doHandle(EventModel eventModel) {
        //若用户登录异常
        Map<String,Object> map = new HashMap<>();
        map.put("username",eventModel.getExts("username"));
        mailSender.sendWithHTMLTemplate(eventModel.getExts("email"),"登陆ip异常","mail/login_exception.html",map);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
