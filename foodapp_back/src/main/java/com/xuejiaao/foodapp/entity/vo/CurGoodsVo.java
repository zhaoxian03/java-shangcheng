package com.xuejiaao.foodapp.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xuejiaao.foodapp.entity.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 封装当前商品的数据模型
 * 1. 商品信息表   goods
 * 2. 商品详细信息表 goods_detail
 * 3. 商品所属店铺 shops
 * 4. 商品保障列表 safeguard 和 goods_safeguard
 * 5. 商品评价表 evaluate
 * 6. 商品优惠活动表 discounts
 * =====它们的关系=======
 * goods====>goods_detail 一对多关系
 * goods====>shops 一对一关系
 * goods====>safeguard  一对多关系
 * goods====>evaluate 一对多关系
 * goods====>discounts 一对一关系
 */

@Data
public class CurGoodsVo implements Serializable {
    private Long gid;//'商品id'
    private String gname;//'商品名称
    private String icon;//'商品图片多个用;分号分隔'
    private String content;//'商品内容'
    private Double price;//商品价格（无类型）
    private Integer gstatus;//商品状态：0,下架  1,上架
    private Integer stockTotalNum;//'商品库存总量'
    private Integer sellTotalNum;//商品总销售量
    private Integer selectType;//'用户未选择商品时显示的类型：1.商品标题 2.图片缩略图'
    private String selectTypeText;//'提示用户有多少类型可选择'
    private Integer restBuyNum;//'限购数量'
    private Double postprice;//'邮费'
    private Integer cgid;//'商品类型id'
    private Integer sid;//'所属店铺id'
    private String discounts; // 商品优惠活动
    private Integer evaluateCount;// 当前商品评价数量
    private String[] icons; // 商品图片数组

    public String[] getIcons() {
        return icon.split(";");
    }
    /* * =====它们的关系=======
     *  goods====>goods_detail 一对多关系
     *  goods====>shops 一对一关系
     *  goods====>safeguard  一对多关系
     *  goods====>evaluate 一对多关系
     *  goods====>discounts 一对一关系
     */
    //  goods====>goods_detail 一对多关系
    private List<GoodsDetail> goodsDetailList;// 商品详细
    //  goods====>shops 一对一关系
    private Shops shop;  // 商品所属的店辅
    //  goods====>safeguard  一对多关系
    private List<Safeguard> safeguardList; //商品保障列表
    //  goods====>evaluate 一对多关系
    private List<Evaluate> evaluateList;//商品评价表
    //  goods====>discounts 一对一关系
    private Discounts discountsmodel; //商品优惠活动
}
