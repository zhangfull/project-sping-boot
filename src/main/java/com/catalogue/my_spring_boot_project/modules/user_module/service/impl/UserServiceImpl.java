package com.catalogue.my_spring_boot_project.modules.user_module.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.catalogue.my_spring_boot_project.exception.OperationException;
import com.catalogue.my_spring_boot_project.modules.common.entity.UserEntity;
import com.catalogue.my_spring_boot_project.modules.common.utils.ImgUtils;
import com.catalogue.my_spring_boot_project.modules.common.utils.Log;
import com.catalogue.my_spring_boot_project.modules.common.utils.ThreadLocalUtil;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.user_module.mapper.UserMapper;
import com.catalogue.my_spring_boot_project.modules.user_module.pojo.dto.UpdatePasswordDTO;
import com.catalogue.my_spring_boot_project.modules.user_module.pojo.dto.UploadUserDTO;
import com.catalogue.my_spring_boot_project.modules.user_module.pojo.vo.LoginVO;
import com.catalogue.my_spring_boot_project.modules.user_module.service.UserService;
import com.catalogue.my_spring_boot_project.modules.user_module.utils.GeneratingUtils;
import com.catalogue.my_spring_boot_project.modules.user_module.utils.JwtUtil;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder pe;
    private final ImgUtils imgUtils;

    public UserServiceImpl(UserMapper userMapper, JwtUtil jwtUtil, PasswordEncoder pe, ImgUtils imgUtils) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.pe = pe;
        this.imgUtils = imgUtils;
    }

    @Override
    public Result<String> register(String username, String email, String password) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        boolean existsByEmail = userMapper.selectCount(queryWrapper) > 0;
        queryWrapper.clear();

        if (existsByEmail) {
            return Result.error(-1, String.format("邮箱已被注册"));
        } else {
            String uid = GeneratingUtils.generateUID();
            queryWrapper.eq("uid", uid);
            boolean success = false;
            for (int i = 0; i < 50; i++) {
                ;
                if (userMapper.selectCount(queryWrapper) == 0) {
                    success = true;
                    break;
                }
                uid = GeneratingUtils.generateUID();
            }
            if (!success) {
                return Result.error(-2, "注册失败,请稍后再试");
            }

            UserEntity user = new UserEntity();
            user.setId(0l);
            user.setName(username);
            user.setEmail(email);
            user.setPassword(pe.encode(password));
            user.setUid(uid);
            user.setRegistrationDate(LocalDateTime.now());
            user.setFollowersCount(0);
            user.setMyFollowersCount(0);
            int save = userMapper.insert(user);
            if (save == 0) {
                throw new OperationException(-3, "注册失败,请稍后再试");
            }
            Log.info(getClass(), "注册成功: {}", email);
            return Result.success("注册成功");
        }
    }

    @Value("${file.img.avatarPath}")
    private String AVATARPATH;

    @Override
    public Result<LoginVO> refreshLogin(String refreshToken) {
        String emailOrUid = jwtUtil.parseToken(refreshToken);
        if (emailOrUid == null) {
            return Result.error(-1, "token无效");
        }

        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", emailOrUid);
        queryWrapper.or().eq("uid", emailOrUid);
        UserEntity user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            Log.info(getClass(), "自动登录用户为空:{}", emailOrUid);
            return Result.error(-2, "用户不存在");
        }
        LoginVO loginResponse = new LoginVO();
        if (user.getAvatarUrl() == null || user.getAvatarUrl().isEmpty() || user.getAvatarUrl().trim().isEmpty()) {
            loginResponse.setAvatarUrl(AVATARPATH + "default.txt");
        } else {
            loginResponse.setAvatarUrl(user.getAvatarUrl());
        }

        loginResponse.setAccessToken(jwtUtil.generateToken(emailOrUid, 1000 * 60 * 10));
        if (loginResponse.getAccessToken() == null) {
            throw new OperationException(-3, "token生成失败");
        }
        return Result.success(loginResponse);
    }

    @Override
    public Result<UserEntity> getUserInfo() {
        UserEntity user = userMapper.selectById(ThreadLocalUtil.getLongId());
        if (user == null) {
            return Result.error(-1, "用户不存在");
        }
        user.setPassword(null);
        user.setId(null);
        return Result.success(user);
    }

    @Override
    public Result<String> getAvatar(String url) {
        Result<String> result = imgUtils.getImg(url);
        if (result.getCode() != 0) {
            Log.info(getClass(), "获取用户头像失败");
            Path path = Paths.get(AVATARPATH, "default.bin");
            return imgUtils.getImg("webp|" + path.toString());
        } else {
            return result;
        }
    }

    @Override
    public Result<String> uploadAvatar(MultipartFile file) {
        UserEntity user = userMapper.selectById(ThreadLocalUtil.getLongId());
        if (user == null) {
            return Result.error(-1, "用户不存在");
        }
        String path = AVATARPATH + user.getUid() + ".bin";
        String originalFilename = file.getOriginalFilename(); // e.g. "picture.png"
        String suffix = imgUtils.getImgsDataPath(originalFilename);

        user.setAvatarUrl(suffix + path);
        userMapper.updateById(user);
        Log.info(getClass(), "上传用户头像成功:{}", user.getAvatarUrl());
        return imgUtils.uploadImg(file, path);
    }

    @Override
    public Result<String> updateUserInfo(UploadUserDTO dto) {
        Log.info(getClass(), "用户更改用户名:{},用户介绍:{}", dto.getName(), dto.getSex());
        Long id = ThreadLocalUtil.getLongId();
        UserEntity user = userMapper.selectById(id);
        if (user == null) {
            return Result.error(-1, "用户不存在");
        }
        user.setName(dto.getName());
        user.setSex(dto.getSex());
        user.setBriefIntroduction(dto.getBriefIntroduction());
        userMapper.updateById(user);
        return Result.success();
    }

    @Override
    public Result<String> updatePassword(UpdatePasswordDTO dto) {
        Long id = ThreadLocalUtil.getLongId();
        UserEntity user = userMapper.selectById(id);
        if (user == null) {
            Log.info(getClass(), "更新用户密码失败：id:{} 的用户为空", id);
            return Result.error(-1, "用户不存在");
        }

        if (!pe.matches(dto.getOldPassword(), user.getPassword())) {
            return Result.error(-2, "旧密码错误");
        }

        user.setPassword(pe.encode(dto.getNewPassword()));
        userMapper.updateById(user);
        return Result.success();
    }

}
