package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * select by openid
     * @param openid
     * @return
     */
    @Select("select * from sky_take_out.user where openid = #{openid}")
    public User selectByOpenid(String openid);

    /**
     * insert
     * @param user
     */
    void insert(User user);

    /**
     * select by user id
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.user where id = #{id}")
    User getById(Long id);

    /**
     * count new user number daily and daily user history total
     * @param map LocalDate start time/ end time
     * @return
     */
    Integer getCountByMap(Map map);
}
