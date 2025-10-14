package com.catalogue.my_spring_boot_project.modules.file_module.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catalogue.my_spring_boot_project.modules.common.entity.CategoryEntity;
import com.catalogue.my_spring_boot_project.modules.common.entity.FileEntity;
import com.catalogue.my_spring_boot_project.modules.common.utils.InspectionTool;
import com.catalogue.my_spring_boot_project.modules.common.utils.Log;
import com.catalogue.my_spring_boot_project.modules.common.vo.FilePage;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.file_module.mapper.CategoryMapper;
import com.catalogue.my_spring_boot_project.modules.file_module.mapper.FileMapper;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileRequestDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileUploadFormDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.ListItemVO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.UploadUrlsVO;
import com.catalogue.my_spring_boot_project.modules.file_module.service.CategoryService;
import com.catalogue.my_spring_boot_project.modules.file_module.service.FileService;

import org.springframework.beans.factory.annotation.Value;

@Transactional
@Service
public class FileServiceImpl implements FileService {

    @Value("${file.pagination.page-size}")
    private int defaultPageSize;

    private final FileMapper fileMapper;
    private final CategoryMapper categoryMapper;

    public FileServiceImpl(FileMapper fileMapper, CategoryMapper categoryMapper) {
        this.fileMapper = fileMapper;
        this.categoryMapper = categoryMapper;
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
                    || dto.getFilters().getCategoryCode() == 0)) {
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

    @Override
    public Result<UploadUrlsVO> uploadForm(FileUploadFormDTO dto) {
        if (InspectionTool.stringIsEmpty(dto.getFileName()) &&
                InspectionTool.numberIsEmpty(dto.getCategoryCode()) &&
                InspectionTool.stringIsEmpty(dto.getSize()) &&
                InspectionTool.stringIsEmpty(dto.getHeadline())) {
            return null;
        }
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadForm'");
    }

}
