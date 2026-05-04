package com.xuejiaao.foodapp.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 收藏实体类
 */
@Data
@TableName("goods_collect")
public class Collect {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
    
    /**
     * 商品ID
     */
    @TableField("goods_id")
    private Integer goodsId;
    
    /**
     * 商品名称
     */
    @TableField("goods_name")
    private String goodsName;
    
    /**
     * 商品图片
     */
    @TableField("goods_image")
    private String goodsImage;
    
    /**
     * 商品价格
     */
    @TableField("goods_price")
    private BigDecimal goodsPrice;
    
    /**
     * 收藏时间
     */
    @TableField("collect_time")
    private Date collectTime;
    
    /**
     * 状态：1-正常，0-删除
     */
    private Integer status;
}
