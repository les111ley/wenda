package com.wenda.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Create by xrh
 * 4:34 PM on 12/11/19 2019
 * 新鲜事feed流
 */
@Component
public class Feed {
    private int id;
    private int type;
    private int userId;
    private Date createdDate;
    //JSON
    private String data;
    private JSONObject dataJSON = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getData() {
        return data;
    }

    //将字符串data中的存储的数据在存储到一个JSONObject对象中
    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }

    //velocity可以识别 get 方法，所以构造一个get方法，用来获取JSONObject中的数据
    public String get(String key) {
        return dataJSON == null ? null : dataJSON.getString(key);
    }
}
