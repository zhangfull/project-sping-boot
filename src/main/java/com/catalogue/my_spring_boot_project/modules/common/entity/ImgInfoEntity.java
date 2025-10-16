package com.catalogue.my_spring_boot_project.modules.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("file_imgs")
public class ImgInfoEntity {
    
    private Long fileId;
    private String imgPath;

    public ImgInfoEntity(Long fileId, String imgPath) {
        this.fileId = fileId;
        this.imgPath = imgPath;
    }
}
