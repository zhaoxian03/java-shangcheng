package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("cart_detail")
public class CartDetail implements Serializable {
    @TableId(value = "cdid", type = IdType.AUTO)
    private Long cdid;//'购物车详细id'
    private Long gid;//'商品id'
    private Long gdid;//'商品详细id'
    private Long cartid;//'购物车id'
    private String gname;//'商品名称'
    private String icon;//'商品图片
    private String bcontent;//'商品内容'
    private Double buyPrice;//'商品价格'
    private Integer buyNums;//'商品数量'
    @TableField(exist = false)
    private Boolean checked=false; // 是否选中
}
