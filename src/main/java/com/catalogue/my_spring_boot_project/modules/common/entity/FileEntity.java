package com.catalogue.my_spring_boot_project.modules.common.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("file_info")
public class FileEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long uploaderId;
    private String headline;
    private Long fileCategoryId;
    private String description;
    private String size;
    private LocalDateTime uploadDate;
    private Long collectionCount;
    private String introduce;
    private String fileName;
    private String display;
    private String path;

}
