package com.catalogue.my_spring_boot_project.modules.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static void info(Class<?> clazz, String message, Object... args) {
        getLogger(clazz).info("[日志] " + message, args);
    }

    public static void error(Class<?> clazz, String message, Throwable ex) {
        getLogger(clazz).error("[自定义日志] " + message, ex);
    }

}