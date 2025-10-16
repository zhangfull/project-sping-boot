package com.catalogue.my_spring_boot_project.modules.file_module.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catalogue.my_spring_boot_project.modules.common.entity.ImgInfoEntity;

public interface ImgMapper extends BaseMapper<ImgInfoEntity> {

    int insertBatch(@Param("list") List<ImgInfoEntity> list);
    
}
