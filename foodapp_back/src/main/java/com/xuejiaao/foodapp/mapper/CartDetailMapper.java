package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.CartDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartDetailMapper extends BaseMapper<CartDetail> {

    @Select("SELECT * FROM cart_detail WHERE cartid = #{cartid}")
    public List<CartDetail> findByCartid(@Param("cartid") Long cartid);

    @Delete("DELETE FROM `cart_detail` WHERE cartid in (SELECT cartid FROM cart WHERE cid = #{cid})")
    public int deleteCartDetailByCid(@Param("cid") Long cid);

    @Delete("DELETE FROM cart_detail WHERE cartid = #{cartid} and gdid = #{gdid}")
    public int deleteByCartidAndGdid(@Param("cartid") Long cartid,@Param("gdid") Long gdid);
}
