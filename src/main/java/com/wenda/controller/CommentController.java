package com.wenda.controller;

import com.wenda.model.Comment;
import com.wenda.model.EntityType;
import com.wenda.model.HostHolder;
import com.wenda.service.CommentService;
import com.wenda.service.QuestionService;
import com.wenda.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Create by xrh
 * 7:51 PM on 12/6/19 2019
 */
@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/addComment"},method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            if(hostHolder.getUser() != null){
                //用户已登陆
                comment.setUserId(hostHolder.getUser().getId());
            }else{
                //匿名状态浏览
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }

            comment.setCreateDate(new Date());
            comment.setEntityType(EntityType.ENTITY_COMMENT);
            comment.setEntityId(questionId);
            commentService.addComment(comment);
            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(),count);

        } catch (Exception e) {
            logger.error("添加评论失败" + e.getMessage());
        }

        return "redirect:/question/" + questionId;

    }

}
