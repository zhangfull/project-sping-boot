package com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo;

import java.time.format.DateTimeFormatter;

import com.catalogue.my_spring_boot_project.modules.common.entity.FileEntity;

import lombok.Data;

@Data
public class ListItemVO {

    private Long id;

    private String headline;

    private String category;

    private String description;

    private String uploadDate;

    private String collectionCount;

    public ListItemVO(FileEntity f) {
        this.id = f.getId();
        this.headline = f.getHeadline();
        this.description = f.getDescription();
        this.uploadDate = f.getUploadDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.collectionCount = f.getCollectionCount().toString();
    }

}
