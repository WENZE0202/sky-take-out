package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
@Api(tags = "Category Related Api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * select by type
     * @param type 1: dish category 2: set-meal category null: both
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("1. Select by type")
    public Result<List<Category>> selectByType(Integer type){
        log.info("[SELECT] select by type: {}", type);
        List<Category> list =  categoryService.selectByType(type);
        return Result.success(list);
    }
}
