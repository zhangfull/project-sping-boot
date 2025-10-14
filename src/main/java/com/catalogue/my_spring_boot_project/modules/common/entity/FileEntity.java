package com.catalogue.my_spring_boot_project.modules.common.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("file_info")
public class FileEntity {
    private Long id;
    private Long uploaderId;
    private String headline;
    private Long fileCategoryId;
    private String description;
    private LocalDateTime uploadDate;
    private Long collectionCount;
    private String introduce;
    private String fileName;
    private String display;
    private String path;

}
