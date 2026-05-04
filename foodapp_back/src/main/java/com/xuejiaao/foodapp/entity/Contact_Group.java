package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 联系人分组实体类
 */
@Data
@TableName("contact_group")
public class Contact_Group implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer groupId;        // 分组ID
    
    private Integer userId;         // 所属用户ID
    private String groupName;       // 分组名称
    private Integer contactCount;   // 分组内联系人数量
    private java.util.Date createdTime; // 创建时间
    private java.util.Date updatedTime; // 更新时间
}