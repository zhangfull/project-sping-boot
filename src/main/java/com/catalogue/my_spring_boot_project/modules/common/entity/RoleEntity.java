package com.catalogue.my_spring_boot_project.modules.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("roles")
public class RoleEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
}
