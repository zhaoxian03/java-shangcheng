// controller/FileUploadController.java
package com.xuejiaao.foodapp.controller;

import com.xuejiaao.foodapp.common.Result;
import com.xuejiaao.foodapp.service.FileUploadService;
import com.xuejiaao.foodapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins = "*")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private UserService userService;

    /**
     * 上传头像文件
     */
    @PostMapping("/upload-avatar")
    public Result uploadAvatar(@RequestHeader("Authorization") String token,
                               @RequestParam("file") MultipartFile file) {
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

            // 上传文件
            Map<String, Object> result = fileUploadService.uploadAvatar(file, userId);

            Integer code = (Integer) result.get("code");
            String message = (String) result.get("message");

            if (code == 200) {
                return Result.success(message, result.get("data"));
            } else {
                return Result.error(code, message);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传Base64头像
     */
    @PostMapping("/upload-avatar-base64")
    public Result uploadAvatarBase64(@RequestHeader("Authorization") String token,
                                     @RequestBody Map<String, Object> requestData) {
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

            // 获取请求数据
            String base64Image = (String) requestData.get("base64Image");
            String fileName = (String) requestData.get("fileName");

            // 验证数据
            if (base64Image == null || base64Image.isEmpty()) {
                return Result.error(400, "图片数据不能为空");
            }

            // 调用服务上传
            Map<String, Object> result = fileUploadService.uploadAvatarBase64(base64Image, fileName, userId);

            Integer code = (Integer) result.get("code");
            String message = (String) result.get("message");

            if (code == 200) {
                return Result.success(message, result.get("data"));
            } else {
                return Result.error(code, message);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "上传失败: " + e.getMessage());
        }
    }

    /**
     * 测试文件上传
     */
    @PostMapping("/test-upload")
    public Result testUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error(400, "文件不能为空");
            }

            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            String contentType = file.getContentType();

            Map<String, Object> data = new java.util.HashMap<>();
            data.put("fileName", fileName);
            data.put("fileSize", fileSize);
            data.put("contentType", contentType);
            data.put("message", "文件上传测试成功");

            return Result.success("上传测试成功", data);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "测试上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取上传配置信息
     */
    @GetMapping("/upload-config")
    public Result getUploadConfig() {
        try {
            Map<String, Object> config = new java.util.HashMap<>();
            config.put("avatarMaxSize", "2MB");
            config.put("allowedTypes", "jpg, jpeg, png, gif");
            config.put("maxFileSize", "10MB");

            return Result.success("获取配置成功", config);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取配置失败");
        }
    }
}