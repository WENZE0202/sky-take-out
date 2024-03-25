package com.sky.service.impl;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;


    @Override
    public PageResult pagination(CategoryPageQueryDTO categoryPageQueryDTO) {
        // dynamic interpolate limit query, handle detail when calculate page information
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        // PageHelper Api require return value MUST Page object(com.github.pagehelper.Page)
        Page<Category> page = categoryMapper.pagination(categoryPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Integer startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();

        // general and dynamically update category table in our business
        Integer count = categoryMapper.update(category);

        return count;
    }

    @Override
    public Integer add(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        // New added category b4 add dish is have no value to display in customer view(enable)
        category.setStatus(StatusConstant.DISABLE);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());

        Integer count = categoryMapper.add(category);
        return count;
    }

    @Override
    public Integer deleteById(Long id) {
        // Business logic is b4 drop MUST determine there is no any item(dish/set-meal) exist
        Integer count1 = dishMapper.selectById(id).size();
        if(count1 > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        Integer count2 = setMealMapper.selectById(id).size();
        if(count2 > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        Integer count3 = categoryMapper.deleteById(id);
        return count3;
    }


    public Integer update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setUpdateUser(BaseContext.getCurrentId());
        category.setUpdateTime(LocalDateTime.now());
        Integer count = categoryMapper.update(category);
        return count;
    }


    public List<Category> selectByType(Integer type) {
        List<Category> list = categoryMapper.selectByType(type);
        return list;
    }
}
