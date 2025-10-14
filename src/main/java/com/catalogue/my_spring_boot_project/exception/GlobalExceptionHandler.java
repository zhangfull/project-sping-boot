package com.catalogue.my_spring_boot_project.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.catalogue.my_spring_boot_project.modules.common.utils.Log;
import com.catalogue.my_spring_boot_project.modules.common.vo.Result;


@RestControllerAdvice // 让这个类成为全局异常处理器
public class GlobalExceptionHandler {

    @ExceptionHandler(OperationException.class)
    public Result<String> handleOperationException(OperationException ex) {
        Log.info(getClass(), "["+ex.getCode()+"]", ex.getMessage());
        if (ex.getCode() == -1) {
           Log.error(getClass(), "后端操作失败：{}", ex);
           return Result.error(110, "操作失败，服务器异常");
        }
        return Result.error(ex.getCode(), "操作失败：" + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<String> handleGeneralException(Exception ex) {
        Log.error(getClass(), "未知错误：{}", ex);
        return Result.error(999, "操作失败，请重试");
    }
}