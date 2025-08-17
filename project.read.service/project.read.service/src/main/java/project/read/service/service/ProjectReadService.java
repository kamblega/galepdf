package project.read.service.service;

import project.read.service.entity.Project;
import project.read.service.repo.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectReadService {
    @Autowired
    private ProjectRepository projectRepository;

    public Project getProjectByID(String projectId) {
        Long id = Long.parseLong(projectId);
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> getProjectByName(String projectName) {
        return projectRepository.findByProjectName(projectName);
    }

    public List<Project> getAllProjectByStaffEmailId(String staffEmail) {
        return projectRepository.findByStaffEmail(staffEmail);
    }

    public List<Project> getAllProjectByStaffName(String staffName) {
        return projectRepository.findByStaffName(staffName);
    }

    public List<Project> getAllProjectByDomain(String domain) {
        return projectRepository.findByProjectDomain(domain);
    }

    public List<Project> getAllProjects()
    {
        List<Project> projects = projectRepository.findAll();
        return projects;
    }

    public List<Project> getProjectsByLevel(String projectLevel) {
        return projectRepository.findByProjectLevel(projectLevel);
    }

    public List<Project> getProjectsByLevelAndStaffEmail(String projectLevel, String staffEmail) {
        return projectRepository.findByProjectLevelAndStaffEmail(projectLevel, staffEmail);
    }
}
