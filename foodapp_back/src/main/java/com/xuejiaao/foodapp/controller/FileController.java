package com.xuejiaao.foodapp.controller;

import com.xuejiaao.foodapp.entity.vo.MyResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/file")
@CrossOrigin
public class FileController {
    
    @Value("${upload.path}")
    private String uploadPath;
    
    /**
     * 上传图片
     * @param file 上传的文件
     * @return 上传结果
     */
    @PostMapping("/upload")
    public MyResult uploadImage(@RequestParam("file") MultipartFile file) {
        MyResult myResult = new MyResult();
        
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                myResult.setCode(400);
                myResult.setMsg("文件不能为空");
                myResult.setData(null);
                return myResult;
            }
            
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                myResult.setCode(400);
                myResult.setMsg("只支持图片类型上传");
                myResult.setData(null);
                return myResult;
            }
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + suffix;
            
            // 创建上传目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 保存文件
            File dest = new File(uploadPath + fileName);
            file.transferTo(dest);
            
            // 构建文件访问URL
            String fileUrl = "http://localhost:8089/" + fileName;
            
            myResult.setCode(200);
            myResult.setMsg("上传成功");
            myResult.setData(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("上传失败：" + e.getMessage());
            myResult.setData(null);
        }
        
        return myResult;
    }
}