package com.catalogue.my_spring_boot_project.modules.file_module.pojo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileRequestDTO {
    private Long needPage;
    private Long currentVersion;
    private Boolean isFiltered;
    private FileFilterCondition filters;

    @NoArgsConstructor
    @Data
    public class FileFilterCondition {
        String searchTerm;
        Long categoryCode;
        String dateRange;
        String order;

        LocalDateTime startDate;

        public void setDateRange() {
            if (dateRange != null) {
                switch (dateRange) {
                    case "last24Hours":
                        startDate = LocalDateTime.now().minusDays(1);
                        break;

                    case "last7Days":
                        startDate = LocalDateTime.now().minusDays(7);
                        break;

                    case "last30Days":
                        startDate = LocalDateTime.now().minusDays(30);
                        break;

                    case "last90Days":
                        startDate = LocalDateTime.now().minusDays(90);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
