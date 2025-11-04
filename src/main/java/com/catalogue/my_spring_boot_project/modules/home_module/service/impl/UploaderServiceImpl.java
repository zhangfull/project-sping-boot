package com.catalogue.my_spring_boot_project.modules.home_module.service.impl;

import java.util.List;

import javax.management.Query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catalogue.my_spring_boot_project.modules.common.entity.FileEntity;
import com.catalogue.my_spring_boot_project.modules.common.entity.FileOperationEntity;
import com.catalogue.my_spring_boot_project.modules.common.vo.MyPage;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.home_module.mapper.UploaderMapper;
import com.catalogue.my_spring_boot_project.modules.home_module.pojo.vo.UploaderListItemVO;
import com.catalogue.my_spring_boot_project.modules.home_module.service.UploaderService;

@Service
public class UploaderServiceImpl implements UploaderService {

    private final UploaderMapper uploaderMapper;

    public UploaderServiceImpl(UploaderMapper uploaderMapper) {
        this.uploaderMapper = uploaderMapper;
    }

    @Value("${file.pagination.page-size}")
    private int defaultPageSize;

    @Override
    public Result<List<UploaderListItemVO>> getHistory() {
        QueryWrapper<FileOperationEntity> queryWrapper = new QueryWrapper<FileOperationEntity>();
        queryWrapper
                .select("file_id", "operator_time", "operator")
                .eq("op_type", 1)
                .orderByDesc("operator_time")
                .last("LIMIT 100");

        List<FileOperationEntity> selectPage = uploaderMapper.selectList(queryWrapper);

        List<UploaderListItemVO> list = selectPage.stream().map(UploaderListItemVO::new).toList();
        return Result.success(list);
    }

}
