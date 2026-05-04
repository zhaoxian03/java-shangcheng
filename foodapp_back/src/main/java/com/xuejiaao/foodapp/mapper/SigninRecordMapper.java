package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.SigninRecord;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface SigninRecordMapper extends BaseMapper<SigninRecord> {

    /**
     * 统计用户签到总天数（从记录统计）
     */
    @Select("SELECT COUNT(*) FROM user_signin WHERE user_id = #{userId}")
    Integer countUserSigninDays(@Param("userId") Integer userId);

    /**
     * 获取用户最后签到日期
     */
    @Select("SELECT signin_date FROM user_signin WHERE user_id = #{userId} ORDER BY signin_date DESC LIMIT 1")
    LocalDate getLastSigninDate(@Param("userId") Integer userId);

    /**
     * 获取用户最后一条签到记录
     */
    @Select("SELECT * FROM user_signin WHERE user_id = #{userId} ORDER BY signin_date DESC LIMIT 1")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "signin_date", property = "signinDate"),
            @Result(column = "signin_time", property = "signinTime"),
            @Result(column = "continuous_days", property = "continuousDays"),
            @Result(column = "total_days", property = "totalDays"),
            @Result(column = "created_at", property = "createdAt")
    })
    SigninRecord getLastSigninRecord(@Param("userId") Integer userId);

    /**
     * 获取用户最近N天签到记录
     */
    @Select("SELECT * FROM user_signin WHERE user_id = #{userId} ORDER BY signin_date DESC LIMIT #{limit}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "signin_date", property = "signinDate"),
            @Result(column = "signin_time", property = "signinTime"),
            @Result(column = "continuous_days", property = "continuousDays"),
            @Result(column = "total_days", property = "totalDays"),
            @Result(column = "created_at", property = "createdAt")
    })
    List<SigninRecord> getRecentSignins(@Param("userId") Integer userId, @Param("limit") Integer limit);

    /**
     * 检查用户今天是否已签到
     */
    @Select("SELECT COUNT(*) FROM user_signin WHERE user_id = #{userId} AND signin_date = CURDATE()")
    Integer checkTodaySigned(@Param("userId") Integer userId);

    /**
     * 获取用户今天签到记录
     */
    @Select("SELECT * FROM user_signin WHERE user_id = #{userId} AND signin_date = CURDATE() LIMIT 1")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "signin_date", property = "signinDate"),
            @Result(column = "signin_time", property = "signinTime"),
            @Result(column = "continuous_days", property = "continuousDays"),
            @Result(column = "total_days", property = "totalDays"),
            @Result(column = "created_at", property = "createdAt")
    })
    SigninRecord getTodaySigninRecord(@Param("userId") Integer userId);

    /**
     * 获取用户最大连续签到天数
     */
    @Select("SELECT MAX(continuous_days) FROM user_signin WHERE user_id = #{userId}")
    Integer getMaxContinuousDays(@Param("userId") Integer userId);

    /**
     * 获取用户本月签到天数
     */
    @Select("SELECT COUNT(*) FROM user_signin WHERE user_id = #{userId} AND YEAR(signin_date) = YEAR(CURDATE()) AND MONTH(signin_date) = MONTH(CURDATE())")
    Integer getCurrentMonthSigninCount(@Param("userId") Integer userId);

    /**
     * 获取用户本月签到日期列表
     */
    @Select("SELECT signin_date FROM user_signin WHERE user_id = #{userId} AND YEAR(signin_date) = YEAR(CURDATE()) AND MONTH(signin_date) = MONTH(CURDATE()) ORDER BY signin_date")
    List<LocalDate> getCurrentMonthSigninDates(@Param("userId") Integer userId);

    /**
     * 获取用户统计数据
     */
    @Select("SELECT " +
            "COALESCE(MAX(total_days), 0) as total_days, " +
            "COALESCE(MAX(continuous_days), 0) as max_continuous_days, " +
            "MAX(signin_date) as last_signin_date, " +
            "COUNT(*) as signin_count " +
            "FROM user_signin WHERE user_id = #{userId}")
    Map<String, Object> getUserSigninStats(@Param("userId") Integer userId);

    /**
     * 自定义插入方法，只插入数据库存在的字段
     */
    @Insert("INSERT INTO user_signin (user_id, signin_date, signin_time, continuous_days, total_days) " +
            "VALUES (#{userId}, #{signinDate}, #{signinTime}, #{continuousDays}, #{totalDays})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSimple(SigninRecord record);
}