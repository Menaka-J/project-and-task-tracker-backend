//package com.projectmanagement.dto.request;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import lombok.Data;
//import java.time.LocalDate;
//
//@Data
//public class TaskRequest {
//    @NotBlank
//    private String title;
//
//    private String description;
//
//    @NotNull
//    private Long assigneeId;
//
//    private LocalDate deadline;
//}

package com.projectmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TaskRequest {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Long assigneeId;

    private LocalDate deadline;

    private String priority; // HIGH, MEDIUM, LOW
}