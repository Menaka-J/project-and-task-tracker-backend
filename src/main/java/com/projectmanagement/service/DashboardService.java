package com.projectmanagement.service;

import com.projectmanagement.dto.response.DashboardResponse;
import com.projectmanagement.entity.User;
import com.projectmanagement.enums.TaskStatus;
import com.projectmanagement.repository.ProjectRepository;
import com.projectmanagement.repository.TaskRepository;
import com.projectmanagement.repository.UserRepository;
import com.projectmanagement.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public DashboardResponse getDashboardStats() {
        User currentUser = getCurrentUser();

        long totalProjects = projectRepository.findProjectsByUser(currentUser).size();
        long totalTasks = taskRepository.count();
        long completedTasks = taskRepository.countByStatus(TaskStatus.DONE);
        long pendingTasks = taskRepository.countByStatus(TaskStatus.TO_DO) +
                taskRepository.countByStatus(TaskStatus.IN_PROGRESS);

        return new DashboardResponse(totalProjects, totalTasks, completedTasks, pendingTasks);
    }
}