package com.example.project.management.service.controller;

import com.example.project.management.service.entity.Student;
import com.example.project.management.service.service.StudentWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/write")
public class StudentController {

    @Autowired
    private StudentWriteService studentWriteService;

    @PostMapping("/create-student")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentWriteService.createStudent(student));
    }
}
