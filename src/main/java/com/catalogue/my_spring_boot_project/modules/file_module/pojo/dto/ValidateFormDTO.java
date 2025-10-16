package com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto;

import lombok.Data;

@Data
public class ValidateFormDTO {
    private String filePath;
    private String imgsPath;
    private Integer totalNumber;
    private Integer totalImgs;
}
