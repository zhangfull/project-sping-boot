package com.catalogue.my_spring_boot_project.modules.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
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
            return Result.error(-2, "前端请求的头像地址为空");
        }
        Log.info(getClass(), "前端请求的最终头像地址：{}", url);
        File file = new File(url);
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            Log.error(getClass(), "读取文件失败", e);
            return Result.error(-1, "图片获取失败");
        }
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
            byte[] bytes = img.getBytes();
            String base64 = Base64.getEncoder().encodeToString(bytes);
            File dest = new File(url);
            File parent = dest.getParentFile();
            if (!parent.exists())
                parent.mkdirs();
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(dest), StandardCharsets.UTF_8))) {
                writer.write(base64);
            }
        } catch (Exception e) {
            Log.error(getClass(), "头像转换Base64失败", e);
            return Result.error(-3, "头像转换Base64失败");
        }
        return Result.success();
    }
}
