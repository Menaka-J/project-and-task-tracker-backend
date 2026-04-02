package com.projectmanagement.repository;

import com.projectmanagement.entity.ProjectMember;
import com.projectmanagement.entity.Project;
import com.projectmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    boolean existsByProjectAndUser(Project project, User user);
    Optional<ProjectMember> findByProjectAndUser(Project project, User user);
}