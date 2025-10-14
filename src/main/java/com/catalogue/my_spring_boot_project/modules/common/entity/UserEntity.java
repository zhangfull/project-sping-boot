package com.catalogue.my_spring_boot_project.modules.common.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("user")
public class UserEntity {
    private Long id;
    private String uid;
    private String name;
    private String sex;
    private String email;
    private String password;
    private LocalDateTime registrationDate;
    private String briefIntroduction;
    private Integer followersCount;
    private Integer myFollowersCount;
    private String avatarUrl;
    @TableField(exist = false)
    private Set<RoleEntity> roles = new HashSet<>();
}
