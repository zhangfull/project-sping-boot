package com.catalogue.my_spring_boot_project.modules.file_module.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.CategoryVO;

@Service
public interface CategoryService {
    
    Result<List<CategoryVO>> getCategoryList();

}
