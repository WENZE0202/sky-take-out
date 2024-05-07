package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DishMapper {

    /**
     * Select dish by id
     * @return
     */
    @Select("select * from sky_take_out.dish where category_id = #{id}")
    List<Dish> selectById(Long id);

    /**
     * Insert dish
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);
}
