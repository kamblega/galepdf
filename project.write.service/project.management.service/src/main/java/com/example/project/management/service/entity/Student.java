package com.example.project.management.service.entity;

import jakarta.persistence.*;
import java.util.Map;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    @ElementCollection
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_id"))
    @MapKeyColumn(name = "skill_name")
    @Column(name = "skill_level")
    private Map<String, String> skills;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, String> getSkills() {
        return skills;
    }

    public void setSkills(Map<String, String> skills) {
        this.skills = skills;
    }
}
