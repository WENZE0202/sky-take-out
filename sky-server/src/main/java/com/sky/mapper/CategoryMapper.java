package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CategoryMapper {

    /**
     * Page Helper dynamic interpolate LIMIT query(ThreadLocal)
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pagination(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * General update dynamically
     * @param category
     * @return
     */
    @AutoFill(value = OperationType.UPDATE)
    Integer update(Category category);

    /**
     * Insert new category
     * @param category
     * @return
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into sky_take_out.category (type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "values (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    Integer add(Category category);

    /**
     * Delete by id
     * @param id
     * @return
     */
    @Delete("delete from sky_take_out.category where id = #{id}")
    Integer deleteById(Long id);

    /**
     * Select by type
     * @param type
     * @return
     */
    List<Category> selectByType(Integer type);


}
