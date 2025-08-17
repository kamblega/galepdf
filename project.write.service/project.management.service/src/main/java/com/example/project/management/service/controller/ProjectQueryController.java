package com.example.project.management.service.controller;

import com.example.project.management.service.entity.Project;
import com.example.project.management.service.entity.Student;
import com.example.project.management.service.service.ProjectQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/query")
public class ProjectQueryController {
    @Autowired
    private ProjectQueryService projectQueryService;

    @GetMapping("/students/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(projectQueryService.getAllStudents());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectQueryService.getAllProjects());
    }

    @GetMapping("/id/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable String projectId) {
        return ResponseEntity.ok(projectQueryService.getProjectById(projectId));
    }

    @GetMapping("/name/{projectName}")
    public ResponseEntity<List<Project>> getProjectByName(@PathVariable String projectName) {
        return ResponseEntity.ok(projectQueryService.getProjectByName(projectName));
    }

    @GetMapping("/staff-email/{staffEmail}")
    public ResponseEntity<List<Project>> getProjectsByStaffEmail(@PathVariable String staffEmail) {
        return ResponseEntity.ok(projectQueryService.getProjectsByStaffEmail(staffEmail));
    }

    @GetMapping("/staff-name/{staffName}")
    public ResponseEntity<List<Project>> getProjectsByStaffName(@PathVariable String staffName) {
        return ResponseEntity.ok(projectQueryService.getProjectsByStaffName(staffName));
    }

    @GetMapping("/domain/{projectDomain}")
    public ResponseEntity<List<Project>> getProjectsByDomain(@PathVariable String projectDomain) {
        return ResponseEntity.ok(projectQueryService.getProjectsByDomain(projectDomain));
    }

    @GetMapping("/level/{projectLevel}")
    public ResponseEntity<List<Project>> getProjectsByLevel(@PathVariable String projectLevel) {
        return ResponseEntity.ok(projectQueryService.getProjectsByLevel(projectLevel));
    }

    @GetMapping("/level-and-email")
    public ResponseEntity<List<Project>> getProjectsByLevelAndStaffEmail(
            @RequestParam String projectLevel,
            @RequestParam String staffEmail) {
        return ResponseEntity.ok(projectQueryService.getProjectsByLevelAndStaffEmail(projectLevel, staffEmail));
    }
}
