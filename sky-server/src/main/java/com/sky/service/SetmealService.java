package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * select by category id
     * @param categoryId
     * @return
     */
    List<Setmeal> selectByCategoryId(Long categoryId);

    /**
     * page query
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * insert set meal record
     * @param setmealDTO
     */
    void insert(SetmealDTO setmealDTO);

    /**
     * status start or stop
     * @param status
     */
    void startOrStop(Integer status, Long id);

    /**
     * batch delete by ids
     * @param ids
     */
    void deleteBatch(Long[] ids);

    /**
     * select by id
     * @param id
     * @return
     */
    SetmealVO selectById(Long id);

    /**
     * update
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);
}
