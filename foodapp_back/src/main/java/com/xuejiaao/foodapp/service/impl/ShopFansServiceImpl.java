package com.xuejiaao.foodapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuejiaao.foodapp.entity.ShopFans;
import com.xuejiaao.foodapp.mapper.ShopFansMapper;
import com.xuejiaao.foodapp.service.ShopFansService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 店铺粉丝表 服务实现类
 *
 * @author xuejiaao
 * @since 2026-01-13
 */
@Service
public class ShopFansServiceImpl extends ServiceImpl<ShopFansMapper, ShopFans> implements ShopFansService {

    @Override
    public boolean followShop(Integer cid, Integer sid) {
        // 检查是否已关注
        LambdaQueryWrapper<ShopFans> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopFans::getCid, cid)
                .eq(ShopFans::getSid, sid);
        ShopFans shopFans = getOne(queryWrapper);

        if (shopFans == null) {
            // 未关注，创建新记录
            shopFans = new ShopFans();
            shopFans.setCid(cid);
            shopFans.setSid(sid);
            shopFans.setStatus(1);
            shopFans.setFocusTime(new Date()); // 设置关注时间
            return save(shopFans);
        } else {
            // 已存在，更新状态为已关注
            shopFans.setStatus(1);
            return updateById(shopFans);
        }
    }

    @Override
    public boolean unfollowShop(Integer cid, Integer sid) {
        // 更新关注状态为已取消
        LambdaQueryWrapper<ShopFans> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(ShopFans::getCid, cid)
                .eq(ShopFans::getSid, sid);
        ShopFans shopFans = new ShopFans();
        shopFans.setStatus(0);
        return update(shopFans, updateWrapper);
    }

    @Override
    public boolean checkFollowStatus(Integer cid, Integer sid) {
        // 查询关注状态
        Integer status = baseMapper.getStatusByCidAndSid(cid, sid);
        return status != null && status == 1;
    }

    @Override
    public Integer getShopFansCount(Integer sid) {
        // 统计店铺粉丝数量
        return baseMapper.countBySid(sid);
    }
    
    @Override
    public java.util.List<java.util.Map<String, Object>> getFollowedShops(Integer cid) {
        // 获取用户关注的店铺列表
        return baseMapper.getFollowedShops(cid);
    }
}