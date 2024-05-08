package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FlavorMapper {

    /**
     * insert flavors
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * delete by dish ids
     * @param dishIds
     */
    void deleteByDishIds(List<Long> dishIds);

    /**
     * select by dish id
     * @param dishId
     * @return
     */
    @Select("select * from sky_take_out.dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> selectById(Long dishId);
}
