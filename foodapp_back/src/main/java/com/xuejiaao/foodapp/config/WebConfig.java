// config/WebConfig.java
package com.xuejiaao.foodapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.base-dir:uploads}")
    private String baseUploadDir;

    @Value("${file.upload.avatar-dir:avatars}")
    private String avatarDir;

    @Value("${file.upload.access-path:/uploads/**}")
    private String accessPath;

    private String absoluteUploadPath;
    private String absoluteAvatarPath;

    /**
     * 初始化 - 创建上传目录
     */
    @PostConstruct
    public void init() {
        try {
            // 构建项目根目录路径
            String projectPath = System.getProperty("user.dir");
            absoluteUploadPath = projectPath + File.separator + baseUploadDir;
            absoluteAvatarPath = absoluteUploadPath + File.separator + avatarDir;

            // 创建目录
            File uploadDirFile = new File(absoluteUploadPath);
            if (!uploadDirFile.exists()) {
                boolean created = uploadDirFile.mkdirs();
                if (created) {
                    System.out.println("✅ 创建上传基础目录: " + uploadDirFile.getAbsolutePath());
                }
            }

            File avatarDirFile = new File(absoluteAvatarPath);
            if (!avatarDirFile.exists()) {
                boolean created = avatarDirFile.mkdirs();
                if (created) {
                    System.out.println("✅ 创建头像上传目录: " + avatarDirFile.getAbsolutePath());
                }
            }

            System.out.println("==========================================");
            System.out.println("📁 文件上传目录初始化完成:");
            System.out.println("   项目路径: " + projectPath);
            System.out.println("   上传基础目录: " + absoluteUploadPath);
            System.out.println("   头像存储目录: " + absoluteAvatarPath);
            System.out.println("   访问路径: " + accessPath);
            System.out.println("==========================================");

        } catch (Exception e) {
            System.err.println("❌ 创建上传目录失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 配置静态资源访问
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            // 配置头像文件访问
            String avatarResourceLocation = "file:" + absoluteAvatarPath + File.separator;
            String avatarResourceHandler = "/" + baseUploadDir + "/" + avatarDir + "/**";

            System.out.println("🖼️ 配置头像资源映射:");
            System.out.println("   访问URL: " + avatarResourceHandler);
            System.out.println("   映射到: " + avatarResourceLocation);

            registry.addResourceHandler(avatarResourceHandler)
                    .addResourceLocations(avatarResourceLocation);

            // 配置通用上传文件访问
            String uploadResourceLocation = "file:" + absoluteUploadPath + File.separator;
            String uploadResourceHandler = "/" + baseUploadDir + "/**";

            registry.addResourceHandler(uploadResourceHandler)
                    .addResourceLocations(uploadResourceLocation);

            System.out.println("✅ 静态资源映射配置完成");

        } catch (Exception e) {
            System.err.println("❌ 配置静态资源失败: " + e.getMessage());
        }
    }

    /**
     * 配置跨域 - 修复版（解决CORS错误且不影响现有功能）
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 获取当前环境
        String profile = System.getProperty("spring.profiles.active", "");
        boolean isDev = "dev".equals(profile) || "development".equals(profile) || "local".equals(profile) || profile.isEmpty();

        if (isDev) {
            // ✅ 开发环境：兼容性最好的配置（解决你的CORS错误）
            registry.addMapping("/api/**")
                    .allowedOriginPatterns("*")  // 允许所有域名访问
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                    .allowedHeaders("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers")
                    .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
                    .allowCredentials(true)  // 可以设为true，因为使用了allowedOriginPatterns
                    .maxAge(3600);

            System.out.println("✅ CORS跨域配置完成（开发模式 - 修复CORS错误）");
        } else {
            // ✅ 生产环境：更安全的配置
            registry.addMapping("/api/**")
                    .allowedOrigins(
                            "https://yourdomain.com",      // 生产域名1
                            "https://www.yourdomain.com"   // 生产域名2
                    )
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("Authorization", "Content-Type", "X-Requested-With")
                    .allowCredentials(true)
                    .maxAge(3600);

            System.out.println("✅ CORS跨域配置完成（生产模式）");
        }

        // ✅ 可选：为特定的路由单独配置（如果需要特殊处理）
        registry.addMapping("/api/address/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 备选方案：如果你想在开发时使用allowCredentials(true)，使用这个版本
     */
    /*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // ✅ 方案1：最简单的解决方案 - 直接修改为false
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(false)  // ✅ 关键修改点
                .maxAge(3600);

        System.out.println("✅ CORS跨域配置完成（兼容模式）");
    }
    */

    /**
     * 备选方案：如果你想使用最新的allowedOriginPatterns（Spring Boot 2.4+）
     */
    /*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // ✅ 方案2：使用allowedOriginPatterns配合allowCredentials(true)
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")  // ✅ 注意：这里用的是allowedOriginPatterns（复数）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)  // 可以设为true
                .maxAge(3600);

        System.out.println("✅ CORS跨域配置完成（使用allowedOriginPatterns）");
    }
    */
}