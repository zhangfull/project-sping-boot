package com.catalogue.my_spring_boot_project.modules.user_module.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginDTO {
    private String emailOrUid;
    private String password;
}
