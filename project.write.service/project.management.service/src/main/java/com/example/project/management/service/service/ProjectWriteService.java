package com.example.project.management.service.service;

import com.example.project.management.service.dto.ProjectDto;
import com.example.project.management.service.entity.Project;
import com.example.project.management.service.repo.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectWriteService {
    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public Project createProject(ProjectDto projectDto) {
        Project project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setProjectDescription(projectDto.getProjectDescription());
        project.setProjectDomain(projectDto.getProjectDomain());
        project.setReadingSources(projectDto.getReadingSources());
        project.setProjectLevel(projectDto.getProjectLevel());
        project.setSecondSupervisors(projectDto.getSecondSupervisors());
        project.setProjectStatus(projectDto.isProjectStatus());
        project.setStudentPreferences(projectDto.getStudentPreferences());
        project.setStaffName(projectDto.getStaffName());
        project.setStaffEmail(projectDto.getStaffEmail());
        project.setSchool(projectDto.getSchool());
        project.setDepartment(projectDto.getDepartment());
        project.setPreference(projectDto.getPreference());
        return projectRepository.save(project);
    }

    @Transactional
    public Project approveProject(String projectId) {
        Long id = Long.parseLong(projectId);
        Project project = projectRepository.findById(id).orElseThrow();
        project.setProjectStatus(true);
        return projectRepository.save(project);
    }

    @Transactional
    public void requestSecondSupervisors(String projectId, List<String> supervisorEmails) {
        Long id = Long.parseLong(projectId);
        Project project = projectRepository.findById(id).orElseThrow();
        project.setSecondSupervisors(supervisorEmails);
        projectRepository.save(project);

    }

    @Transactional
    public Project updateProjectByID(String projectId, ProjectDto updatedProject) {
        Long id = Long.parseLong(projectId);
        Project project = projectRepository.findById(id).orElseThrow();
        project.setProjectName(updatedProject.getProjectName());
        project.setProjectDescription(updatedProject.getProjectDescription());
        project.setPreference(updatedProject.getPreference());
        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProjectByName(String projectName, ProjectDto updatedProject) {
        Project project = projectRepository.findByProjectName(projectName).stream().findFirst().orElseThrow();
        project.setProjectDescription(updatedProject.getProjectDescription());
        project.setPreference(updatedProject.getPreference());
        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProjectByID(String projectId) {
        Long id = Long.parseLong(projectId);
        projectRepository.deleteById(id);
    }

    @Transactional
    public void deleteProjectByName(String projectName) {
        projectRepository.findByProjectName(projectName).forEach(projectRepository::delete);
    }

    @Transactional
    public void deleteProjectByStaffName(String staffName) {
        projectRepository.findByStaffName(staffName).forEach(projectRepository::delete);
    }

    @Transactional
    public void deleteProjectByStaffEmailID(String staffEmail) {
        projectRepository.findByStaffEmail(staffEmail).forEach(projectRepository::delete);
    }
}
