package com.wenda.dao;

import com.wenda.model.Comment;
import com.wenda.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create by xrh
 * 3:47 PM on 12/7/19 2019
 */
@Mapper
public interface MessageDao {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read, create_date, conversation_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME, "(",INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{createDate},#{conversationId})"})
    int addMessage(Message message);

    @Select({"select",SELECT_FIELDS, "from", TABLE_NAME,
            "where conversation_id=#{conversationId} order by create_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);


    //选出与该user相关的message，按照conversationId分组，分组后显示每组最新的消息，所显示的消息也按时间顺序排列，并且统计每组的个数
    @Select({"select ", INSERT_FIELDS, " , count(id) as id from ( select * from ", TABLE_NAME,
            " where from_id=#{userId} or to_id=#{userId} order by created_date desc) tt group by conversation_id order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    @Select({"select count(id) from",TABLE_NAME,"where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId,
                                   @Param("conversationId") String conversationId);

}
