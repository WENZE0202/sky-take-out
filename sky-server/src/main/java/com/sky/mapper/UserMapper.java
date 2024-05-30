package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
