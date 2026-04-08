package com.projectmanagement.service;

import com.projectmanagement.dto.request.TaskRequest;
import com.projectmanagement.dto.response.TaskResponse;
import com.projectmanagement.entity.Project;
import com.projectmanagement.entity.Task;
import com.projectmanagement.entity.User;
import com.projectmanagement.enums.TaskStatus;
import com.projectmanagement.repository.ProjectRepository;
import com.projectmanagement.repository.TaskRepository;
import com.projectmanagement.repository.UserRepository;
import com.projectmanagement.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private TaskResponse convertToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus().name());
        response.setPriority(task.getPriority().name()); // ADD THIS LINE
        response.setDeadline(task.getDeadline());
        response.setCreatedAt(task.getCreatedAt());

        if (task.getAssignee() != null) {
            TaskResponse.AssigneeInfo assignee = new TaskResponse.AssigneeInfo();
            assignee.setId(task.getAssignee().getId());
            assignee.setName(task.getAssignee().getName());
            assignee.setEmail(task.getAssignee().getEmail());
            response.setAssignee(assignee);
        }

        if (task.getCreatedBy() != null) {
            TaskResponse.CreatorInfo creator = new TaskResponse.CreatorInfo();
            creator.setId(task.getCreatedBy().getId());
            creator.setName(task.getCreatedBy().getName());
            response.setCreatedBy(creator);
        }

        return response;
    }

    public TaskResponse createTask(Long projectId, TaskRequest request) {
        User currentUser = getCurrentUser();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("Assignee not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDeadline(request.getDeadline());
        task.setProject(project);
        task.setAssignee(assignee);
        task.setCreatedBy(currentUser);
        task.setStatus(TaskStatus.TO_DO);

        // FIX: Set priority properly
        System.out.println("Received priority: " + request.getPriority()); // Debug log

        if (request.getPriority() != null && !request.getPriority().isEmpty()) {
            try {
                task.setPriority(Task.Priority.valueOf(request.getPriority()));
                System.out.println("Priority set to: " + request.getPriority());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid priority, setting to MEDIUM");
                task.setPriority(Task.Priority.MEDIUM);
            }
        } else {
            task.setPriority(Task.Priority.MEDIUM);
            System.out.println("No priority provided, setting to MEDIUM");
        }

        Task savedTask = taskRepository.save(task);
        System.out.println("Saved task priority: " + savedTask.getPriority()); // Debug log

        return convertToResponse(savedTask);
    }

    public List<TaskResponse> getTasksByProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Task> tasks = taskRepository.findByProject(project);
        return tasks.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public TaskResponse updateTaskStatus(Long taskId, String status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.valueOf(status));
        Task updatedTask = taskRepository.save(task);
        return convertToResponse(updatedTask);
    }

    public List<TaskResponse> getTasksByProjectAndStatus(Long projectId, String status) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Task> tasks = taskRepository.findByProjectAndStatus(project, TaskStatus.valueOf(status));
        return tasks.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
}