package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.Safeguard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SafeguardMapper extends BaseMapper<Safeguard> {
    @Select("SELECT * FROM safeguard s" +
            "  INNER JOIN goods_safeguard gs ON s.sgid = gs.sgid " +
            "     WHERE gs.gid = #{gid}")
   public List<Safeguard> findByGid(@Param("gid") Long gid);
}
