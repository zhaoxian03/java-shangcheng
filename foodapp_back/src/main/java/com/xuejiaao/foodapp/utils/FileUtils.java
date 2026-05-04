// utils/FileUtils.java
package com.xuejiaao.foodapp.utils;

import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class FileUtils {

    /**
     * 保存Base64图片
     */
    public static String saveBase64Image(String base64Data, String dirPath, String fileName) throws IOException {
        if (!StringUtils.hasText(base64Data)) {
            throw new IllegalArgumentException("Base64数据不能为空");
        }

        // 清理Base64前缀
        String imageData = base64Data;
        if (base64Data.contains("base64,")) {
            imageData = base64Data.split("base64,")[1];
        }

        // 解码Base64
        byte[] imageBytes = Base64Utils.decodeFromString(imageData);

        // 确保目录存在
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 构建文件路径
        String filePath = dirPath + File.separator + fileName;

        // 保存文件
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(imageBytes);
        }

        return filePath;
    }

    /**
     * 生成唯一的文件名
     */
    public static String generateUniqueFileName(String originalFileName) {
        if (!StringUtils.hasText(originalFileName)) {
            return UUID.randomUUID().toString() + ".jpg";
        }

        String extension = "";
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }

        return UUID.randomUUID().toString() + extension;
    }

    /**
     * 验证文件是否为图片
     */
    public static boolean isImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    /**
     * 验证文件是否为图片（通过扩展名）
     */
    public static boolean isImageFile(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return false;
        }

        String extension = getFileExtension(fileName).toLowerCase();
        return extension.matches("(jpg|jpeg|png|gif|bmp|webp)");
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileExtension(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return "";
        }

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String filePath) {
        if (!StringUtils.hasText(filePath)) {
            return false;
        }

        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 将文件转换为Base64字符串
     */
    public static String fileToBase64(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在: " + filePath);
        }

        byte[] fileContent = Files.readAllBytes(file.toPath());
        return Base64Utils.encodeToString(fileContent);
    }

    /**
     * 获取文件大小（MB）
     */
    public static double getFileSizeMB(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.length() / (1024.0 * 1024.0);
        }
        return 0;
    }
}