package com.wenda.dao;

import com.wenda.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Create by xrh
 * 4:27 PM on 12/11/19 2019
 */
@Mapper
public interface FeedDao {
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " user_id, data, create_date, type ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME, "(",INSERT_FIELDS,
            ") values (#{userId},#{data},#{createDate},#{type})"})
    int addFeed(Feed feed);

    //"拉"模式，获取关注人的新鲜事
    List<Feed> selsectUserFeeds(@Param("maxId") int maxId,
                                @Param("userIds") List<Integer> userIds,
                                @Param("count") int count);

    //"推"模式
    @Select({"select",SELECT_FIELDS, "from", TABLE_NAME, "where id=#{Id}"})
    Feed getFeedById(int id);

}
