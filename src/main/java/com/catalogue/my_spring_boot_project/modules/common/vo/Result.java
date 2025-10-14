package com.catalogue.my_spring_boot_project.modules.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    // 成功返回，有数据
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "success", data);
    }
    // 成功返回，无数据
    public static <E> Result<E> success() {
        return new Result<>(0, "success", null);
    }
    // 失败返回，只有错误信息
    public static <E> Result<E> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
