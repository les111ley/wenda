package com.wenda.dao;

import com.wenda.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Create by xrh
 * 11:28 AM on 12/5/19 2019
 */
@Mapper
public interface LoginTicketDao {
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, expired, status, ticket ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME, "(",INSERT_FIELDS,
            ") values (#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);

    @Select({"select", SELECT_FIELDS,"from",TABLE_NAME, "where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update",TABLE_NAME,"set status=#{status} where ticket=#{ticket}"})
    boolean updateStatus(@Param("ticket") String ticket, @Param("status") int status);


}



