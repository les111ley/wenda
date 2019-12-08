package com.wenda.model;

import org.springframework.stereotype.Component;

/**
 * Create by xrh
 * 3:19 PM on 12/5/19 2019
 * 与拦截器配合使用
 * 在 preHandle 将每个客户（线程）的 User 都存入 ThreadLocal 中（相当于一个map）
 * 方便以后在上下文中使用（例如渲染view）
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }

    public void setUsers(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }

}
