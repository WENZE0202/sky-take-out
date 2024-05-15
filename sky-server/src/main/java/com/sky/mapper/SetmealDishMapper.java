package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SetmealDishMapper {

    /**
     * insert multi set-meal dishes records
     * @param setmealDishes
     */
    public void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * select by set meal id
     * @param setmealId
     * @return
     */
    @Select("select * from sky_take_out.setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> selectBySetmealId(Long setmealId);

    /**
     * delete by set meal id(UPDATE set meal dish purpose)
     * @param setmealId
     */
    @Delete("delete from sky_take_out.setmeal_dish where setmeal_id = #{setmealId}")
    void deleteById(Long setmealId);
}
