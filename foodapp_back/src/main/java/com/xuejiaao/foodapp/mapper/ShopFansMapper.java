package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.ShopFans;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 店铺粉丝表 Mapper 接口
 *
 * @author xuejiaao
 * @since 2026-01-13
 */
@Mapper
public interface ShopFansMapper extends BaseMapper<ShopFans> {

    /**
     * 根据店铺ID统计粉丝数量
     *
     * @param sid 店铺ID
     * @return 粉丝数量
     */
    Integer countBySid(@Param("sid") Integer sid);

    /**
     * 根据客户ID和店铺ID查询关注状态
     *
     * @param cid 客户ID
     * @param sid 店铺ID
     * @return 关注状态：1=已关注，0=已取消，null=未关注
     */
    Integer getStatusByCidAndSid(@Param("cid") Integer cid, @Param("sid") Integer sid);
    
    /**
     * 获取用户关注的店铺列表
     *
     * @param cid 客户ID
     * @return 关注的店铺列表
     */
    @org.apache.ibatis.annotations.Select("SELECT s.sid as shopId, s.sname as shopName, s.slogo as shopImage, s.overallExperience as shopRating, s.sstatus as shopStatus, sf.focus_time as focusTime FROM shop_fans sf LEFT JOIN shops s ON sf.sid = s.sid WHERE sf.cid = #{cid} AND sf.status = 1 ORDER BY sf.focus_time DESC")
    java.util.List<java.util.Map<String, Object>> getFollowedShops(@Param("cid") Integer cid);
}