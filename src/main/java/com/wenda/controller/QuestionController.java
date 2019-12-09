package com.wenda.controller;

import com.wenda.model.*;
import com.wenda.service.CommentService;
import com.wenda.service.LikeService;
import com.wenda.service.QuestionService;
import com.wenda.service.UserService;
import com.wenda.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by xrh
 * 8:59 AM on 12/6/19 2019
 */
@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @RequestMapping(value="/question/add",method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content){
        try{
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if(hostHolder.getUser() == null){
                question.setUserId(WendaUtil.ANONYMOUS_USERID);
            }else{
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionService.addQuestion(question) > 0){
                return WendaUtil.getJSONString(0);
            }
        }catch(Exception e){
            logger.error("增加题目失败" + e.getMessage());
        }
        return WendaUtil.getJSONString(1,"失败");
    }

    @RequestMapping(value = "/question/{qid}")
    public String questionDetail(Model model, @PathVariable("qid") int questionID){
        Question question = questionService.getById(questionID);
        model.addAttribute("question", question);
        model.addAttribute("user", userService.getUser(question.getUserId()));

        List<Comment> commentList = commentService.getCommentByEntity(questionID,EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<>();
        for(Comment comment : commentList){
            ViewObject vo = new ViewObject();
            vo.set("comment",comment);
            vo.set("user",userService.getUser(comment.getUserId()));
            //是否对该问题下某条评论点过赞/踩
            if(hostHolder.getUser() == null){
                vo.set("liked",0);
            }else{
                vo.set("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()));
            }
            //对某条评论的点赞总数
            vo.set("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));

            comments.add(vo);
        }
        model.addAttribute("comments", comments);

        return "detail";
    }


}
