package com.catalogue.my_spring_boot_project.modules.file_module.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.catalogue.my_spring_boot_project.modules.common.entity.CategoryEntity;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.file_module.mapper.CategoryMapper;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.CategoryVO;
import com.catalogue.my_spring_boot_project.modules.file_module.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Result<List<CategoryVO>> getCategoryList() {
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "category_name");
        List<CategoryEntity> categoryEntities = categoryMapper.selectList(queryWrapper);
        List<CategoryVO> categoryVOs = categoryEntities.stream().map(CategoryVO::new).toList();
        return Result.success(categoryVOs);
    }

}
