// config/UploadConfig.java
package com.xuejiaao.foodapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file.upload")
public class UploadConfig {

    private String baseDir = "uploads";
    private String avatarDir = "avatars";
    private long avatarMaxSize = 2097152; // 2MB
    private String avatarAllowedTypes = "jpg,jpeg,png,gif";
    private String avatarUrlPrefix = "/uploads/avatars/";

    // Getters and Setters
    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getAvatarDir() {
        return avatarDir;
    }

    public void setAvatarDir(String avatarDir) {
        this.avatarDir = avatarDir;
    }

    public long getAvatarMaxSize() {
        return avatarMaxSize;
    }

    public void setAvatarMaxSize(long avatarMaxSize) {
        this.avatarMaxSize = avatarMaxSize;
    }

    public String getAvatarAllowedTypes() {
        return avatarAllowedTypes;
    }

    public void setAvatarAllowedTypes(String avatarAllowedTypes) {
        this.avatarAllowedTypes = avatarAllowedTypes;
    }

    public String getAvatarUrlPrefix() {
        return avatarUrlPrefix;
    }

    public void setAvatarUrlPrefix(String avatarUrlPrefix) {
        this.avatarUrlPrefix = avatarUrlPrefix;
    }

    /**
     * 获取允许的文件类型数组
     */
    public String[] getAllowedTypesArray() {
        return avatarAllowedTypes.split(",");
    }

    /**
     * 验证文件类型是否允许
     */
    public boolean isAllowedType(String fileType) {
        if (fileType == null || fileType.isEmpty()) {
            return false;
        }

        // 去掉点号和转小写
        String type = fileType.toLowerCase().replace(".", "");

        for (String allowedType : getAllowedTypesArray()) {
            if (allowedType.trim().equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
}