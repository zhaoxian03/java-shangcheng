package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 联系人实体类
 */
@Data
@TableName("contact")
public class Contact implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer contactId;      // 联系人ID
    
    private Integer userId;         // 所属用户ID
    private String contactName;     // 联系人姓名
    private String phone;           // 联系电话
    private String contactEmail;    // 联系人邮箱
    private String avatar;          // 头像URL
    private String remark;          // 备注名
    private String company;         // 公司
    private String position;        // 职位
    private String address;         // 地址
    private Integer groupId;        // 分组ID
    private Boolean isFavorite;     // 是否收藏
    private Date createdTime;       // 创建时间
    private Date updatedTime;       // 更新时间
    private Boolean status;         // 状态
}