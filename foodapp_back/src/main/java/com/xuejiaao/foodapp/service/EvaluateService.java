package com.xuejiaao.foodapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuejiaao.foodapp.entity.Evaluate;
import com.xuejiaao.foodapp.mapper.EvaluateMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluateService extends ServiceImpl<EvaluateMapper, Evaluate> implements IService<Evaluate> {
    @Autowired
    private EvaluateMapper evaluateMapper;

    /**
     * 根据商品id,查询当前商品的评价数据
     * @param gid
     * @return
     */
    public List<Evaluate> getEvaluateTwo(Long gid){
        return evaluateMapper.getEvaluateTwo(gid);
    }

    /**
     * 根据商品id,查询当前商品的评价总数量
     * @param gid
     * @return
     */
    public Integer getEvaluateCount(@Param("gid") Long gid){
        return evaluateMapper.getEvaluateCount(gid);
    }
    
    /**
     * 根据订单ID查询评价
     * @param orderid 订单ID
     * @return 评价列表
     */
    public List<Evaluate> getEvaluatesByOrderId(Long orderid){
        return evaluateMapper.getEvaluatesByOrderId(orderid);
    }
}
