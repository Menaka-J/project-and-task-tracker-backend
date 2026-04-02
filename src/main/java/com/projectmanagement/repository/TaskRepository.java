package com.projectmanagement.repository;

import com.projectmanagement.entity.Task;
import com.projectmanagement.entity.Project;
import com.projectmanagement.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);
    List<Task> findByProjectAndStatus(Project project, TaskStatus status);
    long countByStatus(TaskStatus status);
}