package com.xuejiaao.foodapp.controller;

import com.xuejiaao.foodapp.common.Result;
import com.xuejiaao.foodapp.entity.User;
import com.xuejiaao.foodapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", allowCredentials = "false")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        Map<String, Object> loginResult = userService.login(username, password);

        if (loginResult.containsKey("error")) {
            return Result.error(401, loginResult.get("error").toString());
        }

        // 构建返回数据
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("token", loginResult.get("token"));
        responseData.put("user", loginResult.get("user"));
        responseData.put("userId", ((Map)loginResult.get("user")).get("id"));

        return Result.success("登录成功", responseData);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        Integer result = userService.register(user);

        switch (result) {
            case -1:
                return Result.error(400, "用户名不能为空");
            case -2:
                return Result.error(400, "密码不能为空");
            case -3:
                return Result.error(400, "手机号不能为空");
            case -4:
                return Result.error(400, "手机号格式不正确");
            case -5:
                return Result.error(400, "用户名已存在");
            case -6:
                return Result.error(400, "手机号已存在");
            case 0:
                return Result.error(500, "注册失败");
            default:
                Map<String, Object> data = new HashMap<>();
                data.put("userId", result);
                return Result.success("注册成功", data);
        }
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/profile")
    public Result getProfile(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error(401, "未授权");
        }

        String realToken = token.substring(7);
        Map<String, Object> userInfo = userService.getUserInfoByToken(realToken);

        if (userInfo == null) {
            return Result.error(401, "Token无效或已过期");
        }

        return Result.success("获取成功", userInfo);
    }

    /**
     * 验证Token
     */
    @GetMapping("/validate")
    public Result validateToken(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error(401, "Token不能为空");
        }

        String realToken = token.substring(7);
        Integer userId = userService.validateToken(realToken);

        if (userId == null) {
            return Result.error(401, "Token无效");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("valid", true);
        data.put("userId", userId);
        data.put("cid", userId);  // 为了兼容前端代码，添加cid字段
        return Result.success("Token有效", data);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    public Result updateUserInfo(@RequestHeader("Authorization") String token,
                                 @RequestBody Map<String, String> userData) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error(401, "未授权");
        }

        String realToken = token.substring(7);
        Integer userId = userService.validateToken(realToken);

        if (userId == null) {
            return Result.error(401, "Token无效或已过期");
        }

        boolean success = userService.updateUserInfo(userId, userData);

        if (!success) {
            return Result.error(400, "更新失败，请检查输入信息");
        }

        return Result.success("更新成功");
    }

    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    public Result changePassword(@RequestHeader("Authorization") String token,
                                 @RequestBody Map<String, String> params) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error(401, "未授权");
        }

        String realToken = token.substring(7);
        Integer userId = userService.validateToken(realToken);

        if (userId == null) {
            return Result.error(401, "Token无效或已过期");
        }

        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");

        // 验证参数
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            return Result.error(400, "原密码不能为空");
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Result.error(400, "新密码不能为空");
        }

        // 验证新密码格式
        if (newPassword.length() < 6 || newPassword.length() > 20) {
            return Result.error(400, "密码长度需6-20位");
        }

        // 验证新密码包含字母和数字
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : newPassword.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        if (!hasLetter || !hasDigit) {
            return Result.error(400, "密码需包含字母和数字");
        }

        // 验证新旧密码不能相同
        if (oldPassword.equals(newPassword)) {
            return Result.error(400, "新密码不能与原密码相同");
        }

        // 调用服务层修改密码
        boolean success = userService.changePassword(userId, oldPassword, newPassword);

        if (!success) {
            return Result.error(400, "原密码错误或修改失败");
        }

        return Result.success("密码修改成功");
    }

    /**
     * 健康检查接口（测试用）
     */
    @GetMapping("/health")
    public Result healthCheck() {
        return Result.success("用户服务正常运行");
    }

    /**
     * 用户头像上传接口（一体化）- 修复版
     */
    @PostMapping("/upload-avatar")
    public Result uploadAvatarV2(@RequestHeader("Authorization") String token,
                                 @RequestBody Map<String, Object> requestData) {
        try {
            System.out.println("=== 头像上传开始 ===");

            // 验证Token
            if (token == null || !token.startsWith("Bearer ")) {
                System.out.println("❌ Token格式错误");
                return Result.error(401, "未授权");
            }

            String realToken = token.substring(7);
            Integer userId = userService.validateToken(realToken);

            if (userId == null) {
                System.out.println("❌ Token无效: " + realToken);
                return Result.error(401, "Token无效或已过期");
            }

            System.out.println("✅ 用户验证通过: userId=" + userId);

            // 获取Base64图片数据
            String base64Image = (String) requestData.get("base64Image");
            String fileName = (String) requestData.get("fileName");

            System.out.println("📊 收到数据: fileName=" + fileName + ", base64长度=" + (base64Image != null ? base64Image.length() : 0));

            if (base64Image == null || base64Image.isEmpty()) {
                System.out.println("❌ 图片数据为空");
                return Result.error(400, "图片数据不能为空");
            }

            // 调用服务上传并更新
            Map<String, Object> result = userService.uploadUserAvatar(userId, base64Image, fileName);

            System.out.println("📥 服务层返回: " + result);

            Integer code = (Integer) result.get("code");
            String message = (String) result.get("message");

            if (code == 200) {
                System.out.println("✅ 头像上传成功");
                return Result.success(message, result.get("data"));
            } else {
                System.out.println("❌ 头像上传失败: " + message);
                return Result.error(code, message);
            }

        } catch (Exception e) {
            System.err.println("💥 头像上传异常: " + e.getMessage());
            e.printStackTrace();
            return Result.error(500, "头像上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传用户头像（兼容旧版）- 修复确保返回标准格式
     */
    @PostMapping("/uploadAvatar")
    public Result uploadAvatar(@RequestHeader("Authorization") String token,
                               @RequestBody Map<String, String> params) {
        try {
            System.out.println("=== uploadAvatar接口调用开始 ===");
            System.out.println("🔍 请求参数keys: " + params.keySet());

            if (token == null || !token.startsWith("Bearer ")) {
                System.out.println("❌ Token格式错误");
                return Result.error(401, "未授权");
            }

            String realToken = token.substring(7);
            System.out.println("🔍 实际Token: " + realToken.substring(0, Math.min(30, realToken.length())) + "...");

            Integer userId = userService.validateToken(realToken);

            if (userId == null) {
                System.out.println("❌ Token无效");
                return Result.error(401, "Token无效或已过期");
            }

            System.out.println("✅ 用户验证成功: userId=" + userId);

            String avatarBase64 = params.get("avatarBase64");
            System.out.println("📊 Base64数据长度: " + (avatarBase64 != null ? avatarBase64.length() : 0));

            if (avatarBase64 != null && avatarBase64.length() > 30) {
                System.out.println("🔍 Base64前30字符: " + avatarBase64.substring(0, 30) + "...");
            }

            if (avatarBase64 == null || avatarBase64.trim().isEmpty()) {
                System.out.println("❌ Base64数据为空");
                return Result.error(400, "头像数据不能为空");
            }

            System.out.println("🔄 调用服务层上传头像...");
            boolean success = userService.uploadAvatar(userId, avatarBase64);

            System.out.println("📥 服务层返回结果: " + success);

            if (!success) {
                System.out.println("❌ 服务层上传失败");
                return Result.error(500, "头像上传失败");
            }

            // ✅ 重要：必须返回标准格式
            Map<String, Object> data = new HashMap<>();
            String avatarUrl = "/uploads/avatars/" + userId + "_" + System.currentTimeMillis() + ".jpg";
            data.put("avatarUrl", avatarUrl);

            System.out.println("✅ 头像上传成功，返回URL: " + avatarUrl);
            System.out.println("=== uploadAvatar接口调用结束 ===");

            return Result.success("头像上传成功", data);

        } catch (Exception e) {
            System.err.println("💥 上传头像异常: " + e.getMessage());
            e.printStackTrace();
            return Result.error(500, "头像上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户头像信息
     */
    @GetMapping("/avatar-info")
    public Result getAvatarInfo(@RequestHeader("Authorization") String token) {
        try {
            // 验证Token
            if (token == null || !token.startsWith("Bearer ")) {
                return Result.error(401, "未授权");
            }

            String realToken = token.substring(7);
            Integer userId = userService.validateToken(realToken);

            if (userId == null) {
                return Result.error(401, "Token无效或已过期");
            }

            Map<String, Object> result = userService.getUserAvatarInfo(userId);

            Integer code = (Integer) result.get("code");
            String message = (String) result.get("message");

            if (code == 200) {
                return Result.success(message, result.get("data"));
            } else {
                return Result.error(code, message);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取头像信息失败: " + e.getMessage());
        }
    }
}