package com.catalogue.my_spring_boot_project.config.constant;

public final class SecurityWhitelist {
    private SecurityWhitelist() {
        // 防止被实例化
    }

    /** 放行的 URL 列表（不需要登录验证） */
    public static final String[] PUBLIC_URLS = {
            "/login/register",
            "/login/refresh",
            "/login/active",
            "/login/test",
            "/login/initialize",
            "/file/getFiles",
            "/file/getCategory",
            "/user/getAvatarBase64"
    };
}
