// service/FileUploadService.java
package com.xuejiaao.foodapp.service;

import com.xuejiaao.foodapp.config.UploadConfig;
import com.xuejiaao.foodapp.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileUploadService {

    @Autowired
    private UploadConfig uploadConfig;

    /**
     * 上传头像文件
     */
    public Map<String, Object> uploadAvatar(MultipartFile file, Integer userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                result.put("code", 400);
                result.put("message", "文件不能为空");
                return result;
            }

            // 验证文件大小
            if (file.getSize() > uploadConfig.getAvatarMaxSize()) {
                result.put("code", 400);
                result.put("message", String.format("文件大小不能超过%.1fMB",
                        uploadConfig.getAvatarMaxSize() / (1024.0 * 1024.0)));
                return result;
            }

            // 验证文件类型
            String originalFilename = file.getOriginalFilename();
            String fileExtension = FileUtils.getFileExtension(originalFilename);

            if (!uploadConfig.isAllowedType(fileExtension)) {
                result.put("code", 400);
                result.put("message", "只允许上传以下格式的图片: " +
                        String.join(", ", uploadConfig.getAllowedTypesArray()));
                return result;
            }

            // 生成文件名
            String fileName = FileUtils.generateUniqueFileName(originalFilename);

            // 构建保存路径
            String avatarDir = uploadConfig.getBaseDir() + File.separator + uploadConfig.getAvatarDir();
            File dir = new File(avatarDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath = avatarDir + File.separator + fileName;

            // 保存文件
            file.transferTo(new File(filePath));

            // 生成访问URL
            String avatarUrl = uploadConfig.getAvatarUrlPrefix() + fileName;

            result.put("code", 200);
            result.put("message", "头像上传成功");
            result.put("data", new HashMap<String, Object>() {{
                put("avatarUrl", avatarUrl);
                put("fileName", fileName);
                put("fileSize", file.getSize());
                put("fileType", file.getContentType());
            }});

        } catch (IOException e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "文件保存失败: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "上传失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 上传Base64格式的头像
     */
    public Map<String, Object> uploadAvatarBase64(String base64Data, String fileName, Integer userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 验证数据
            if (!StringUtils.hasText(base64Data)) {
                result.put("code", 400);
                result.put("message", "图片数据不能为空");
                return result;
            }

            // 生成文件名
            if (!StringUtils.hasText(fileName)) {
                fileName = "avatar_" + userId + "_" + System.currentTimeMillis() + ".jpg";
            }

            // 验证文件类型
            String fileExtension = FileUtils.getFileExtension(fileName);
            if (!uploadConfig.isAllowedType(fileExtension)) {
                result.put("code", 400);
                result.put("message", "只允许上传以下格式的图片: " +
                        String.join(", ", uploadConfig.getAllowedTypesArray()));
                return result;
            }

            // 构建保存路径
            String avatarDir = uploadConfig.getBaseDir() + File.separator + uploadConfig.getAvatarDir();

            // 保存Base64图片
            String filePath = FileUtils.saveBase64Image(base64Data, avatarDir, fileName);

            // 生成访问URL
            String avatarUrl = uploadConfig.getAvatarUrlPrefix() + fileName;

            result.put("code", 200);
            result.put("message", "头像上传成功");
            // 先创建Map对象
            Map<String, Object> data = new HashMap<>();
            data.put("avatarUrl", avatarUrl);
            data.put("fileName", fileName);
            data.put("filePath", filePath);

// 再放入result
            result.put("data", data);

        } catch (IOException e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "文件保存失败: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "上传失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 删除头像文件
     */
    public boolean deleteAvatarFile(String avatarUrl) {
        try {
            if (!StringUtils.hasText(avatarUrl)) {
                return false;
            }

            // 从URL中提取文件名
            String fileName = avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1);

            // 构建文件路径
            String filePath = uploadConfig.getBaseDir() + File.separator +
                    uploadConfig.getAvatarDir() + File.separator + fileName;

            return FileUtils.deleteFile(filePath);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}