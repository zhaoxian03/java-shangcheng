package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.GoodsDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GoodsDetailMapper extends BaseMapper<GoodsDetail> {
    @Update("UPDATE goods_detail set sellNum = sellNum + #{curSellNum},stockNum = stockNum - #{curSellNum} WHERE gdid = #{gdid}")
    public int updateByGdid(@Param("gdid")Long gdid,@Param("curSellNum") Integer curSellNum);
}
