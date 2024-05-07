package com.sky.mapper;

import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SetMealMapper {
    /**
     * Select set-meal by id
     * @return
     */
    @Select("select * from sky_take_out.setmeal where id = #{categoryId}")
    List<Setmeal> selectByCategoryId(Long categoryId);

    /**
     * select by dish ids
     * @param dishIds
     * @return
     */
    List<Setmeal> selectByDishIds(List<Long> dishIds);
}
