package project.read.service.controller;

import project.read.service.entity.Project;
import project.read.service.service.ProjectReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/read")
public class ProjectReadController {
    @Autowired
    private ProjectReadService projectReadService;

    @GetMapping("/id/{projectId}")
    public ResponseEntity<Project> getProjectByID(@PathVariable String projectId) {
        Project project = projectReadService.getProjectByID(projectId);
        return project != null ? ResponseEntity.ok(project) : ResponseEntity.notFound().build();
    }


    @GetMapping("/name/{projectName}")
    public ResponseEntity<List<Project>> getProjectByName(@PathVariable String projectName) {
        return ResponseEntity.ok(projectReadService.getProjectByName(projectName));
    }

    @GetMapping("/staff-email/{staffEmail}")
    public ResponseEntity<List<Project>> getAllProjectByStaffEmailId(@PathVariable String staffEmail) {
        return ResponseEntity.ok(projectReadService.getAllProjectByStaffEmailId(staffEmail));
    }

    @GetMapping("/staff-name/{staffName}")
    public ResponseEntity<List<Project>> getAllProjectByStaffName(@PathVariable String staffName) {
        return ResponseEntity.ok(projectReadService.getAllProjectByStaffName(staffName));
    }

    @GetMapping("/domain/{domain}")
    public ResponseEntity<List<Project>> getAllProjectByDomain(@PathVariable String domain) {
        return ResponseEntity.ok(projectReadService.getAllProjectByDomain(domain));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects(){
        List<Project> projects = projectReadService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/level/{projectLevel}")
    public ResponseEntity<List<Project>> getProjectsByLevel(@PathVariable String projectLevel) {
        return ResponseEntity.ok(projectReadService.getProjectsByLevel(projectLevel));
    }

    @GetMapping("/level/{projectLevel}/staff/{staffEmail}")
    public ResponseEntity<List<Project>> getProjectsByLevelAndStaffEmail(@PathVariable String projectLevel, @PathVariable String staffEmail) {
        return ResponseEntity.ok(projectReadService.getProjectsByLevelAndStaffEmail(projectLevel, staffEmail));
    }
}
