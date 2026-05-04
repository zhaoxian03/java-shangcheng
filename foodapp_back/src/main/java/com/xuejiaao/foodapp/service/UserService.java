package com.xuejiaao.foodapp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuejiaao.foodapp.entity.User;
import com.xuejiaao.foodapp.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileUploadService fileUploadService;

    // Token前缀
    private static final String TOKEN_PREFIX = "DOUYIN_";

    // 模拟Token存储（实际应该用Redis）
    private Map<String, Integer> tokenStore = new HashMap<>();

    /**
     * 用户登录 - 返回登录结果数据，不包装Result
     */
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();

        // 验证参数
        if (username == null || username.trim().isEmpty()) {
            result.put("error", "用户名不能为空");
            return result;
        }

        if (password == null || password.trim().isEmpty()) {
            result.put("error", "密码不能为空");
            return result;
        }

        // 查询用户（注意：直接查询，不需要自定义SQL）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .eq("password", password);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            result.put("error", "用户名或密码错误");
        } else {
            // 生成token
            String token = generateToken(user.getCid());

            // 存储token（模拟）
            tokenStore.put(token, user.getCid());

            // 准备返回的用户信息（不包含密码）
            Map<String, Object> userInfo = getUserInfoMap(user);

            result.put("token", token);
            result.put("user", userInfo);
        }

        return result;
    }

    /**
     * 获取用户信息
     */
    public Map<String, Object> getUserInfo(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }
        return getUserInfoMap(user);
    }

    /**
     * 更新用户信息
     */
    public boolean updateUserInfo(Integer userId, Map<String, String> userData) {
        // 验证用户是否存在
        User existingUser = userMapper.selectById(userId);
        if (existingUser == null) {
            return false;
        }

        // ✅ 修复：先检查username，再检查name（兼容两种）
        String username = userData.get("username");
        if (username == null || username.trim().isEmpty()) {
            // 如果username没有，尝试name字段（兼容旧版本）
            username = userData.get("name");
        }

        String phone = userData.get("phone");

        if (username == null || username.trim().isEmpty()) {
            System.out.println("❌ 更新失败：用户名不能为空，收到的数据: " + userData);
            return false;
        }

        if (phone == null || phone.trim().isEmpty()) {
            System.out.println("❌ 更新失败：手机号不能为空，收到的数据: " + userData);
            return false;
        }

        // 手机号格式验证
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            System.out.println("❌ 更新失败：手机号格式不正确: " + phone);
            return false;
        }

        // 邮箱格式验证（可选）
        String email = userData.get("email");
        if (email != null && !email.isEmpty()) {
            if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                System.out.println("❌ 更新失败：邮箱格式不正确: " + email);
                return false;
            }
        }

        // 检查用户名是否被其他用户使用
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).ne("cid", userId);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            System.out.println("❌ 更新失败：用户名已被使用: " + username);
            return false;
        }

        // 更新用户信息
        User updateUser = new User();
        updateUser.setCid(userId);
        updateUser.setUsername(username);
        updateUser.setPhone(phone);
        updateUser.setEmail(email != null ? email : existingUser.getEmail());
        updateUser.setAddress(userData.getOrDefault("address", existingUser.getAddress()));
        updateUser.setAvatarUrl(userData.getOrDefault("avatarUrl", existingUser.getAvatarUrl()));
        updateUser.setUpdateTime(LocalDateTime.now());

        // 保持密码不变
        updateUser.setPassword(existingUser.getPassword());
        updateUser.setRegisterTime(existingUser.getRegisterTime());

        System.out.println("✅ 准备更新用户: " + updateUser);

        int rows = userMapper.updateById(updateUser);
        System.out.println("✅ 更新结果，影响行数: " + rows);

        return rows > 0;
    }

    /**
     * 用户注册
     */
    public Integer register(User user) {
        // 验证必要字段
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return -1; // 用户名不能为空
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return -2; // 密码不能为空
        }

        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            return -3; // 手机号不能为空
        }

        // 手机号格式验证
        if (!user.getPhone().matches("^1[3-9]\\d{9}$")) {
            return -4; // 手机号格式不正确
        }

        // 检查用户名是否已存在
        QueryWrapper<User> usernameQuery = new QueryWrapper<>();
        usernameQuery.eq("username", user.getUsername());
        Long usernameCount = userMapper.selectCount(usernameQuery);
        if (usernameCount > 0) {
            return -5; // 用户名已存在
        }

        // 检查手机号是否已存在
        QueryWrapper<User> phoneQuery = new QueryWrapper<>();
        phoneQuery.eq("phone", user.getPhone());
        Long phoneCount = userMapper.selectCount(phoneQuery);
        if (phoneCount > 0) {
            return -6; // 手机号已存在
        }

        // 设置默认值
        user.setRegisterTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 插入用户
        int rows = userMapper.insert(user);
        return rows > 0 ? user.getCid() : 0;
    }

    /**
     * 生成Token
     */
    private String generateToken(Integer userId) {
        String token = TOKEN_PREFIX + UUID.randomUUID().toString().replace("-", "")
                + "_" + userId + "_" + System.currentTimeMillis();
        return token;
    }

    /**
     * 验证Token
     */
    public Integer validateToken(String token) {
        if (token == null) {
            return null;
        }
        
        // 移除Bearer前缀（如果存在）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        if (!token.startsWith(TOKEN_PREFIX)) {
            return null;
        }

        // 从存储中获取
        return tokenStore.get(token);
    }

    /**
     * 修改密码
     */
    public boolean changePassword(Integer userId, String oldPassword, String newPassword) {
        // 验证参数
        if (userId == null || oldPassword == null || newPassword == null) {
            return false;
        }

        // 新密码格式验证
        if (newPassword.length() < 6 || newPassword.length() > 20) {
            return false;
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
            return false;
        }

        // 获取用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        // 验证旧密码（注意：这里使用明文比较，建议使用加密比较）
        if (!user.getPassword().equals(oldPassword)) {
            return false;
        }

        // 验证新旧密码不能相同
        if (oldPassword.equals(newPassword)) {
            return false;
        }

        // 更新密码
        User updateUser = new User();
        updateUser.setCid(userId);
        updateUser.setPassword(newPassword);
        updateUser.setUpdateTime(LocalDateTime.now());

        int rows = userMapper.updateById(updateUser);
        return rows > 0;
    }

    /**
     * 将User对象转换为Map（隐藏敏感信息）
     */
    private Map<String, Object> getUserInfoMap(User user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("cid", user.getCid());
        userInfo.put("id", user.getCid());  // 为了兼容，保留id字段
        userInfo.put("username", user.getUsername());  // 注意：前端期待的是"name"字段
        userInfo.put("name", user.getUsername());      // 为了兼容，两个都放
        userInfo.put("phone", user.getPhone());
        userInfo.put("email", user.getEmail());
        userInfo.put("address", user.getAddress());
        userInfo.put("avatarUrl", user.getAvatarUrl());
        userInfo.put("avatar", user.getAvatarUrl());
        userInfo.put("registerTime", user.getRegisterTime());
        return userInfo;
    }

    /**
     * 根据Token获取用户信息
     */
    public Map<String, Object> getUserInfoByToken(String token) {
        Integer userId = validateToken(token);
        if (userId == null) {
            return null;
        }
        return getUserInfo(userId);
    }

    // ==================== 新增的头像上传相关方法 ====================

    /**
     * 上传头像并更新用户信息（旧版接口调用）
     */
    public Map<String, Object> uploadUserAvatar(Integer userId, String base64Image, String fileName) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 上传文件
            Map<String, Object> uploadResult = fileUploadService.uploadAvatarBase64(base64Image, fileName, userId);

            if (!uploadResult.get("code").equals(200)) {
                return uploadResult;
            }

            // 获取上传后的URL
            Map<String, Object> data = (Map<String, Object>) uploadResult.get("data");
            String avatarUrl = (String) data.get("avatarUrl");

            // 更新用户信息
            User user = userMapper.selectById(userId);
            if (user == null) {
                result.put("code", 404);
                result.put("message", "用户不存在");
                return result;
            }

            user.setAvatarUrl(avatarUrl);
            user.setUpdateTime(LocalDateTime.now());

            int updateResult = userMapper.updateById(user);
            if (updateResult > 0) {
                result.put("code", 200);
                result.put("message", "头像更新成功");
                result.put("data", data);
            } else {
                result.put("code", 500);
                result.put("message", "更新数据库失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "上传失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 上传用户头像（简单Base64版 - Controller直接调用）
     */
    public boolean uploadAvatar(Integer userId, String avatarBase64) {
        try {
            System.out.println("收到用户头像上传请求，用户ID：" + userId + "，数据长度：" + avatarBase64.length());

            // 解码Base64图片
            String base64Data = avatarBase64;
            if (base64Data.contains(",")) {
                base64Data = base64Data.split(",")[1]; // 去掉 data:image/png;base64, 前缀
            }

            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // 创建上传目录
            String uploadDir = "uploads/avatars/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成文件名并保存
            String fileName = userId + "_" + System.currentTimeMillis() + ".jpg";
            String filePath = uploadDir + fileName;

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(imageBytes);
            }

            // 更新用户表中的头像路径
            Map<String, String> userData = new HashMap<>();
            userData.put("avatarUrl", "/" + filePath);
            boolean updateResult = updateUserInfo(userId, userData);

            if (updateResult) {
                System.out.println("用户头像上传成功，用户ID：" + userId + "，文件路径：" + filePath);
                return true;
            } else {
                System.out.println("用户头像更新失败，用户ID：" + userId);
                return false;
            }

        } catch (Exception e) {
            System.err.println("用户头像上传失败，用户ID：" + userId + "，错误：" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取用户头像信息
     */
    public Map<String, Object> getUserAvatarInfo(Integer userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                result.put("code", 404);
                result.put("message", "用户不存在");
                return result;
            }

            String avatarUrl = user.getAvatarUrl();
            boolean hasAvatar = avatarUrl != null && !avatarUrl.isEmpty();

            Map<String, Object> data = new HashMap<>();
            data.put("avatarUrl", avatarUrl);
            data.put("hasAvatar", hasAvatar);
            data.put("userId", userId);
            data.put("avatarType", getAvatarType(avatarUrl));

            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", data);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "获取失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 根据ID获取用户信息
     */
    public Map<String, Object> getUserInfoById(Integer userId) {
        try {
            User user = userMapper.selectById(userId);
            if (user != null) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", user.getCid());
                userInfo.put("username", user.getUsername());
                userInfo.put("phone", user.getPhone());
                userInfo.put("avatarUrl", user.getAvatarUrl());
                userInfo.put("email", user.getEmail());
                userInfo.put("address", user.getAddress());
                userInfo.put("registerTime", user.getRegisterTime());
                return userInfo;
            }
            return null;
        } catch (Exception e) {
            System.err.println("getUserInfoById失败，用户ID：" + userId + "，错误：" + e.getMessage());
            return null;
        }
    }

    /**
     * 判断头像类型
     */
    private String getAvatarType(String avatarUrl) {
        if (avatarUrl == null || avatarUrl.isEmpty()) {
            return "default";
        }
        if (avatarUrl.startsWith("http")) {
            return "remote";
        }
        if (avatarUrl.startsWith("data:image")) {
            return "base64";
        }
        if (avatarUrl.contains("uploads/avatars")) {
            return "uploaded";
        }
        return "other";
    }
}