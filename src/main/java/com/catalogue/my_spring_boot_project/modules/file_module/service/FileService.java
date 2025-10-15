package com.catalogue.my_spring_boot_project.modules.file_module.service;

import org.springframework.web.multipart.MultipartFile;

import com.catalogue.my_spring_boot_project.modules.common.vo.FilePage;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileRequestDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileUploadFormDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.ListItemVO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.UploadPathsVO;

public interface FileService {

    /**
     * 获取文件列表
     * @param dto
     * @return
     */
    Result<FilePage<ListItemVO>> getFileList(FileRequestDTO dto);

    /**
     * 更新文件版本
     * @param message
     * @return
     */
    Result<String> updateVersion(String message);

    /**
     * 获取文件详情
     * @param id
     * @return
     */
    Result<String> getDetail(Long id);

    /**
     * 上传文件
     * @param dto
     * @return
     */
    Result<UploadPathsVO> uploadForm(FileUploadFormDTO dto);

    /**
     * 上传文件分片
     * @param uploadUrl
     * @param chunkIndex
     * @param chunkTotal
     * @param chunkBLOB
     * @return
     */
    Result<String> uploadChunk(String uploadUrl, Integer chunkIndex, Integer chunkTotal, MultipartFile chunkBLOB);

}
