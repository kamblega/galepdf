package gale.shapley.controller;

import gale.shapley.model.Project;
import gale.shapley.model.Student;
import gale.shapley.service.GaleShapleyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/match")
public class MatchingController {

    private final GaleShapleyService galeShapleyService;

    @Autowired
    public MatchingController(GaleShapleyService galeShapleyService) {
        this.galeShapleyService = galeShapleyService;
    }

    @PostMapping
    public List<String> match() {
        Map<Student, Project> matches = galeShapleyService.match();
        List<String> result = new ArrayList<>();
        for (Map.Entry<Student, Project> entry : matches.entrySet()) {
            Student student = entry.getKey();
            Project project = entry.getValue();
            result.add(String.format("Student {%s} - Best match - Project {%d} - Project {%s}",
                    student.getName(), project.getId(), project.getProjectName()));
        }
        return result;
    }
}
