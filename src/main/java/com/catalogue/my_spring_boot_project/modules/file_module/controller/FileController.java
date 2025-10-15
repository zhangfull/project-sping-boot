package com.catalogue.my_spring_boot_project.modules.file_module.controller;

import org.springframework.web.bind.annotation.RestController;

import com.catalogue.my_spring_boot_project.modules.common.utils.Log;
import com.catalogue.my_spring_boot_project.modules.common.vo.FilePage;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileRequestDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileUploadFormDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.ListItemVO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.UploadUrlsVO;
import com.catalogue.my_spring_boot_project.modules.file_module.service.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/getFiles")
    public Result<FilePage<ListItemVO>> getFiles(@RequestBody FileRequestDTO dto) {
        Log.info(getClass(), "获取文件列表条件：{}", dto.toString());
        return fileService.getFileList(dto);
    }


    @PostMapping("/uploadForm")
    public Result<UploadUrlsVO> postMethodName(@RequestBody FileUploadFormDTO dto) {
        Log.info(getClass(), "上传文件条件：{}", dto.toString());
        return fileService.uploadForm(dto);
    }
    

}
