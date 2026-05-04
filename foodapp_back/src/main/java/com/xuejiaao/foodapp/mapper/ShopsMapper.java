package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.Shops;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ShopsMapper extends BaseMapper<Shops> {
    @Select("SELECT sid,sname,slogo,xgade FROM shops WHERE sid = #{sid}")
    public Shops findBySid(@Param("sid") Integer sid);
}
