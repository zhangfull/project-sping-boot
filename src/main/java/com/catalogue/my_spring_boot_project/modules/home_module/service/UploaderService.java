package com.catalogue.my_spring_boot_project.modules.home_module.service;

import java.util.List;

import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.home_module.pojo.vo.UploaderListItemVO;

public interface UploaderService {

    Result<List<UploaderListItemVO>> getHistory();
    
}
