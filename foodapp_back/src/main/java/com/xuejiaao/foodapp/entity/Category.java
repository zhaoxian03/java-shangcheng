package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "category")
public class Category {
    @TableId(value = "cgid",type = IdType.AUTO)
    private Integer cgid;//'类别id'
    private String cgname;//'类别名称'
    private String ctag;//'副标题'
    private Integer cgstatus;//'类别状态: 1正常 2.下线'
    private Integer position;//'排序位置'
}
