package com.xuejiaao.foodapp.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SigninRecord {
    private Long id;
    private Integer userId;
    private LocalDate signinDate;
    private LocalDateTime signinTime;
    private Integer continuousDays = 1;
    private Integer totalDays = 1;
    private LocalDateTime createdAt;

    // 删除所有MyBatis Plus注解，只保留这些数据库实际存在的字段

    // 构造方法
    public SigninRecord() {}

    /**
     * 创建默认的签到记录
     */
    public static SigninRecord createDefault(Integer userId) {
        SigninRecord record = new SigninRecord();
        record.setUserId(userId);
        record.setSigninDate(LocalDate.now());
        record.setSigninTime(LocalDateTime.now());
        record.setContinuousDays(1);
        record.setTotalDays(1);
        // 不设置createdAt，让数据库自动处理或设置为null
        return record;
    }
}