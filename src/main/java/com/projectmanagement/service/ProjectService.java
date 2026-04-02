package com.projectmanagement.service;

import com.projectmanagement.dto.request.AddMemberRequest;
import com.projectmanagement.dto.request.ProjectRequest;
import com.projectmanagement.dto.response.ProjectResponse;
import com.projectmanagement.dto.response.UserResponse;
import com.projectmanagement.entity.Project;
import com.projectmanagement.entity.ProjectMember;
import com.projectmanagement.entity.User;
import com.projectmanagement.repository.ProjectMemberRepository;
import com.projectmanagement.repository.ProjectRepository;
import com.projectmanagement.repository.UserRepository;
import com.projectmanagement.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    private User getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private ProjectResponse convertToResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setCreatedAt(project.getCreatedAt());

        // Set created by
        if (project.getCreatedBy() != null) {
            ProjectResponse.UserInfo createdBy = new ProjectResponse.UserInfo();
            createdBy.setId(project.getCreatedBy().getId());
            createdBy.setName(project.getCreatedBy().getName());
            createdBy.setEmail(project.getCreatedBy().getEmail());
            response.setCreatedBy(createdBy);
        }

        // Set members
        List<ProjectResponse.UserInfo> members = project.getMembers().stream()
                .map(pm -> {
                    ProjectResponse.UserInfo userInfo = new ProjectResponse.UserInfo();
                    userInfo.setId(pm.getUser().getId());
                    userInfo.setName(pm.getUser().getName());
                    userInfo.setEmail(pm.getUser().getEmail());
                    return userInfo;
                })
                .collect(Collectors.toList());
        response.setMembers(members);

        // Set total tasks
        response.setTotalTasks(project.getTasks() != null ? project.getTasks().size() : 0);

        return response;
    }

    public ProjectResponse createProject(ProjectRequest request) {
        User currentUser = getCurrentUser();

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreatedBy(currentUser);

        Project savedProject = projectRepository.save(project);

        // Add creator as a member automatically
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(savedProject);
        projectMember.setUser(currentUser);
        projectMemberRepository.save(projectMember);

        return convertToResponse(savedProject);
    }

    public List<ProjectResponse> getUserProjects() {
        User currentUser = getCurrentUser();
        List<Project> projects = projectRepository.findProjectsByUser(currentUser);
        return projects.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Transactional
    public void addMember(Long projectId, AddMemberRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if already a member
        if (projectMemberRepository.existsByProjectAndUser(project, user)) {
            throw new RuntimeException("User is already a member of this project");
        }

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setUser(user);
        projectMemberRepository.save(projectMember);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name()))
                .collect(Collectors.toList());
    }
}