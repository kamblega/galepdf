package gale.shapley.service;

import gale.shapley.model.Project;
import gale.shapley.model.Student;
import gale.shapley.repository.ProjectRepository;
import gale.shapley.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GaleShapleyServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private GaleShapleyService galeShapleyService;

    @Test
    public void testMatch() {
        // Given
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Student 1");
        Map<String, String> student1Skills = new HashMap<>();
        student1Skills.put("Java", "high");
        student1.setSkills(student1Skills);

        Project project1 = new Project();
        project1.setId(1L);
        project1.setProjectName("Project 1");
        Map<String, String> project1Skills = new HashMap<>();
        project1Skills.put("Java", "high");
        project1.setRequiredSkills(project1Skills);

        when(studentRepository.findAll()).thenReturn(List.of(student1));
        when(projectRepository.findAll()).thenReturn(List.of(project1));

        // When
        Map<Student, Project> matching = galeShapleyService.match();

        // Then
        assertEquals(1, matching.size());
        assertEquals(project1, matching.get(student1));
    }
}
