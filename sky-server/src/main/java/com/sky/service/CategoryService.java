package com.sky.service;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {

    /**
     * category pagination inquiry
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pagination(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * Category status setup
     * @param status
     * @param id
     * @return
     */
    Integer startOrStop(Integer status, Long id);

    /**
     * Category insert new item
     * @param categoryDTO
     * @return
     */
    Integer add(CategoryDTO categoryDTO);

    /**
     * Category delete by id
     * @param id
     * @return
     */
    Integer deleteById(Long id);

    /**
     * Category detail update
     * @param categoryDTO
     * @return
     */
    Integer update(CategoryDTO categoryDTO);

    /**
     * Category select by type
     * @param type
     * @return
     */
    List<Category> selectByType(Integer type);
}
