package com.catalogue.my_spring_boot_project.modules.file_module.service;

import java.nio.file.Path;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.catalogue.my_spring_boot_project.modules.common.vo.FilePage;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileRequestDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileUploadFormDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.ValidateFormDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.FileDetailVO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.ListItemVO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.UploadPathsVO;

public interface FileService {

    /**
     * 获取文件列表
     * 
     * @param dto
     * @return
     */
    Result<FilePage<ListItemVO>> getFileList(FileRequestDTO dto);

    /**
     * 更新文件版本
     * 
     * @param message
     * @return
     */
    Result<String> updateVersion(String message);

    /**
     * 获取文件详情
     * 
     * @param id
     * @return
     */
    Result<FileDetailVO> getDetail(Long id);

    /**
     * 上传文件
     * 
     * @param dto
     * @return
     */
    Result<UploadPathsVO> uploadForm(FileUploadFormDTO dto);

    /**
     * 上传文件分片
     * 
     * @param uploadUrl
     * @param chunkIndex
     * @param chunkTotal
     * @param chunkBLOB
     * @return
     */
    Result<String> uploadChunk(String uploadUrl, Integer chunkIndex, Integer chunkTotal, MultipartFile chunkBLOB);

    /**
     * 批量上传文件
     * 
     * @param path
     * @param files
     * @return
     */
    Result<String> uploadImgs(String path, MultipartFile[] files, Long id);

    /**
     * 检查文件完整性
     * 
     * @param fileNameString
     * @param totalNumber
     * @param path
     * @return
     */
    Result<String> validate(ValidateFormDTO dto);

    /**
     * 删除文件
     * 
     * @param path
     * @return
     */
    Result<String> deleteFile(Path path);

    /**
     * 删除垃圾文件
     * 
     * @param body
     * @return
     */
    Result<String> deleteGarbage(Long id);

}
