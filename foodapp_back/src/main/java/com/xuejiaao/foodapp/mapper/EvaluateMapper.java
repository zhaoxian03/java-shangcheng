package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.Evaluate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EvaluateMapper extends BaseMapper<Evaluate> {
    @Select("SELECT e.*,c.cid,c.douyinCode,c.headerimg FROM evaluate e" +
            "   INNER JOIN customer c ON e.cid = c.cid" +
            "      WHERE e.gid = #{gid}")
    public List<Evaluate> getEvaluateTwo(@Param("gid") Long gid);

    @Select("SELECT COUNT(1) FROM evaluate WHERE gid=#{gid}")
    public Integer getEvaluateCount(@Param("gid") Long gid);
    
    /**
     * 根据订单ID查询评价
     * @param orderid 订单ID
     * @return 评价列表
     */
    @Select("SELECT e.*,c.cid,c.douyinCode,c.headerimg FROM evaluate e" +
            "   INNER JOIN customer c ON e.cid = c.cid" +
            "   INNER JOIN orders_detail od ON e.gid = od.gid AND e.gdid = od.gdid" +
            "   WHERE od.orderid = #{orderid}")
    public List<Evaluate> getEvaluatesByOrderId(@Param("orderid") Long orderid);
}
