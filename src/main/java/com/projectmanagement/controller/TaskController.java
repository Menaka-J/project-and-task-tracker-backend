package com.projectmanagement.controller;

import com.projectmanagement.dto.request.TaskRequest;
import com.projectmanagement.dto.response.TaskResponse;
import com.projectmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponse>> getTasksByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId));
    }

    @PostMapping("/project/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> createTask(@PathVariable Long projectId, @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(projectId, request));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long taskId, @RequestBody Map<String, String> statusMap) {
        String status = statusMap.get("status");
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, status));
    }

    @GetMapping("/project/{projectId}/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@PathVariable Long projectId, @PathVariable String status) {
        return ResponseEntity.ok(taskService.getTasksByProjectAndStatus(projectId, status));
    }
}