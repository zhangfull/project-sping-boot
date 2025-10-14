package com.catalogue.my_spring_boot_project.modules.common.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FilePage<T> {
    Long currentPage;
    Long totalPages;
    Integer pageSize;
    Long latestVersion;
    List<T> results;
}
