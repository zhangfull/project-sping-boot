package com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo;

import java.time.format.DateTimeFormatter;

import com.catalogue.my_spring_boot_project.modules.common.entity.FileEntity;

import lombok.Data;

@Data
public class FileDetailVO {

    private Long id;
    private Long uploaderId;
    private String headline;
    private String fileName;
    private String uploader;
    private String size;
    private String category;
    private String uploadDate;
    private String collectionCount;
    private String filePath;
    private String introduce;
    private String[] imgsBase64;

    /**
     * 没有 上传者名，图片，标签
     * @param f
     */
    public FileDetailVO(FileEntity f) {
        this.id = f.getId();
        this.uploaderId = f.getUploaderId();
        this.headline = f.getHeadline();
        this.fileName = f.getFileName();
        this.size = f.getSize();
        this.uploadDate = f.getUploadDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.collectionCount = f.getCollectionCount().toString();
        this.filePath = f.getPath();
        this.introduce = f.getIntroduce();
    }
    
}
