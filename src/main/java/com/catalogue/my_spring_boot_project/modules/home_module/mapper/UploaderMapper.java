package com.catalogue.my_spring_boot_project.modules.home_module.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catalogue.my_spring_boot_project.modules.common.entity.FileOperationEntity;

@Mapper
public interface UploaderMapper extends BaseMapper<FileOperationEntity> {
    
}
