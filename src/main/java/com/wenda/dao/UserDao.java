package com.wenda.dao;

import com.wenda.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Create by xrh
 * 8:49 AM on 11/26/19 2019
 */

@Mapper
public interface UserDao {

    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME,"(", INSERT_FIELDS,
            ") values(#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
    User selectByName(String name);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    int updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    int deleteById(int id);

}
