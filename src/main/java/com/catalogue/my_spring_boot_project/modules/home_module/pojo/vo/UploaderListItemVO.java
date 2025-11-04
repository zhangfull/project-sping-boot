package com.catalogue.my_spring_boot_project.modules.home_module.pojo.vo;

import java.time.format.DateTimeFormatter;

import com.catalogue.my_spring_boot_project.modules.common.entity.FileOperationEntity;

import lombok.Data;

@Data
public class UploaderListItemVO {
    private String date;
    private Long fileId;
    private String text;

    public UploaderListItemVO(FileOperationEntity f) {
        this.date = f.getOperatorTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.fileId = f.getFileId();
        this.text = f.getOperator();
    }
}
