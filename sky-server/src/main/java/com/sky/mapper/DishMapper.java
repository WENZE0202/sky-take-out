package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DishMapper {

    /**
     * Select dish by category id
     * @return
     */
    @Select("select * from sky_take_out.dish where category_id = #{categoryId}")
    List<DishVO> selectByCategoryId(Long categoryId);

    /**
     * Insert dish
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * Pagination dish
     * @return
     */
    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * select by id
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.dish where id = #{id}")
    Dish selectById(Long id);

    /**
     * delete by id
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * update
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);


}
