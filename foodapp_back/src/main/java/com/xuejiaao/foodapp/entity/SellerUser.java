package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商家用户表
 */
@Data
@TableName("t_user")
public class SellerUser implements Serializable {
    @TableId(value = "userid", type = IdType.AUTO) // 主键自增，对应数据库中的userid字段
    private Integer userid;
    private String username;     // 登录账号
    private String userpwd;      // 登录密码
    @TableField(value = "shopName") // 对应数据库中的shopName字段
    private String shopName;     // 店铺名称
    private Integer role;        // 角色：0.普通用户 1.商家
    private Integer sid;         // 所属店铺ID
    @TableField(value = "lastlogintime") // 对应数据库中的lastlogintime字段
    private Date lastLoginTime;  // 最后一次登录时间
    @TableField(value = "ustatus") // 对应数据库中的ustatus字段
    private Integer ustatus;     // 状态：0.禁用 1.正常
}