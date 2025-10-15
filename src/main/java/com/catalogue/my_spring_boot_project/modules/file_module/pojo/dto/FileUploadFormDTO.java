package com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto;

import lombok.Data;

@Data
public class FileUploadFormDTO {
    private String headline;
    private Long categoryCode;
    private String fileName;
    private String size;
    private String description;
    private String introduce;
    private boolean display;
}
