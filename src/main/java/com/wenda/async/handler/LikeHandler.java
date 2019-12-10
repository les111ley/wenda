package com.wenda.async.handler;

import com.wenda.async.EventHandler;
import com.wenda.async.EventModel;
import com.wenda.async.EventType;
import com.wenda.model.Message;
import com.wenda.model.User;
import com.wenda.service.MessageService;
import com.wenda.service.UserService;
import com.wenda.utils.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Create by xrh
 * 2:04 PM on 12/9/19 2019
 * 发送点赞消息
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreateDate(new Date());
        User user = userService.getUser(eventModel.getActorId());
        message.setContent("用户" + user.getName()
                + "赞了你的评论,http://127.0.0.1:8080/question/" + eventModel.getExts("questionId"));

        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
