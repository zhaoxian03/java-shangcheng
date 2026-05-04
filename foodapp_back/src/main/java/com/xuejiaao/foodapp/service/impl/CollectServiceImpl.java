package com.xuejiaao.foodapp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuejiaao.foodapp.entity.Collect;
import com.xuejiaao.foodapp.mapper.CollectMapper;
import com.xuejiaao.foodapp.service.CollectService;

import lombok.RequiredArgsConstructor;

/**
 * 收藏ServiceImpl
 */
@Service
@RequiredArgsConstructor
public class CollectServiceImpl implements CollectService {
    
    private final CollectMapper collectMapper;
    
    @Override
    public boolean addGoods(Collect collect) {
        // 检查是否已收藏
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", collect.getUserId())
                   .eq("goods_id", collect.getGoodsId());
        Collect existing = collectMapper.selectOne(queryWrapper);
        
        if (existing != null) {
            // 已收藏，更新状态为正常
            existing.setStatus(1);
            int result = collectMapper.updateById(existing);
            return result > 0;
        }
        
        // 未收藏，添加新收藏
        collect.setCollectTime(new Date());
        collect.setStatus(1);
        int result = collectMapper.insert(collect);
        return result > 0;
    }
    
    @Override
    public boolean cancelGoods(String userId, Integer goodsId) {
        // 更新状态为取消收藏
        UpdateWrapper<Collect> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId)
                    .eq("goods_id", goodsId);
        Collect collect = new Collect();
        collect.setStatus(0);
        int result = collectMapper.update(collect, updateWrapper);
        return result > 0;
    }
    
    @Override
    public boolean batchDeleteGoods(String userId, List<Integer> goodsIds) {
        // 批量删除收藏
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .in("goods_id", goodsIds);
        int result = collectMapper.delete(queryWrapper);
        return result > 0;
    }
    
    @Override
    public Map<String, Object> getGoodsList(String userId, Integer page, Integer pageSize) {
        // 使用MyBatis Plus的Page对象进行分页查询
        Page<Collect> pageObj = new Page<>(page, pageSize);
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("status", 1)
                   .orderByDesc("collect_time");
        Page<Collect> resultPage = collectMapper.selectPage(pageObj, queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", resultPage.getRecords());
        result.put("total", resultPage.getTotal());
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", (int) resultPage.getPages());
        
        return result;
    }
    
    @Override
    public boolean checkGoods(String userId, Integer goodsId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("goods_id", goodsId)
                   .eq("status", 1);
        Collect collect = collectMapper.selectOne(queryWrapper);
        return collect != null;
    }
}
