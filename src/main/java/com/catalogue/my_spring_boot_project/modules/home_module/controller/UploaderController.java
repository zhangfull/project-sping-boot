package com.catalogue.my_spring_boot_project.modules.home_module.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catalogue.my_spring_boot_project.modules.common.utils.Log;
import com.catalogue.my_spring_boot_project.modules.common.vo.MyPage;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.home_module.pojo.vo.UploaderListItemVO;
import com.catalogue.my_spring_boot_project.modules.home_module.service.impl.UploaderServiceImpl;

@RestController
@RequestMapping("/uploader")
public class UploaderController {

    private final UploaderServiceImpl uploaderService;

    public UploaderController(UploaderServiceImpl uploaderService) {
        this.uploaderService = uploaderService;
    }

    @GetMapping("/getHistory")
    public Result<List<UploaderListItemVO>> getHistory() {
        Log.info(getClass(), "获取上传历史");
        return uploaderService.getHistory();
    }
}
