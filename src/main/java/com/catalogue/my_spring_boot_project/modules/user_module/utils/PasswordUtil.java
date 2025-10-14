package com.catalogue.my_spring_boot_project.modules.user_module.utils;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    /**
     * 加密密码
     * @param plainPassword 明文密码
     * @return 加密后的密码
     */
    public static String encrypt(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * 验证密码
     * @param plainPassword 用户输入的明文密码
     * @param hashedPassword 数据库存储的加密密码
     * @return 是否匹配
     */
    public static boolean verify(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
    
}
