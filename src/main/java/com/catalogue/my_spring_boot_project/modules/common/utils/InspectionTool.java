package com.catalogue.my_spring_boot_project.modules.common.utils;

public class InspectionTool {
    
    public static boolean stringIsEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean numberIsEmpty(Number number) {
        return number == null || number.longValue() == 0;
    }
}
