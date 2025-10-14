package com.catalogue.my_spring_boot_project.modules.file_module.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catalogue.my_spring_boot_project.modules.common.entity.FileEntity;

@Mapper
public interface FileMapper extends BaseMapper<FileEntity> {

}
