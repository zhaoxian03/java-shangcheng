package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.Cart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {
    @Results(id="cartListMap",value = {
            @Result(column = "cartid",property ="cartid"),
            @Result(column = "cid",property ="cid"),
            @Result(column = "sid",property ="sid"),
            @Result(column = "buyTotalNums",property ="buyTotalNums"),
            @Result(column = "buyTotalPrice",property ="buyTotalPrice"),
            @Result(column = "beizhu",property ="beizhu"),
            @Result(column = "cartid",property ="goodsDetail",many = @Many(select = "com.xuejiaao.foodapp.mapper.CartDetailMapper.findByCartid")),
            @Result(column = "sid",property = "shop",one = @One(select = "com.xuejiaao.foodapp.mapper.ShopsMapper.findBySid"))
    })
    @Select("SELECT * FROM cart WHERE cid = #{cid}")
    public List<Cart> getCartList(@Param("cid") Long cid);

    @Delete("DELETE FROM cart WHERE cartid = #{cartid} and (SELECT COUNT(1) FROM cart_detail WHERE cartid = #{cartid})=0")
    public int deleteByCartid(@Param("cartid") Long cartid);

    @Delete("DELETE FROM `cart` WHERE cid = #{cid}")
    public int deleteCartByCid(@Param("cid") Long cid);

    @Update("UPDATE cart set " +
            "buyTotalNums = (SELECT SUM(buyNums) FROM cart_detail WHERE cartid = #{cartid})," +
            " buyTotalPrice = (SELECT SUM(buyPrice*buyNums) FROM cart_detail WHERE cartid = #{cartid}) " +
            "   WHERE cartid = #{cartid}")
    public int updateTotalNumsAndTotalPriceByCartid(@Param("cartid") Long cartid);
}
