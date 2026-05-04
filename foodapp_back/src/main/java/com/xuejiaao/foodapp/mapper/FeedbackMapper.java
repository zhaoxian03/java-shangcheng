// FeedbackMapper.java
package com.xuejiaao.foodapp.mapper;

import com.xuejiaao.foodapp.entity.Feedback;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FeedbackMapper {

    // 插入反馈
    @Insert("INSERT INTO feedback (user_id, type, content, contact, images, status, create_time, update_time) " +
            "VALUES (#{userId}, #{type}, #{content}, #{contact}, #{images}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Feedback feedback);

    // 根据ID查询
    @Select("SELECT * FROM feedback WHERE id = #{id}")
    Feedback selectById(Long id);

    // 根据用户ID查询
    @Select("SELECT * FROM feedback WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Feedback> selectByUserId(Long userId);

    // 分页查询用户反馈
    @Select("SELECT * FROM feedback WHERE user_id = #{userId} ORDER BY create_time DESC LIMIT #{offset}, #{pageSize}")
    List<Feedback> selectPageByUserId(@Param("userId") Long userId,
                                      @Param("offset") Integer offset,
                                      @Param("pageSize") Integer pageSize);

    // 统计用户反馈数量
    @Select("SELECT COUNT(*) FROM feedback WHERE user_id = #{userId}")
    int countByUserId(Long userId);

    // 更新反馈状态
    @Update("UPDATE feedback SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    // 管理员回复
    @Update("UPDATE feedback SET reply_content = #{replyContent}, reply_time = NOW(), " +
            "admin_id = #{adminId}, status = 2, update_time = NOW() WHERE id = #{id}")
    int replyFeedback(@Param("id") Long id,
                      @Param("replyContent") String replyContent,
                      @Param("adminId") Long adminId);

    // 删除反馈（可选）
    @Delete("DELETE FROM feedback WHERE id = #{id} AND user_id = #{userId}")
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}