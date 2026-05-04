package com.xuejiaao.foodapp.service;

import com.xuejiaao.foodapp.entity.Safeguard;
import com.xuejiaao.foodapp.mapper.SafeguardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SafeguardService {
    @Autowired
    private SafeguardMapper safeguardMapper;

    /**
     * 根据商品id,查询此商品的保障信息
     * @param gid
     * @return
     */
    public List<Safeguard> findByGid(Long gid){
        return safeguardMapper.findByGid(gid);
    }
}
