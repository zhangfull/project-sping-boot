package com.catalogue.my_spring_boot_project.modules.file_module.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catalogue.my_spring_boot_project.modules.common.entity.CategoryEntity;
import com.catalogue.my_spring_boot_project.modules.common.entity.FileEntity;
import com.catalogue.my_spring_boot_project.modules.common.entity.ImgInfoEntity;
import com.catalogue.my_spring_boot_project.modules.common.entity.UserEntity;
import com.catalogue.my_spring_boot_project.modules.common.utils.FileUtils;
import com.catalogue.my_spring_boot_project.modules.common.utils.ImgUtils;
import com.catalogue.my_spring_boot_project.modules.common.utils.InspectionTool;
import com.catalogue.my_spring_boot_project.modules.common.utils.Log;
import com.catalogue.my_spring_boot_project.modules.common.utils.ThreadLocalUtil;
import com.catalogue.my_spring_boot_project.modules.common.vo.FilePage;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.file_module.mapper.CategoryMapper;
import com.catalogue.my_spring_boot_project.modules.file_module.mapper.FileMapper;
import com.catalogue.my_spring_boot_project.modules.file_module.mapper.ImgMapper;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileRequestDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.FileUploadFormDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto.ValidateFormDTO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.FileDetailVO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.ListItemVO;
import com.catalogue.my_spring_boot_project.modules.file_module.pojo.vo.UploadPathsVO;
import com.catalogue.my_spring_boot_project.modules.file_module.service.FileService;
import com.catalogue.my_spring_boot_project.modules.user_module.mapper.UserMapper;

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
    private final ImgMapper imgMapper;
    private final UserMapper userMapper;

    public FileServiceImpl(FileMapper fileMapper,
            CategoryMapper categoryMapper,
            ImgUtils imgUtils,
            FileUtils fileUtils,
            ImgMapper imgMapper,
            UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.categoryMapper = categoryMapper;
        this.imgUtils = imgUtils;
        this.fileUtils = fileUtils;
        this.imgMapper = imgMapper;
        this.userMapper = userMapper;
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
            if (listItemVO.getDescription().length() > 30) {
                listItemVO.setDescription(listItemVO.getDescription().substring(0, 30) + "...");
            }
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
    public Result<FileDetailVO> getDetail(Long id) {
        FileEntity selectById = fileMapper.selectById(id);
        if (selectById == null) {
            return Result.error(-1, "文件不存在");
        }
        FileDetailVO fileDetailVO = new FileDetailVO(selectById);
        QueryWrapper<ImgInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", id);
        List<ImgInfoEntity> imgs = imgMapper.selectList(queryWrapper);

        String[] imgsBase64 = new String[imgs.size()];
        for (ImgInfoEntity img : imgs) {
            imgsBase64[imgs.indexOf(img)] = imgUtils.getImg(img.getImgPath()).getData();
        }
        fileDetailVO.setImgsBase64(imgsBase64);

        fileDetailVO.setCategory(categoryMapper.selectById(selectById.getFileCategoryId()).getCategoryName());

        QueryWrapper <UserEntity> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.select("name");
        userQueryWrapper.eq("id", selectById.getUploaderId());
        UserEntity user = userMapper.selectOne(userQueryWrapper);
        fileDetailVO.setUploader(user.getName());

        return Result.success(fileDetailVO);
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
        fileMapper.insert(file);

        if (file.getId() == null) {
            return Result.error(-2, "文件上传失败");
        }

        UploadPathsVO uploadUrlsVO = new UploadPathsVO();
        uploadUrlsVO.setFilePath(FILEPATH + "file" + file.getId());
        uploadUrlsVO.setImgsPath(INTRODUCEIMGS + "introduceImgs" + file.getId());
        uploadUrlsVO.setId(file.getId());
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
    public Result<String> uploadImgs(String path, MultipartFile[] files, Long id) {
        int count = 0;
        List<ImgInfoEntity> imgs = new ArrayList<>();
        for (MultipartFile file : files) {
            File f = new File(path, "img" + ".bin" + count);

            Result<String> uploadImg = imgUtils.uploadImg(file, f.toString());
            if (uploadImg.getCode() != 0) {
                Log.info(getClass(), "上传图片失败:{}", f.toString());
            } else {
                String originalFilename = file.getOriginalFilename(); // e.g. "picture.png"
                String prefix = imgUtils.getImgsDataPath(originalFilename);
                imgs.add(new ImgInfoEntity(id, prefix + f.toString()));
            }
            count++;
        }
        if (imgs.size() > 0) {
            imgMapper.insertBatch(imgs);
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

        Result<String> deleteGarbage = deleteGarbage(dto.getId());
        if (deleteGarbage.getCode() == 0) {
            return deleteGarbage;
        }
        Log.info(getClass(), "上传文件失败");
        return Result.error(-1, "文件校验失败");
    }

    @Override
    public Result<String> deleteFile(Path path) {
        Log.info(getClass(), "删除文件:{}", path.toString());
        if (Files.exists(path)) {
            try {
                Files.walk(path) // 遍历目录
                        .sorted((a, b) -> b.compareTo(a)) // 先删子目录再删父目录
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            } catch (IOException e) {
                Log.error(getClass(), "删除文件失败", e);
                return Result.error(-1, "文件删除失败1");
            }
        }
        return Result.success();
    }

    @Override
    public Result<String> deleteGarbage(Long id) {
        Path filePath = Paths.get(FILEPATH, "file" + id);
        Path imgsPath = Paths.get(INTRODUCEIMGS, "introduceImgs" + id);
        Result<String> delete1 = deleteFile(filePath);
        Result<String> delete2 = deleteFile(imgsPath);
        fileMapper.deleteById(id);
        if (delete1.getCode() != 0 || delete2.getCode() != 0) {
            Log.info(getClass(), "删除文件失败2");
            return Result.error(-1, "文件删除失败2");
        }
        return Result.success();
    }

}
