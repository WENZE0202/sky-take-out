package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import lombok.ToString;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * select by dishId, set-mealId and userId
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * update number add on one
     * @param shoppingCart
     */
    void updateByNumber(ShoppingCart shoppingCart);

    /**
     * add shopping cart item
     * @param shoppingCart
     */
    @Insert("insert into sky_take_out.shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time)" +
            "values(#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
    void add(ShoppingCart shoppingCart);

    /**
     * clean all shopping cart items by user id
     * @param userId
     */
    @Delete("delete from sky_take_out.shopping_cart where user_id = #{userId}")
    void clean(Long userId);
}
