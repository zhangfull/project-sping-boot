package com.catalogue.my_spring_boot_project.modules.user_module.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UploadUserDTO {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String sex;
    private String briefIntroduction;
}
