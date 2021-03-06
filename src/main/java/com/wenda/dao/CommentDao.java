package com.wenda.dao;

import com.wenda.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Create by xrh
 * 7:32 PM on 12/6/19 2019
 */
@Mapper
public interface CommentDao {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, entity_id,  entity_type, create_date, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME, "(",INSERT_FIELDS,
            ") values (#{userId},#{content},#{entityId},#{entityType},#{createDate},#{status})"})
    int addComment(Comment comment);

    @Select({"select",SELECT_FIELDS, "from", TABLE_NAME,
    "where entity_id=#{entityId} and entity_type=#{entityType} order by create_date desc"})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId,
                                         @Param("entityType") int entityType);

    @Select({"select count(id) from", TABLE_NAME,
            "where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,
                                        @Param("entityType") int entityType);


    @Select({"select",SELECT_FIELDS, "from", TABLE_NAME, "where id=#{Id}"})
   Comment getCommentById(int Id);

    @Select({"select count(id) from ", TABLE_NAME, " where user_id=#{userId}"})
    int getUserCommentCount(int userId);

    @Update({"update comment set status={status}"})
    int updateStauts(@Param("id") int id,
                      @Param("status") int status);
}
