package com.xuejiaao.foodapp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuejiaao.foodapp.entity.Discounts;
import com.xuejiaao.foodapp.mapper.DiscountsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountsService {
    @Autowired
    private DiscountsMapper discountsMapper;

    /**
     * 根据商品id,查询对应的优惠活动
     * @param gid
     * @return
     */
    public Discounts findByGid(Long gid){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("gid",gid);
        return discountsMapper.selectOne(queryWrapper);
    }
}
