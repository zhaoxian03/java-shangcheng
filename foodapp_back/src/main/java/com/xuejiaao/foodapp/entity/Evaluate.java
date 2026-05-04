package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("evaluate")
public class Evaluate implements Serializable {
    @TableId(value = "evid", type = IdType.AUTO)
    private Long evid;//'评价id'
    private Long gid;//'商品id'
    private Long gdid;//'商品详细id'
    private String etype;//'评价类型逗号分隔：1.回头客 2.是正品 3.口感好 4.会回购 '
    private String content;//'评价内容'
    private String pic;//'评价的图片，多个以;分隔'
    private Long cid;//'客户id'
    private Long evidp;//'父id'
}
