package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.Goods;
import com.xuejiaao.foodapp.entity.vo.CurGoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
    @Select("<script>" +
            "SELECT * FROM goods " +
            "<where>" +
              "gstatus=1" +
              "<if test='cgid!=null and cgid!=24'>" +
                 " AND cgid=#{cgid}" +
              "</if>" +
              "<if test='sid!=null'>" +
                 " AND sid=#{sid}" +
              "</if>" +
            "</where>" +
            "<if test='page!=null and pageSize!=null'>" +
              " LIMIT #{page}, #{pageSize}" +
            "</if>" +
            "</script>")
    public List<Goods> findByCgid(@Param("cgid") Integer cgid, @Param("sid") Integer sid, @Param("page") Integer page, @Param("pageSize") Integer pageSize);

    @Select("SELECT * FROM goods WHERE gname LIKE #{keyword} AND gstatus=1")
    public List<Goods> findByKeyWord(@Param("keyword") String keyword);

    @Select("SELECT * FROM goods WHERE gid= #{gid} AND gstatus=1")
    public CurGoodsVo findByGid(@Param("gid") Long gid);

    @Update("UPDATE goods set " +
            "sellTotalNum = (SELECT SUM(sellNum) FROM goods_detail WHERE gid =  #{gid})," +
            "stockTotalNum = (SELECT SUM(stockNum) FROM goods_detail WHERE gid =  #{gid}) " +
            " WHERE gid =  #{gid} ")
    public int updateByGid(@Param("gid") Long gid);
}
