//package com.projectmanagement.repository;
//
//import com.projectmanagement.entity.Task;
//import com.projectmanagement.entity.Project;
//import com.projectmanagement.enums.TaskStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import java.util.List;
//
//@Repository
//public interface TaskRepository extends JpaRepository<Task, Long> {
//    List<Task> findByProject(Project project);
//    List<Task> findByProjectAndStatus(Project project, TaskStatus status);
//    long countByStatus(TaskStatus status);
//}

package com.projectmanagement.repository;

import com.projectmanagement.entity.Task;
import com.projectmanagement.entity.Project;
import com.projectmanagement.entity.User;
import com.projectmanagement.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProject(Project project);

    List<Task> findByProjectAndStatus(Project project, TaskStatus status);

    // Count by single status
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status")
    long countByStatus(@Param("status") TaskStatus status);

    // Count by multiple statuses
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status IN :statuses")
    long countByStatusIn(@Param("statuses") List<TaskStatus> statuses);

    // For MEMBER - tasks assigned to them
    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignee = :user")
    long countByAssignee(@Param("user") User user);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignee = :user AND t.status = :status")
    long countByAssigneeAndStatus(@Param("user") User user, @Param("status") TaskStatus status);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignee = :user AND t.status IN :statuses")
    long countByAssigneeAndStatusIn(@Param("user") User user, @Param("statuses") List<TaskStatus> statuses);

    // Get tasks by assignee
    @Query("SELECT t FROM Task t WHERE t.assignee = :user")
    List<Task> findTasksByAssignee(@Param("user") User user);
}