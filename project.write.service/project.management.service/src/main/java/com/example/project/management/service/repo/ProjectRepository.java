package com.example.project.management.service.repo;

import com.example.project.management.service.entity.Project; // Ensure this import exists
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAll(); // Corrected generic type

    List<Project> findByStaffEmail(String staffEmail);
    List<Project> findByStaffName(String staffName);
    List<Project> findByProjectDomain( String projectDomain);
    List<Project> findByProjectLevel(String projectLevel);
    List<Project> findByProjectLevelAndStaffEmail(String projectLevel, String staffEmail);

    List<Project> findByProjectName ( String projectName );
}