// FeedbackService.java
package com.xuejiaao.foodapp.service;

import com.xuejiaao.foodapp.entity.Feedback;
import com.xuejiaao.foodapp.mapper.FeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackMapper feedbackMapper;

    /**
     * 提交反馈
     */
    public Map<String, Object> submitFeedback(Long userId, Integer type, String content,
                                              String contact, String images) {
        Map<String, Object> result = new HashMap<>();

        try {
            Feedback feedback = new Feedback();
            feedback.setUserId(userId);
            feedback.setType(type);
            feedback.setContent(content);
            feedback.setContact(contact);
            feedback.setImages(images);
            feedback.setStatus(0); // 待处理
            feedback.setCreateTime(LocalDateTime.now());
            feedback.setUpdateTime(LocalDateTime.now());

            int rows = feedbackMapper.insert(feedback);

            if (rows > 0) {
                result.put("code", 200);
                result.put("message", "提交成功");
                result.put("feedbackId", feedback.getId());
            } else {
                result.put("code", 500);
                result.put("message", "提交失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "服务器异常");
        }

        return result;
    }

    /**
     * 获取用户反馈列表
     */
    public Map<String, Object> getUserFeedbacks(Long userId, Integer page, Integer pageSize) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 计算分页
            int offset = (page - 1) * pageSize;

            // 查询数据
            List<Feedback> feedbacks = feedbackMapper.selectPageByUserId(userId, offset, pageSize);

            // 查询总数
            int total = feedbackMapper.countByUserId(userId);

            // 计算总页数
            int totalPages = (int) Math.ceil((double) total / pageSize);

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", feedbacks);
            result.put("total", total);
            result.put("totalPages", totalPages);
            result.put("currentPage", page);
            result.put("pageSize", pageSize);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "查询失败");
        }

        return result;
    }

    /**
     * 获取反馈详情
     */
    public Map<String, Object> getFeedbackDetail(Long id, Long userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            Feedback feedback = feedbackMapper.selectById(id);

            if (feedback == null) {
                result.put("code", 404);
                result.put("message", "反馈不存在");
                return result;
            }

            // 验证用户权限
            if (!feedback.getUserId().equals(userId)) {
                result.put("code", 403);
                result.put("message", "无权查看此反馈");
                return result;
            }

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", feedback);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "查询失败");
        }

        return result;
    }

    /**
     * 管理员回复反馈
     */
    public Map<String, Object> replyFeedback(Long id, String replyContent, Long adminId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 检查反馈是否存在
            Feedback feedback = feedbackMapper.selectById(id);
            if (feedback == null) {
                result.put("code", 404);
                result.put("message", "反馈不存在");
                return result;
            }

            int rows = feedbackMapper.replyFeedback(id, replyContent, adminId);

            if (rows > 0) {
                result.put("code", 200);
                result.put("message", "回复成功");
            } else {
                result.put("code", 500);
                result.put("message", "回复失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "服务器异常");
        }

        return result;
    }

    /**
     * 删除反馈
     */
    public Map<String, Object> deleteFeedback(Long id, Long userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            int rows = feedbackMapper.deleteByIdAndUserId(id, userId);

            if (rows > 0) {
                result.put("code", 200);
                result.put("message", "删除成功");
            } else {
                result.put("code", 404);
                result.put("message", "反馈不存在或无权删除");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "删除失败");
        }

        return result;
    }
}