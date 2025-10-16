package com.catalogue.my_spring_boot_project.modules.file_module.service.impl;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catalogue.my_spring_boot_project.modules.common.entity.CategoryEntity;
import com.catalogue.my_spring_boot_project.modules.common.entity.FileEntity;
import com.catalogue.my_spring_boot_project.modules.common.utils.FileUtils;
import com.catalogue.my_spring_boot_project.modules.common.utils.ImgUtils;
import com.catalogue.my_spring_boot_project.modules.common.utils.InspectionTool;
import com.catalogue.my_spring_boot_project.modules.common.utils.Log;
import com.catalogue.my_spring_boot_project.modules.common.utils.ThreadLocalUtil;
import com.catalogue.my_spring_boot_project.modules.common.vo.FilePage;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.file_module.mapper.CategoryMapper;
import com.catalogue.my_spring_boot_project.modules.file_module.mapper.FileMapper;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileRequestDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileUploadFormDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.ValidateFormDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.ListItemVO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.UploadPathsVO;
import com.catalogue.my_spring_boot_project.modules.file_module.service.FileService;

import org.springframework.beans.factory.annotation.Value;

@Transactional
@Service
public class FileServiceImpl implements FileService {

    @Value("${file.pagination.page-size}")
    private int defaultPageSize;

    private final FileMapper fileMapper;
    private final CategoryMapper categoryMapper;
    private final ImgUtils imgUtils;
    private final FileUtils fileUtils;

    public FileServiceImpl(FileMapper fileMapper,
            CategoryMapper categoryMapper,
            ImgUtils imgUtils,
            FileUtils fileUtils) {
        this.fileMapper = fileMapper;
        this.categoryMapper = categoryMapper;
        this.imgUtils = imgUtils;
        this.fileUtils = fileUtils;
    }

    @Override
    public Result<FilePage<ListItemVO>> getFileList(FileRequestDTO dto) {
        Page<FileEntity> p = new Page<>(dto.getNeedPage(), defaultPageSize);
        Page<FileEntity> selectPage;
        if (!dto.getIsFiltered()) {
            selectPage = fileMapper.selectPage(p, null);
        } else {
            QueryWrapper<FileEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id", "headline", "file_category_id", "description", "upload_date", "collection_count");
            if (!(dto.getFilters().getSearchTerm() == null
                    || dto.getFilters().getSearchTerm().isBlank())) {
                queryWrapper.like("headline", dto.getFilters().getSearchTerm().trim());
            }

            if (!(dto.getFilters().getCategoryCode() == null
                    || dto.getFilters().getCategoryCode() < 0)) {
                queryWrapper.eq("file_category_id", dto.getFilters().getCategoryCode());
            }

            if (!(dto.getFilters().getDateRange() == null
                    || dto.getFilters().getDateRange().isBlank())) {
                dto.getFilters().setDateRange();
                queryWrapper.between("upload_date", dto.getFilters().getStartDate(),
                        new java.sql.Date(System.currentTimeMillis()));
            }

            if (!(dto.getFilters().getOrder() == null
                    || dto.getFilters().getOrder().isBlank())) {
                switch (dto.getFilters().getOrder()) {
                    case "uploadDateDesc":
                        queryWrapper.orderByDesc("upload_date");
                        break;
                    case "uploadDateAsc":
                        queryWrapper.orderByAsc("upload_date");
                        break;
                    case "collectionCountDesc":
                        queryWrapper.orderByDesc("collection_count");
                        break;
                    case "collectionCountAsc":
                        queryWrapper.orderByAsc("collection_count");
                        break;
                    default:
                        break;
                }
            }
            selectPage = fileMapper.selectPage(p, queryWrapper);
        }
        if (selectPage.getRecords() == null || selectPage.getRecords().isEmpty()) {
            Log.info(getClass(), "获取文件列表为空");
            return Result.success(null);
        }
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "category_name");
        List<CategoryEntity> categories = categoryMapper.selectList(queryWrapper);
        List<ListItemVO> displayFiles = selectPage.getRecords().stream().map(f -> {
            ListItemVO listItemVO = new ListItemVO(f);
            listItemVO.setCategory(
                    categories.stream()
                            .filter(c -> c.getId().equals(f.getFileCategoryId()))
                            .map(c -> c.getCategoryName())
                            .findFirst()
                            .orElse("未分类"));

            return listItemVO;
        }).toList();
        FilePage<ListItemVO> filePage = new FilePage<>();
        filePage.setResults(displayFiles);
        filePage.setCurrentPage(dto.getNeedPage());
        filePage.setTotalPages(selectPage.getPages());
        filePage.setPageSize(defaultPageSize);
        filePage.setLatestVersion(2l);
        return Result.success(filePage);
    }

    @Override
    public Result<String> updateVersion(String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateVersion'");
    }

    @Override
    public Result<String> getDetail(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDetail'");
    }

    @Value("${file.files.path}")
    private String FILEPATH;

    @Value("${file.img.introduceImgs}")
    private String INTRODUCEIMGS;

    @Override
    public Result<UploadPathsVO> uploadForm(FileUploadFormDTO dto) {
        if (InspectionTool.stringIsEmpty(dto.getFileName()) &&
                InspectionTool.numberIsEmpty(dto.getCategoryCode()) &&
                InspectionTool.stringIsEmpty(dto.getSize()) &&
                InspectionTool.stringIsEmpty(dto.getHeadline())) {
            return Result.error(-1, "参数验证失败");
        }

        FileEntity file = new FileEntity();
        file.setFileName(dto.getFileName());
        file.setUploaderId(ThreadLocalUtil.getLongId());
        file.setFileCategoryId(dto.getCategoryCode());
        file.setSize(dto.getSize());
        file.setHeadline(dto.getHeadline());
        file.setIntroduce(dto.getIntroduce());
        file.setDescription(dto.getDescription());
        file.setUploadDate(LocalDateTime.now());
        file.setCollectionCount(0L);
        // fileMapper.insert(file);
        file.setId(111l);

        if (file.getId() == null) {
            return Result.error(-2, "文件上传失败");
        }

        UploadPathsVO uploadUrlsVO = new UploadPathsVO();
        uploadUrlsVO.setFilePath(FILEPATH + "file" + file.getId());
        uploadUrlsVO.setImgsPath(INTRODUCEIMGS + "introduceImgs" + file.getId());
        try {
            File path1 = new File(uploadUrlsVO.getFilePath());
            File path2 = new File(uploadUrlsVO.getImgsPath());
            if (!path1.exists()) {
                path1.mkdirs();
            }
            if (!path2.exists()) {
                path2.mkdirs();
            }
        } catch (Exception e) {
            Log.error(getClass(), "创建文件夹失败", e);
            return Result.error(-2, "创建文件夹失败");
        }
        return Result.success(uploadUrlsVO);
    }

    @Override
    public Result<String> uploadChunk(String uploadUrl, Integer chunkIndex, Integer chunkTotal,
            MultipartFile chunkBLOB) {
        if (InspectionTool.stringIsEmpty(uploadUrl) ||
                InspectionTool.numberIsEmpty(chunkIndex) ||
                InspectionTool.numberIsEmpty(chunkTotal) ||
                chunkBLOB == null) {
            return Result.error(-1, "参数验证失败");
        }
        File file = new File(uploadUrl, "file.part" + chunkIndex);
        boolean saveFile = fileUtils.saveFile(file.toString(), chunkBLOB);
        if (!saveFile) {
            return Result.error(-2, "文件保存失败");
        }
        return Result.success();
    }

    @Override
    public Result<String> uploadImgs(String path, MultipartFile[] files) {
        int count = 0;
        for (MultipartFile file : files) {
            File f = new File(path, "img" + ".bin" + count);
            Result<String> uploadImg = imgUtils.uploadImg(file, f.toString());
            if (uploadImg.getCode() != 0) {
                Log.info(getClass(), "上传图片失败:{}", f.toString());
            }
            count++;
        }
        return Result.success();
    }

    @Override
    public Result<String> validate(ValidateFormDTO dto) {
        int fileCount = fileUtils.getFileCount(dto.getFilePath(), "file.part");
        int imgCount = fileUtils.getFileCount(dto.getImgsPath(), "img.bin");
        if (fileCount == dto.getTotalNumber() && imgCount == dto.getTotalImgs()) {
            return Result.success();
        }
        return Result.error(-1, "文件校验失败");
    }

}
