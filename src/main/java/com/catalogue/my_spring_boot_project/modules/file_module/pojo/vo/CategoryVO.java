package com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo;

import com.catalogue.my_spring_boot_project.modules.common.entity.CategoryEntity;

import lombok.Data;

@Data
public class CategoryVO {
    private Long categoryCode;
    private String categoryName;

    
    public CategoryVO(CategoryEntity c) {
        this.categoryCode = c.getId();
        this.categoryName = c.getCategoryName();
    }
}
