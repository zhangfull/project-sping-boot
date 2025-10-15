package com.catalogue.my_spring_boot_project.modules.user_module.controller;

import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catalogue.my_spring_boot_project.modules.common.utils.Log;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;
import com.catalogue.my_spring_boot_project.modules.user_module.pojo.dto.RegisterDTO;
import com.catalogue.my_spring_boot_project.modules.user_module.pojo.vo.LoginVO;
import com.catalogue.my_spring_boot_project.modules.user_module.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<String> register(@Validated @RequestBody RegisterDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 收集所有错误信息
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();
            Log.info(getClass(), "注册参数验证errors: {}", errors);
            return Result.error(-111, "注册参数验证失败");
        }
        return userService.register(dto.getUsername(), dto.getEmail(), dto.getPassword());
    }

    @PostMapping("/initialize")
    public Result<LoginVO> initializeToken(
            @CookieValue(value = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null) {
            return Result.error(-1, "token无效");
        }
        return userService.refreshLogin(refreshToken);
    }

    @PostMapping("/refresh")
    public Result<LoginVO> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Authorization");
        if (refreshToken == null) {
            return Result.error(-1, "token无效");
        }
        return userService.refreshLogin(refreshToken);
    }

    @PostMapping("/test")
    public void postMethodName() {
        Log.info(getClass(), "test");
    }
    

}
