package gale.shapley.controller;

import gale.shapley.model.Project;
import gale.shapley.model.Supervisor;
import gale.shapley.repository.ProjectRepository;
import gale.shapley.repository.SupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final SupervisorRepository supervisorRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository, SupervisorRepository supervisorRepository) {
        this.projectRepository = projectRepository;
        this.supervisorRepository = supervisorRepository;
    }

    @PostMapping
    public Project createProject(@RequestBody Project project) {
        Supervisor supervisor = supervisorRepository.findByEmail(project.getStaffEmail())
                .orElseGet(() -> {
                    Supervisor newSupervisor = new Supervisor();
                    newSupervisor.setEmail(project.getStaffEmail());
                    newSupervisor.setName(project.getStaffName());
                    return supervisorRepository.save(newSupervisor);
                });
        project.setSupervisor(supervisor);
        return projectRepository.save(project);
    }
}
