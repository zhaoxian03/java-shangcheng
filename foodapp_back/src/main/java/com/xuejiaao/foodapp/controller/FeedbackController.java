// FeedbackController.java
package com.xuejiaao.foodapp.controller;

import com.xuejiaao.foodapp.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    /**
     * 提交反馈
     * POST /api/feedback/submit
     * 请求体：{
     *   "type": 1,
     *   "content": "反馈内容...",
     *   "contact": "13800138000",
     *   "images": "url1,url2,url3"
     * }
     */
    @PostMapping("/submit")
    public Map<String, Object> submitFeedback(
            @RequestBody Map<String, Object> requestData,
            @RequestHeader(value = "Authorization", required = false) String token) {

        // 验证token和获取用户ID（需要你的用户认证逻辑）
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Map.of("code", 401, "message", "请先登录");
        }

        // 获取请求参数
        Integer type = (Integer) requestData.get("type");
        String content = (String) requestData.get("content");
        String contact = (String) requestData.get("contact");
        String images = (String) requestData.get("images");

        // 参数验证
        if (type == null) {
            return Map.of("code", 400, "message", "反馈类型不能为空");
        }
        if (content == null || content.trim().isEmpty()) {
            return Map.of("code", 400, "message", "反馈内容不能为空");
        }
        if (content.trim().length() < 10) {
            return Map.of("code", 400, "message", "反馈内容至少10个字");
        }

        // 调用Service
        return feedbackService.submitFeedback(userId, type, content.trim(), contact, images);
    }

    /**
     * 获取我的反馈列表
     * GET /api/feedback/my
     * 参数：page=1&pageSize=10
     */
    @GetMapping("/my")
    public Map<String, Object> getMyFeedbacks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestHeader(value = "Authorization", required = false) String token) {

        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Map.of("code", 401, "message", "请先登录");
        }

        // 分页参数验证
        if (page < 1) page = 1;
        if (pageSize < 1 || pageSize > 100) pageSize = 10;

        return feedbackService.getUserFeedbacks(userId, page, pageSize);
    }

    /**
     * 获取反馈详情
     * GET /api/feedback/detail/{id}
     */
    @GetMapping("/detail/{id}")
    public Map<String, Object> getFeedbackDetail(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token) {

        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Map.of("code", 401, "message", "请先登录");
        }

        return feedbackService.getFeedbackDetail(id, userId);
    }

    /**
     * 删除反馈
     * DELETE /api/feedback/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteFeedback(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token) {

        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Map.of("code", 401, "message", "请先登录");
        }

        return feedbackService.deleteFeedback(id, userId);
    }

    /**
     * 管理员回复反馈（需要管理员权限）
     * POST /api/feedback/admin/reply/{id}
     * 请求体：{
     *   "replyContent": "回复内容...",
     *   "adminId": 1
     * }
     */
    @PostMapping("/admin/reply/{id}")
    public Map<String, Object> adminReply(
            @PathVariable Long id,
            @RequestBody Map<String, Object> requestData,
            @RequestHeader(value = "Authorization", required = false) String token) {

        // 需要管理员验证逻辑
        Long adminId = getAdminIdFromToken(token);
        if (adminId == null) {
            return Map.of("code", 403, "message", "无权限操作");
        }

        String replyContent = (String) requestData.get("replyContent");
        if (replyContent == null || replyContent.trim().isEmpty()) {
            return Map.of("code", 400, "message", "回复内容不能为空");
        }

        return feedbackService.replyFeedback(id, replyContent.trim(), adminId);
    }

    /**
     * 管理员获取反馈列表
     * GET /api/feedback/admin/list
     * 参数：page=1&pageSize=10&status=0
     */
    @GetMapping("/admin/list")
    public Map<String, Object> adminList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status,
            @RequestHeader(value = "Authorization", required = false) String token) {

        // 需要管理员验证逻辑
        if (!isAdmin(token)) {
            return Map.of("code", 403, "message", "无权限操作");
        }

        // 这里可以扩展查询所有反馈，支持按状态筛选
        // 需要扩展Service方法
        return Map.of("code", 200, "message", "开发中...");
    }

    // ================== 工具方法（需要你根据实际情况实现）==================

    /**
     * 从token中获取用户ID（需要根据你的认证系统实现）
     */
    private Long getUserIdFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        try {
            // 这里需要解析JWT token，获取用户ID
            // 示例：return JwtUtil.getUserIdFromToken(token.substring(7));
            return 1L; // 临时返回固定值，用于测试
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从token中获取管理员ID
     */
    private Long getAdminIdFromToken(String token) {
        // 实现管理员token验证逻辑
        return getUserIdFromToken(token); // 简化处理
    }

    /**
     * 验证是否是管理员
     */
    private boolean isAdmin(String token) {
        // 实现管理员权限验证逻辑
        return true; // 简化处理
    }
}