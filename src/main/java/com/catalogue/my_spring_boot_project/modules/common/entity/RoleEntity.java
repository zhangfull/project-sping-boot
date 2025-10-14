package com.catalogue.my_spring_boot_project.modules.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("roles")
public class RoleEntity {
    private Long id;
    private String name;
    private String description;
}
