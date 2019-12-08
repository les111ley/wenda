package com.wenda.controller;

import com.wenda.model.EntityType;
import com.wenda.model.HostHolder;
import com.wenda.service.LikeService;
import com.wenda.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Create by xrh
 * 3:51 PM on 12/8/19 2019
 */
@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;


    @RequestMapping(value = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser() == null){
            return WendaUtil.getJSONString(999);
        }
        long likeCount = likeService.like(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,commentId);
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(value = "/dislike",method = RequestMethod.POST)
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser() == null){
            return WendaUtil.getJSONString(999);
        }
        long likeCount = likeService.dislike(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,commentId);
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }




}
