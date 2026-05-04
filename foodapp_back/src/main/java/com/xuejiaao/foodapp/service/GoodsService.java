package com.xuejiaao.foodapp.service;

import com.xuejiaao.foodapp.entity.Discounts;
import com.xuejiaao.foodapp.entity.Goods;
import com.xuejiaao.foodapp.entity.vo.CurGoodsVo;
import com.xuejiaao.foodapp.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private DiscountsService discountsService;
    @Autowired
    private GoodsDetailService goodsDetailService;
    @Autowired
    private SafeguardService safeguardService;
    @Autowired
    private EvaluateService evaluateService;
    @Autowired
    private ShopsService shopsService;

    /**
     * 根据商品id,查询此商品信息及相关信息
     * @param gid
     * @return
     */
    public CurGoodsVo getCurGoodsByGid(Long gid){
       CurGoodsVo curGoodsVo = new CurGoodsVo();
        /* * =====它们的关系=======
         *  goods====>goods_detail 一对多关系
         *  goods====>shops 一对一关系
         *  goods====>safeguard  一对多关系
         *  goods====>evaluate 一对多关系
         *  goods====>discounts 一对一关系
         */
        curGoodsVo = goodsMapper.findByGid(gid);
        // goods====>goods_detail 一对多关系
        curGoodsVo.setGoodsDetailList(goodsDetailService.findByGid(gid));
        // goods====>shops 一对一关系
        curGoodsVo.setShop(shopsService.findById(curGoodsVo.getSid()));
        //  goods====>safeguard  一对多关系
        curGoodsVo.setSafeguardList(safeguardService.findByGid(gid));
        // goods====>evaluate 一对多关系
        curGoodsVo.setEvaluateList(evaluateService.getEvaluateTwo(gid));
        curGoodsVo.setEvaluateCount(evaluateService.getEvaluateCount(gid));
        // goods====>discounts 一对一关系
        Discounts dmodel= discountsService.findByGid(gid);
        //'优惠类型：1.劵后价  2.限时价 3.立减',
        if(dmodel!=null){
            if(dmodel.getDtype()==1){//1.劵后价
                curGoodsVo.setDiscounts("劵后价 | 满"+dmodel.getCouponSatisfyPrice()+"减" + dmodel.getSubPrice());
            }
            else if(dmodel.getDtype()==2){//2.限时价
                if(new Date().getTime()<=dmodel.getEndTime().getTime()){
                    curGoodsVo.setDiscounts("限时价 | "+ dmodel.getTimeLimitPrice());
                }
                else {
                    curGoodsVo.setDiscounts("");
                }
            }
            else{// 3.立减'
                curGoodsVo.setDiscounts("立减劵 | "+ dmodel.getSubPrice());
            }
        }
        else {
            curGoodsVo.setDiscounts("");
        }
       return curGoodsVo;
    }


    /**
     * 根据商品分类id和店铺id,查询对应商品分类下的商品列表
     * @param cgid
     * @param sid
     * @param page
     * @param pageSize
     * @return
     */
    public List<Goods> findByCgid(Integer cgid, Integer sid, Integer page, Integer pageSize){
        // 计算分页起始位置
        Integer startPage = (page != null && pageSize != null) ? (page - 1) * pageSize : null;
        List<Goods> list = goodsMapper.findByCgid(cgid, sid, startPage, pageSize);
        // 处理返回商品列表
        controlfindGoodsList(list);

        return list;
    }

    /**
     * 搜索
     * @param keyword
     * @return
     */
    public List<Goods> findByKeyWord(String keyword){
       List<Goods> list = goodsMapper.findByKeyWord("%"+keyword+"%");
        // 处理返回商品列表
        controlfindGoodsList(list);
        return list;
    }

    /**
     * 处理返回商品列表
     */
    public void controlfindGoodsList(List<Goods> list){
        for (Goods item :list){
            //==========处理图片==============
            String[] split = item.getIcon().split(";");
            if(split.length>0)
                item.setIcon(split[0]);

            //==========优惠活动=============
            Discounts dmodel = discountsService.findByGid(item.getGid());
            //'优惠类型：1.劵后价  2.限时价 3.立减',
            if(dmodel!=null){
                if(dmodel.getDtype()==1){//1.劵后价
                    item.setDiscounts("劵后价 | 满"+dmodel.getCouponSatisfyPrice()+"减" + dmodel.getSubPrice());
                }
                else if(dmodel.getDtype()==2){//2.限时价
                    if(new Date().getTime()<=dmodel.getEndTime().getTime()){
                        item.setDiscounts("限时价 | "+ dmodel.getTimeLimitPrice());
                    }
                    else {
                        item.setDiscounts("");
                    }
                }
                else{// 3.立减'
                    item.setDiscounts("立减劵 | "+ dmodel.getSubPrice());
                }
            }
            else {
                item.setDiscounts("");
            }
        }
    }

}
