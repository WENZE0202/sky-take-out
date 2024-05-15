package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SetMealMapper {
    /**
     * Select set-meal by id
     * @return
     */
    @Select("select * from sky_take_out.setmeal where category_id = #{categoryId}")
    List<Setmeal> selectByCategoryId(Long categoryId);

    /**
     * select by dish ids
     * @param dishIds
     * @return
     */
    List<Setmeal> selectByDishIds(List<Long> dishIds);

    /**
     * setmeal page query
     * @return
     */
    Page<Setmeal> page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * add set meal record
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * set meal status start or stop
     * @param status
     */
    @Update("update sky_take_out.setmeal set status = #{status} where id = #{id}")
    void startOrStop(Integer status, Long id);

    /**
     * set meal batch delete by ids
     * @param ids
     */
    void deleteBatch(Long[] ids);

    /**
     * select by set meal id
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.setmeal where id = #{id}")
    Setmeal selectById(Long id);

    /**
     * update
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);
}
