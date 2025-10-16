package com.catalogue.my_spring_boot_project.modules.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FileUtils {

    public boolean saveFile(String path, MultipartFile data) {
        if (data.isEmpty()) {
            Log.info(getClass(), "上传文件为空");
            return false;
        }
        if (path == null || path.isEmpty()) {
            Log.info(getClass(), "上传文件路径为空");
            return false;
        }

        try {
            byte[] bytes = data.getBytes(); // 读取上传文件的字节
            File dest = new File(path); // url 已经包含完整文件名
            File parent = dest.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs(); // 确保目录存在
            }
            // 直接写入二进制文件
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest))) {
                bos.write(bytes);
            }
        } catch (Exception e) {
            Log.error(getClass(), "保存文件失败", e);
            return false;
        }
        return true;
    }

    public void downloadFile(String path, String fileName, HttpServletResponse response) {
        if (InspectionTool.stringIsEmpty(path)) {
            Log.info(getClass(), "合并下载文件路径为空");
            return;
        }
        // 获取文件夹下的所有文件
        File[] files = new File(path).listFiles((dir, name) -> name.startsWith("file.part"));

        if (files == null || files.length == 0) {
            Log.info(getClass(), "合并下载的文件不存在");
            return;
        }
        Arrays.sort(files, Comparator.comparing(File::getName));

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024 * 1024];
            int bytesRead;
            for (File f : files) {
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))) {
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (Exception e) {
            Log.error(getClass(), "合并下载文件失败", e);
        }
    }

    public int getFileCount(String path,String nameType) {
        File[] files = new File(path).listFiles((dir, name) -> name.startsWith(nameType));
        return files == null ? 0 : files.length;
    }
}
