package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * USER controller: list down all category records
     * Redis: cache the records avoid DBMS operation frequently
     * @param categoryId
     * @return
     */
    public List<Setmeal> selectByCategoryId(Long categoryId) {
        // Redis records checking
        String key = "Setmeal_" + categoryId;
        List<Setmeal> list  = (List<Setmeal>) redisTemplate.opsForValue().get(key);
        if(list != null && list.size() > 0){
            return list;
        }

        // MySQL query and save into Redis
        list = setMealMapper.selectByCategoryId(categoryId);
        redisTemplate.opsForValue().set(key,list);
        return list;
    }


    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        // interpolate LIMIT query
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setMealMapper.page(setmealPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Transactional
    public void insert(SetmealDTO setmealDTO) {
        // encapsulate and insert set meal object (NEED RETURN GENERATED SET-MEAL ID)
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setStatus(StatusConstant.DISABLE);
        setMealMapper.insert(setmeal);
        Long setmealId = setmeal.getId();

        // encapsulate and insert list of set meal object (SETUP SET-MEAL ID)
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && setmealDishes.size() > 0){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
        }
        setmealDishMapper.insertBatch(setmealDishes);

    }


    public void startOrStop(Integer status, Long id) {
        setMealMapper.startOrStop(status, id);
    }


    public void deleteBatch(Long[] ids) {
        setMealMapper.deleteBatch(ids);
    }


    @Transactional
    public SetmealVO selectById(Long id) {
        // set meal table
        Setmeal setmeal = setMealMapper.selectById(id);

        // set meal dish table
        List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(id);

        // encapsulate set meal VO object
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    @Transactional
    public void update(SetmealDTO setmealDTO) {
        // UPDATE set meal table
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        Long setmealId = setmeal.getId();

        setMealMapper.update(setmeal);

        // UPDATE set meal dish table: delete then insert
        setmealDishMapper.deleteById(setmealId);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        setmealDishMapper.insertBatch(setmealDishes);
    }
}
