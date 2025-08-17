package com.example.project.management.service.service;

import com.example.project.management.service.entity.Project;
import com.example.project.management.service.entity.Student;
import com.example.project.management.service.repo.ProjectRepository;
import com.example.project.management.service.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectQueryService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(String projectId) {
        Long id = Long.parseLong(projectId);
        return projectRepository.findById(id).orElseThrow();
    }

    public List<Project> getProjectByName(String projectName) {
        return projectRepository.findByProjectName(projectName);
    }

    public List<Project> getProjectsByStaffEmail(String staffEmail) {
        return projectRepository.findByStaffEmail(staffEmail);
    }

    public List<Project> getProjectsByStaffName(String staffName) {
        return projectRepository.findByStaffName(staffName);
    }

    public List<Project> getProjectsByDomain(String projectDomain) {
        return projectRepository.findByProjectDomain(projectDomain);
    }

    public List<Project> getProjectsByLevel(String projectLevel) {
        return projectRepository.findByProjectLevel(projectLevel);
    }

    public List<Project> getProjectsByLevelAndStaffEmail(String projectLevel, String staffEmail) {
        return projectRepository.findByProjectLevelAndStaffEmail(projectLevel, staffEmail);
    }
}
