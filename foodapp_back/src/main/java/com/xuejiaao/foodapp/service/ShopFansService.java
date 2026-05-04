package com.xuejiaao.foodapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuejiaao.foodapp.entity.ShopFans;

/**
 * 店铺粉丝表 服务类
 *
 * @author xuejiaao
 * @since 2026-01-13
 */
public interface ShopFansService extends IService<ShopFans> {

    /**
     * 关注店铺
     *
     * @param cid 客户ID
     * @param sid 店铺ID
     * @return 操作结果
     */
    boolean followShop(Integer cid, Integer sid);

    /**
     * 取消关注店铺
     *
     * @param cid 客户ID
     * @param sid 店铺ID
     * @return 操作结果
     */
    boolean unfollowShop(Integer cid, Integer sid);

    /**
     * 检查客户是否关注了店铺
     *
     * @param cid 客户ID
     * @param sid 店铺ID
     * @return 是否已关注：true=已关注，false=未关注
     */
    boolean checkFollowStatus(Integer cid, Integer sid);

    /**
     * 获取店铺粉丝数量
     *
     * @param sid 店铺ID
     * @return 粉丝数量
     */
    Integer getShopFansCount(Integer sid);
    
    /**
     * 获取用户关注的店铺列表
     *
     * @param cid 客户ID
     * @return 关注的店铺列表
     */
    java.util.List<java.util.Map<String, Object>> getFollowedShops(Integer cid);
}