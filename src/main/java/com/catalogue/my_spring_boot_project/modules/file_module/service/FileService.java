package com.catalogue.my_spring_boot_project.modules.file_module.service;

import com.catalogue.my_spring_boot_project.modules.common.vo.FilePage;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileRequestDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileUploadFormDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.ListItemVO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.UploadUrlsVO;

public interface FileService {

    Result<FilePage<ListItemVO>> getFileList(FileRequestDTO dto);

    Result<String> updateVersion(String message);

    Result<String> getDetail(Long id);

    Result<UploadUrlsVO> uploadForm(FileUploadFormDTO dto);

}
