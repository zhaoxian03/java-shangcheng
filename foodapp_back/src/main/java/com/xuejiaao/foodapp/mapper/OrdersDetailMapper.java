package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.OrdersDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrdersDetailMapper extends BaseMapper<OrdersDetail> {
    @Select("Select * From orders_detail Where orderid = #{orderid}")
    public List<OrdersDetail> findByOrderid(@Param("orderid") Long orderid);
}
