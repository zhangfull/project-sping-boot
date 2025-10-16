package com.catalogue.my_spring_boot_project.modules.user_module.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catalogue.my_spring_boot_project.modules.common.entity.UserEntity;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    UserEntity selectUserWithRoles(String emailOrUid);
    
}
