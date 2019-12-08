package com.wenda.controller;

import com.wenda.model.HostHolder;
import com.wenda.model.Message;
import com.wenda.model.User;
import com.wenda.model.ViewObject;
import com.wenda.service.MessageService;
import com.wenda.service.UserService;
import com.wenda.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by xrh
 * 4:02 PM on 12/7/19 2019
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;


    @RequestMapping(path = {"/msg/addMessage"},method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content){
        try {
            if(hostHolder.getUser() == null) {
                return WendaUtil.getJSONString(999, "未登录");
            }
            User user = userService.selectByName(toName);
            if(user == null){
                return WendaUtil.getJSONString(1,"用户不存在");
            }
            Message message = new Message();
            message.setCreateDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setContent(content);
            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);

        } catch (Exception e) {
            logger.error("发送消息失败" + e.getMessage());
            return WendaUtil.getJSONString(1,"发信失败");
        }
    }

    @RequestMapping(path = {"/msg/detail"},method = RequestMethod.GET)
    public String getConversationDetail(Model model,
                                        @RequestParam("conversationId") String concersationId){

        try {
            List<Message> messageList = messageService.getConversationDetail(concersationId,0,10);
            List<ViewObject> vos = new ArrayList<>();
            for(Message message : messageList){
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user",userService.getUser(message.getFromId()));
                vos.add(vo);
            }
            model.addAttribute("messages",vos);
        }catch(Exception e){
            logger.error("获取详情失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/list"},method = RequestMethod.GET)
    public String getConversationList(Model model){
       try{
           if(hostHolder.getUser() == null){
               return "redirect:/reglogin";
           }
           int localUserId = hostHolder.getUser().getId();
           List<Message> conversationList = messageService.getConversationList(localUserId,0,10);
           List<ViewObject> vos = new ArrayList<>();
           for(Message message : conversationList){
               ViewObject vo = new ViewObject();
               vo.set("message",message);
               int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
               vo.set("user",userService.getUser(targetId));
               vo.set("unRead",messageService.getConversationUnreadCount(localUserId,message.getConversationId()));
               vos.add(vo);
           }
           model.addAttribute("conversations",vos);
       }catch(Exception e){
           logger.error("获取消息列表失败" + e.getMessage());
       }
       return "letter";
    }
}
