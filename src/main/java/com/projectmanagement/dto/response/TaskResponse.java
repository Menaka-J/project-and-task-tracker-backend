package com.projectmanagement.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private LocalDate deadline;
    private LocalDateTime createdAt;
    private AssigneeInfo assignee;
    private CreatorInfo createdBy;

    @Data
    public static class AssigneeInfo {
        private Long id;
        private String name;
        private String email;
    }

    @Data
    public static class CreatorInfo {
        private Long id;
        private String name;
    }
}