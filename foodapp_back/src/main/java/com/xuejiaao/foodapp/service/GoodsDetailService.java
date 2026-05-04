package com.xuejiaao.foodapp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuejiaao.foodapp.entity.GoodsDetail;
import com.xuejiaao.foodapp.mapper.GoodsDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsDetailService {
    @Autowired
    private GoodsDetailMapper goodsDetailMapper;

    /**
     * 根据商品id,查询此商品的详细的数据
     * @param gid
     * @return
     */
    public List<GoodsDetail> findByGid(Long gid){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("gid",gid);
        return goodsDetailMapper.selectList(queryWrapper);
    }

    /**
     * 新增商品详情
     * @param goodsDetail
     * @return
     */
    public boolean addGoodsDetail(GoodsDetail goodsDetail) {
        return goodsDetailMapper.insert(goodsDetail) > 0;
    }

    /**
     * 更新商品详情
     * @param goodsDetail
     * @return
     */
    public boolean updateGoodsDetail(GoodsDetail goodsDetail) {
        return goodsDetailMapper.updateById(goodsDetail) > 0;
    }

    /**
     * 删除商品详情
     * @param gdid
     * @return
     */
    public boolean deleteGoodsDetail(Long gdid) {
        return goodsDetailMapper.deleteById(gdid) > 0;
    }

    /**
     * 批量删除商品详情
     * @param gids
     * @return
     */
    public boolean deleteGoodsDetailsByGid(Long gid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("gid", gid);
        return goodsDetailMapper.delete(queryWrapper) > 0;
    }

    /**
     * 根据商品详情id查询商品详情
     * @param gdid
     * @return
     */
    public GoodsDetail findByGdid(Long gdid) {
        return goodsDetailMapper.selectById(gdid);
    }
}

