package com.catalogue.my_spring_boot_project.modules.user_module.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginVO {
  private String avatarUrl;
  private String accessToken;
}
