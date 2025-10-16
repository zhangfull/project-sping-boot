package com.catalogue.my_spring_boot_project.modules.file_module.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.catalogue.my_spring_boot_project.modules.common.utils.FileUtils;
import com.catalogue.my_spring_boot_project.modules.common.utils.Log;
import com.catalogue.my_spring_boot_project.modules.common.vo.FilePage;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileRequestDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileUploadFormDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.ValidateFormDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.ListItemVO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.UploadPathsVO;
import com.catalogue.my_spring_boot_project.modules.file_module.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    private final FileUtils fileUtils;

    public FileController(FileService fileService, FileUtils fileUtils) {
        this.fileService = fileService;
        this.fileUtils = fileUtils;
    }

    @PostMapping("/getFiles")
    public Result<FilePage<ListItemVO>> getFiles(@RequestBody FileRequestDTO dto) {
        Log.info(getClass(), "获取文件列表条件：{}", dto.toString());
        return fileService.getFileList(dto);
    }

    @PostMapping("/uploadForm")
    public Result<UploadPathsVO> postMethodName(@RequestBody FileUploadFormDTO dto) {
        Log.info(getClass(), "上传文件：{}", dto.toString());
        return fileService.uploadForm(dto);
    }

    @PostMapping("/uploadChunk")
    public Result<String> postMethodName(
            @RequestParam("uploadUrl") String uploadUrl,
            @RequestParam("chunkIndex") Integer chunkIndex,
            @RequestParam("chunkTotal") Integer chunkTotal,
            @RequestPart("chunkBLOB") MultipartFile chunkBLOB) {
        Log.info(getClass(), "上传文件分片:{}/{}", chunkIndex, chunkTotal - 1);

        if (chunkTotal > 125) {
            return Result.error(-1, "分片过多");
        }
        return fileService.uploadChunk(uploadUrl, chunkIndex, chunkTotal, chunkBLOB);
    }

    @PostMapping("/uploadImgs")
    public Result<String> uploadImgs(@RequestPart("filePath") String path,
            @RequestPart("imgs") MultipartFile[] files) {
        return fileService.uploadImgs(path, files);
    }

    @PostMapping("/validate")
    public Result<String> validate(@RequestBody ValidateFormDTO dto) {
        return fileService.validate(dto);
    }

    @GetMapping("/download")
    public void getMethodName(HttpServletResponse response) {
        fileUtils.downloadFile("E:/VSCodeWarehouse/JavaProjects/my-spring-boot-project/warehouse/files/filenull",
                "1.21.8-Fabric 0.17.2.zip", response);
    }

}
