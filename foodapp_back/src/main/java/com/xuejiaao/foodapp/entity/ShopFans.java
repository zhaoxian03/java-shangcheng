package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 店铺粉丝表
 *
 * @author xuejiaao
 * @since 2026-01-13
 */
@Data
@TableName("shop_fans")
public class ShopFans {

    /**
     * 主键ID
     */
    @TableId(value = "fid", type = IdType.AUTO)
    private Integer fid;

    /**
     * 客户ID
     */
    private Integer cid;

    /**
     * 店铺ID
     */
    private Integer sid;

    /**
     * 关注时间
     */
    @TableField("focus_time")
    private Date focusTime;

    /**
     * 关注状态：1=已关注，0=已取消
     */
    private Integer status;
}