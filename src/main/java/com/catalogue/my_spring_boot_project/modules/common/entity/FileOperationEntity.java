package com.catalogue.my_spring_boot_project.modules.common.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("file_operation_info")
public class FileOperationEntity {
    private Long id;
    private LocalDateTime operatorTime;
    private String operator;
    private Long fileId;
    private Integer opType;
}
