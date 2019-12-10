package com.wenda.service;

import com.wenda.dao.LoginTicketDao;
import com.wenda.dao.UserDao;
import com.wenda.model.LoginTicket;
import com.wenda.model.User;
import com.wenda.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Create by xrh
 * 9:32 AM on 11/26/19 2019
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;

    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId((userId));
        Date now = new Date();
        now.setTime(3600*24*100 + now.getTime());   //设置过期时间为100天
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDao.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public Map<String,Object> register(String username, String password){
        Map<String,Object> map= new HashMap<>();
        if(StringUtils.isEmpty(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user = userDao.selectByName(username);
        if(user !=null){
            map.put("msg","用户名已存在");
            return map;
        }
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userDao.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket","ticket");

        return map;
    }


    public Map<String,Object> login(String username, String password){
        Map<String,Object> map= new HashMap<>();

        if(StringUtils.isEmpty(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user = userDao.selectByName(username);
        if(user == null){
            map.put("msg","用户名不存在");
            return map;
        }

        if(!WendaUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码错误");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket","ticket");
        map.put("userId",user.getId());

        return map;
    }

    public User getUser(int id) {
        return userDao.selectById(id);
    }

    public void logout(String ticket) {
        loginTicketDao.updateStatus(ticket,1);
    }

    public User selectByName(String name) {
        return userDao.selectByName(name);
    }
}
