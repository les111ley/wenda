package com.wenda.service;

import com.wenda.dao.FeedDao;
import com.wenda.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create by xrh
 * 4:29 PM on 12/11/19 2019
 */
@Service
public class FeedService {
    @Autowired
    FeedDao feedDao;


    public boolean addFeed(Feed feed){
        feedDao.addFeed(feed);
        return feed.getId() > 0;
    }

    /**
     * 拉模式获取消息流
     * @param maxId
     * @param userIds
     * @param count
     * @return
     */
    public List<Feed> getUserFeeds(int maxId,List<Integer> userIds, int count){
        return feedDao.selsectUserFeeds(maxId, userIds, count);
    }

    /**
     * 推模式获取消息流
     * @param id
     * @return
     */
    public Feed getById(int id){
        return feedDao.getFeedById(id);
    }


}
