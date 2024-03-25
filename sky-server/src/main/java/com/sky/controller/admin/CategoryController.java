package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "Category Related Api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * (1) Category pagination inquiry
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("1. Category Pagination")
    public Result<PageResult> pagination(CategoryPageQueryDTO categoryPageQueryDTO){
        PageResult pageResult = categoryService.pagination(categoryPageQueryDTO);

        return Result.success(pageResult);
    }



    /**
     * (2) Category status enable OR disable
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("2. Category Status")
    public Result startOrStop(@PathVariable Integer status, Long id){
        Integer count = categoryService.startOrStop(status, id);
        if(count == 0){
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        return Result.success();
    }


    /**
     * (3) Category insert new item
     * @return
     */
    @PostMapping
    @ApiOperation("3. Category Insert")
    public Result add(@RequestBody CategoryDTO categoryDTO){
        Integer count = categoryService.add(categoryDTO);
        if(count == 0){
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        return Result.success();
    }


    /**
     * (4) Category delete by id
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("4. Category delete")
    public Result deleteById(Long id){
        Integer count = categoryService.deleteById(id);
        if(count == 0){
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        return Result.success();
    }


    /**
     * (5) Category detail update
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("5. Category update")
    public Result update(@RequestBody CategoryDTO categoryDTO){
        Integer count = categoryService.update(categoryDTO);
        if(count == 0){
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        return Result.success();
    }


    /**
     * (6) Category select by type
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("6. Category select by type")
    public Result selectByType(Integer type){
        List<Category> categories = categoryService.selectByType(type);
        return Result.success(categories);
    }



}
