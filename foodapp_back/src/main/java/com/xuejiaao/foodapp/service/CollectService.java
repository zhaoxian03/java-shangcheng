package com.xuejiaao.foodapp.service;

import java.util.Map;
import java.util.List;

import com.xuejiaao.foodapp.entity.Collect;

/**
 * 收藏Service
 */
public interface CollectService {
    /**
     * 添加商品收藏
     * @param collect 收藏信息
     * @return 是否添加成功
     */
    boolean addGoods(Collect collect);
    
    /**
     * 取消商品收藏
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @return 是否取消成功
     */
    boolean cancelGoods(String userId, Integer goodsId);
    
    /**
     * 批量删除收藏
     * @param userId 用户ID
     * @param goodsIds 商品ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteGoods(String userId, List<Integer> goodsIds);
    
    /**
     * 获取商品收藏列表
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 包含列表和总数的Map
     */
    Map<String, Object> getGoodsList(String userId, Integer page, Integer pageSize);
    
    /**
     * 检查商品是否已收藏
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @return 是否已收藏
     */
    boolean checkGoods(String userId, Integer goodsId);
}
