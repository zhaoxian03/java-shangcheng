
package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("users")  // 指定表名
public class User {

    @TableId(type = IdType.AUTO)  // 自增主键
    private Integer cid;

    private String username;

    private String password;

    private String phone;
    private String email;
    private String address;

    @TableField("avatar_url")  // 数据库字段名
    private String avatarUrl;

    @TableField("register_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerTime;

    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    // 构造方法
    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}