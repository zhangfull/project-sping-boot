package com.catalogue.my_spring_boot_project.modules.user_module.service;

import org.springframework.web.multipart.MultipartFile;

import com.catalogue.my_spring_boot_project.modules.common.entity.UserEntity;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.user_module.pojo.dto.UpdatePasswordDTO;
import com.catalogue.my_spring_boot_project.modules.user_module.pojo.dto.UploadUserDTO;
import com.catalogue.my_spring_boot_project.modules.user_module.pojo.vo.LoginVO;

public interface UserService {

    /**
     * 注册
     * 
     * @param username 用户名
     * 
     * @param email    邮箱
     * 
     * @param password 密码
     * 
     * @return 注册结果（code = 0 表示成功、 code = -1 重复注册、 code = -2 注册失败、 code = -111
     *         参数验证失败）
     */
    Result<String> register(String username, String email, String password);

    /**
     * 自动登录
     * 
     * @param refreshToken 长期token
     * 
     * @return 自动登录结果（code = 0 表示成功、 code = -1 token无效、 code = -2 用户不存在、 code = -3
     *         token生成失败）
     */
    Result<LoginVO> refreshLogin(String refreshToken);

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    Result<UserEntity> getUserInfo();

    /**
     * 上传头像
     * 
     * @param file 头像文件
     * @return 上传结果
     */
    Result<String> uploadAvatar(MultipartFile file);

    /**
     * 更新用户信息
     * 
     * @param dto 用户信息
     * @return 更新结果
     */
    Result<String> updateUserInfo(UploadUserDTO dto);

    /**
     * 更新密码
     * 
     * @param dto 更新密码信息
     * @return 更新结果
     */
    Result<String> updatePassword(UpdatePasswordDTO dto);
}
