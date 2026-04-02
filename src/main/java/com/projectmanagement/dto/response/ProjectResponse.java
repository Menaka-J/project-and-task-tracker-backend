package com.projectmanagement.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private UserInfo createdBy;
    private List<UserInfo> members;
    private Integer totalTasks;

    @Data
    public static class UserInfo {
        private Long id;
        private String name;
        private String email;
    }
}