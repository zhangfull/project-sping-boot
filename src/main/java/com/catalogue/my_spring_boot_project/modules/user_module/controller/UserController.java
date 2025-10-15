package com.catalogue.my_spring_boot_project.modules.user_module.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.catalogue.my_spring_boot_project.modules.common.entity.UserEntity;
import com.catalogue.my_spring_boot_project.modules.common.utils.ImgUtils;
import com.catalogue.my_spring_boot_project.modules.common.utils.Log;
import com.catalogue.my_spring_boot_project.modules.common.utils.ThreadLocalUtil;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.user_module.mapper.UserMapper;
import com.catalogue.my_spring_boot_project.modules.user_module.pojo.dto.UpdatePasswordDTO;
import com.catalogue.my_spring_boot_project.modules.user_module.pojo.dto.UploadUserDTO;
import com.catalogue.my_spring_boot_project.modules.user_module.service.UserService;
import com.catalogue.my_spring_boot_project.security.CustomUserDetail;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    private final ImgUtils imgUtils;
    private final UserService userService;

    public UserController(ImgUtils imgUtils, UserService userService) {
        this.imgUtils = imgUtils;
        this.userService = userService;
    }

    @GetMapping("/getInfo")
    public Result<UserEntity> getUserInfo(HttpServletRequest request) {
        return userService.getUserInfo();
    }

    @PostMapping("/update")
    public Result<String> updateUserInfo(@RequestBody UploadUserDTO dto, BindingResult bindingResult) {
        return userService.updateUserInfo(dto);
    }

    @PostMapping("/updatePassword")
    public Result<String> updatePassword(@RequestBody UpdatePasswordDTO dto, BindingResult bindingResult) {
        return userService.updatePassword(dto);
    }

    @PostMapping("/getAvatarBase64")
    public Result<String> getAvatarBase64(@RequestBody Map<String, String> body) {
        return imgUtils.getImg(body.get("url"));
    }

    @Value("${file.img.avatarPath}")
    private String AVATARPATH;

    @PostMapping("/uploadAvatar")
    public Result<String> uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        return userService.uploadAvatar(file);
    }

    

}
