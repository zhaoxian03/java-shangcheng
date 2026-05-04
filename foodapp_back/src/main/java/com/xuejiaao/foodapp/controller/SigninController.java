package com.xuejiaao.foodapp.controller;

import com.xuejiaao.foodapp.service.SigninService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/signin")
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        exposedHeaders = "*",
        maxAge = 3600
)
@Slf4j
public class SigninController {

    @Autowired
    private SigninService signinService;

    /**
     * OPTIONS 请求处理器
     */
    @RequestMapping(method = RequestMethod.OPTIONS, value = "/**")
    public Map<String, Object> handleOptions() {
        log.info("处理 CORS 预检请求");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "CORS preflight OK");
        return result;
    }

    /**
     * 获取用户签到信息
     */
    @GetMapping("/info/{userId}")
    public Map<String, Object> getSigninInfo(@PathVariable Integer userId) {
        log.info("获取签到信息 - 用户ID: {}", userId);

        if (userId == null || userId <= 0) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 400);
            errorResult.put("message", "用户ID不能为空且必须大于0");
            return errorResult;
        }

        try {
            Map<String, Object> result = signinService.getSigninInfo(userId);
            return result;
        } catch (Exception e) {
            log.error("获取签到信息异常", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 500);
            errorResult.put("message", "服务器异常: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 执行签到
     */
    @PostMapping("")
    public Map<String, Object> signin(@RequestBody Map<String, Object> request) {
        log.info("执行签到 - 请求参数: {}", request);

        Integer userId = extractUserId(request);
        if (userId == null || userId <= 0) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 400);
            errorResult.put("message", "用户ID不能为空且必须大于0");
            return errorResult;
        }

        try {
            Map<String, Object> result = signinService.signin(userId);
            return result;
        } catch (Exception e) {
            log.error("执行签到异常", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 500);
            errorResult.put("message", "服务器异常: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 获取用户签到历史
     */
    @GetMapping("/history/{userId}")
    public Map<String, Object> getSigninHistory(
            @PathVariable Integer userId,
            @RequestParam(required = false, defaultValue = "30") Integer limit) {

        log.info("获取签到历史 - 用户ID: {}, 限制: {}", userId, limit);

        if (userId == null || userId <= 0) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 400);
            errorResult.put("message", "用户ID不能为空且必须大于0");
            return errorResult;
        }

        try {
            Map<String, Object> result = signinService.getSigninHistory(userId, limit);
            return result;
        } catch (Exception e) {
            log.error("获取签到历史异常", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 500);
            errorResult.put("message", "服务器异常: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 获取用户本月签到日历
     */
    @GetMapping("/calendar/{userId}")
    public Map<String, Object> getMonthCalendar(
            @PathVariable Integer userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        log.info("获取月日历 - 用户ID: {}, 年: {}, 月: {}", userId, year, month);

        if (userId == null || userId <= 0) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 400);
            errorResult.put("message", "用户ID不能为空且必须大于0");
            return errorResult;
        }

        try {
            Map<String, Object> result = signinService.getMonthCalendar(userId, year, month);
            return result;
        } catch (Exception e) {
            log.error("获取月日历异常", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 500);
            errorResult.put("message", "服务器异常: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 获取用户签到统计数据
     */
    @GetMapping("/stats/{userId}")
    public Map<String, Object> getSigninStats(@PathVariable Integer userId) {
        log.info("获取签到统计 - 用户ID: {}", userId);

        if (userId == null || userId <= 0) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 400);
            errorResult.put("message", "用户ID不能为空且必须大于0");
            return errorResult;
        }

        try {
            Map<String, Object> result = signinService.getSigninStats(userId);
            return result;
        } catch (Exception e) {
            log.error("获取签到统计异常", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 500);
            errorResult.put("message", "服务器异常: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 检查用户今天是否已签到
     */
    @GetMapping("/check/{userId}")
    public Map<String, Object> checkTodaySigned(@PathVariable Integer userId) {
        log.info("检查今日签到 - 用户ID: {}", userId);

        if (userId == null || userId <= 0) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 400);
            errorResult.put("message", "用户ID不能为空且必须大于0");
            return errorResult;
        }

        try {
            Map<String, Object> result = signinService.checkTodaySigned(userId);
            return result;
        } catch (Exception e) {
            log.error("检查今日签到异常", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 500);
            errorResult.put("message", "服务器异常: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 手动更新用户签到统计数据（管理员接口）
     */
    @PostMapping("/admin/update-stats/{userId}")
    public Map<String, Object> updateUserStats(@PathVariable Integer userId) {
        log.info("手动更新用户统计 - 用户ID: {}", userId);

        if (userId == null || userId <= 0) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 400);
            errorResult.put("message", "用户ID不能为空且必须大于0");
            return errorResult;
        }

        try {
            Map<String, Object> result = signinService.updateUserSigninStats(userId);
            return result;
        } catch (Exception e) {
            log.error("手动更新用户统计异常", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 500);
            errorResult.put("message", "服务器异常: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        log.info("健康检查");

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "服务正常");
        result.put("data", Map.of(
                "status", "UP",
                "timestamp", System.currentTimeMillis(),
                "service", "signin-service"
        ));
        return result;
    }

    /**
     * 数据库连接测试接口
     */
    @GetMapping("/test/db")
    public Map<String, Object> testDatabaseConnection() {
        log.info("数据库连接测试");

        try {
            Map<String, Object> result = signinService.testDatabaseConnection();
            return result;
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 500);
            errorResult.put("message", "数据库连接异常: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 提取用户ID
     */
    private Integer extractUserId(Map<String, Object> request) {
        if (request == null || request.get("userId") == null) {
            return null;
        }

        Object userIdObj = request.get("userId");
        if (userIdObj instanceof Integer) {
            return (Integer) userIdObj;
        } else if (userIdObj instanceof Long) {
            return ((Long) userIdObj).intValue();
        } else if (userIdObj instanceof String) {
            try {
                return Integer.parseInt((String) userIdObj);
            } catch (NumberFormatException e) {
                log.error("用户ID格式错误: {}", userIdObj);
                return null;
            }
        } else {
            log.error("用户ID类型不支持: {}", userIdObj.getClass());
            return null;
        }
    }
}