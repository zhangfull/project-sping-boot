package com.catalogue.my_spring_boot_project.modules.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.catalogue.my_spring_boot_project.modules.common.vo.Result;

@Component
public class ImgUtils {

    /**
     * 获取图片
     * 
     * @param url 图片地址
     * @return 图片的Base64
     */
    public Result<String> getImg(String url) {
        if (url == null || url.isEmpty()) {
            return Result.error(-2, "前端请求的图片地址为空");
        }

        String filePath = "";
        String mimeType = "";
        String[] parts = url.split("\\|", 2);
        if (parts.length == 2) {
             mimeType = parts[0]; // "image/png"
            filePath = parts[1]; // "/uploads/2025/10/15/abc123.bin"
        } else {
            // 异常处理
            Log.info(getClass(), "前端请求的图片地址格式不正确:{}", url);
            return Result.error(-2, "前端请求的图片地址格式不正确");

        }
        Log.info(getClass(), "前端请求的最终图片地址：{}，文件类型：{}", filePath, mimeType);
        File file = new File(filePath);
        if (!file.exists()) {
            Log.info(getClass(), "文件不存在");
            return Result.error(-3, "文件不存在");
        }
        byte[] bytes;
        try (FileInputStream fis = new FileInputStream(file)) {
            bytes = fis.readAllBytes(); // JDK 9+ 可以直接读取全部字节
        } catch (IOException e) {
            Log.error(getClass(), "读取文件失败", e);
            return Result.error(-1, "文件获取失败");
        }

        // 将字节转换为 Base64 字符串
        StringBuilder content = new StringBuilder();
        content.append("data:image/");
        content.append(mimeType);
        content.append(";base64,");
        content.append(Base64.getEncoder().encodeToString(bytes));

        // 返回读取的 Base64 字符串
        return Result.success(content.toString());
    }

    /**
     * 上传图片
     * 
     * @param img 图片
     * @param url 图片地址
     * @return 上传结果
     */
    public Result<String> uploadImg(MultipartFile img, String url) {
        if (img.isEmpty()) {
            return Result.error(-1, "后端未收到文件");
        }
        if (url == null || url.isEmpty()) {
            return Result.error(-2, "后端未收到地址");
        }

        try {
            byte[] bytes = img.getBytes(); // 读取上传文件的字节
            File dest = new File(url); // url 已经包含完整文件名
            File parent = dest.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs(); // 确保目录存在
            }
            // 直接写入二进制文件
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(bytes);
                fos.flush();
            }

        } catch (Exception e) {
            Log.error(getClass(), "头像转换Base64失败", e);
            return Result.error(-3, "头像转换Base64失败");
        }
        return Result.success();
    }
}
