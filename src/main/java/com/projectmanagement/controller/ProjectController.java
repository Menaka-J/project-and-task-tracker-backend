package com.projectmanagement.controller;

import com.projectmanagement.dto.request.AddMemberRequest;
import com.projectmanagement.dto.request.ProjectRequest;
import com.projectmanagement.dto.response.ProjectResponse;
import com.projectmanagement.dto.response.UserResponse;
import com.projectmanagement.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getUserProjects() {
        return ResponseEntity.ok(projectService.getUserProjects());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.createProject(request));
    }

    @PostMapping("/{projectId}/members")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addMember(@PathVariable Long projectId, @Valid @RequestBody AddMemberRequest request) {
        projectService.addMember(projectId, request);
        return ResponseEntity.ok("Member added successfully");
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(projectService.getAllUsers());
    }
}