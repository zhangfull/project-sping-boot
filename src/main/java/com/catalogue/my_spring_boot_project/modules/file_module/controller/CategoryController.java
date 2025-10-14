package com.catalogue.my_spring_boot_project.modules.file_module.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.CategoryVO;
import com.catalogue.my_spring_boot_project.modules.file_module.service.CategoryService;

@RestController
@RequestMapping("/fileCategory")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/getAll")
    public Result<List<CategoryVO>> getFileCategoryList() {
        return categoryService.getCategoryList();
    }

    
}
