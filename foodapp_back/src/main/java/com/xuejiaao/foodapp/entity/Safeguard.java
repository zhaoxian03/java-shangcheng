package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("safeguard")
public class Safeguard implements Serializable {
    @TableId(value = "sgid",type = IdType.AUTO)
    private Integer sgid;//'商品保障id'
    private String sgname;//'保障条款名称'
    private String sgcontent;//'保障条款内容'
    private String sgicon;//'保障条款图标'
}
