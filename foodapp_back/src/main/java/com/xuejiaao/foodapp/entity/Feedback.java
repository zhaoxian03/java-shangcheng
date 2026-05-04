// Feedback.java
package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("feedback")
public class Feedback {
    private Long id;
    private Long userId;           // 用户ID
    private Integer type;          // 反馈类型：1-功能建议, 2-问题反馈, 3-其他
    private String content;        // 反馈内容
    private String contact;        // 联系方式
    private String images;         // 图片URL，逗号分隔
    private Integer status;        // 状态：0-待处理, 1-处理中, 2-已解决
    private String replyContent;   // 回复内容
    private LocalDateTime replyTime;
    private Long adminId;          // 处理管理员ID
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}