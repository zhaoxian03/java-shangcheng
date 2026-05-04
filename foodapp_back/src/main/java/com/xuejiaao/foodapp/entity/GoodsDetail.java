package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("goods_detail")
public class GoodsDetail implements Serializable {
    @TableId(value = "gdid",type = IdType.AUTO)
    private Long gdid;//'商品详细id'
    private Long gid;//'商品id(父id)'
    private Integer stockNum;//'库存量'
    private Integer sellNum;//'售卖量'
    private Double originalPrice;//'原价'
    private Double specialPrice;//'特价'
    private String gdpic;//'商品图片'
    private String attributeName1;//'属性名称1'
    private String attributeValue1;//'属性值1'
    private String attributeName2;//'属性名称2'
    private String attributeValue2;//'属性值2'
    private String attributeName3;//'属性名称3'
    private String attributeValue3;//'属性值3'
    private String attributeName4;//'属性名称4'
    private String attributeValue4;//'属性值4'
    private String attributeName5;//'属性名称5'
    private String attributeValue5;//'属性值5'
}
