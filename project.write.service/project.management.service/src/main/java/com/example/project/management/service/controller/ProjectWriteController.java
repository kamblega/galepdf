package com.example.project.management.service.controller;

import com.example.project.management.service.dto.ProjectDto;
import com.example.project.management.service.entity.Project;
import com.example.project.management.service.service.ProjectWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/write")
public class ProjectWriteController {
    @Autowired
    private ProjectWriteService projectWriteService;

    @PostMapping("/create-project")
    public ResponseEntity<Project> createProject(@RequestBody ProjectDto projectDto) {
        return ResponseEntity.ok(projectWriteService.createProject(projectDto));
    }

    @PutMapping("/approve/{projectId}")
    public ResponseEntity<Project> approveProject(@PathVariable String projectId) {
        return ResponseEntity.ok(projectWriteService.approveProject(projectId));
    }

    @PutMapping("/request-supervisors/{projectId}")
    public ResponseEntity<Void> requestSecondSupervisors(
            @PathVariable String projectId,
            @RequestBody List<String> supervisorEmails) {
        projectWriteService.requestSecondSupervisors(projectId, supervisorEmails);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/id/{projectId}")
    public ResponseEntity<Project> updateProjectByID(
            @PathVariable String projectId,
            @RequestBody ProjectDto projectDto) {
        return ResponseEntity.ok(projectWriteService.updateProjectByID(projectId, projectDto));
    }

    @PutMapping("/name/{projectName}")
    public ResponseEntity<Project> updateProjectByName(
            @PathVariable String projectName,
            @RequestBody ProjectDto projectDto) {
        return ResponseEntity.ok(projectWriteService.updateProjectByName(projectName, projectDto));
    }

    @DeleteMapping("/id/{projectId}")
    public ResponseEntity<Void> deleteProjectByID(@PathVariable String projectId) {
        projectWriteService.deleteProjectByID(projectId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/name/{projectName}")
    public ResponseEntity<Void> deleteProjectByName(@PathVariable String projectName) {
        projectWriteService.deleteProjectByName(projectName);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/staff-name/{staffName}")
    public ResponseEntity<Void> deleteProjectByStaffName(@PathVariable String staffName) {
        projectWriteService.deleteProjectByStaffName(staffName);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/staff-email/{staffEmail}")
    public ResponseEntity<Void> deleteProjectByStaffEmailID(@PathVariable String staffEmail) {
        projectWriteService.deleteProjectByStaffEmailID(staffEmail);
        return ResponseEntity.noContent().build();
    }
}