package com.xuejiaao.foodapp.service;

import com.xuejiaao.foodapp.entity.Shops;
import com.xuejiaao.foodapp.mapper.ShopsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopsService {
    @Autowired
    private ShopsMapper shopsMapper;

    /**
     * 根据店铺id,查询当前店铺
     * @param sid
     * @return
     */
    public Shops findById(Integer sid){
        return shopsMapper.selectById(sid);
    }

}
