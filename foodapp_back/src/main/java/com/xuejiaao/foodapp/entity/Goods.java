package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("goods")
public class Goods {
    @TableId(value = "gid",type = IdType.AUTO)
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
    @TableField(exist = false)
    private String discounts; // 商品优惠活动
}
