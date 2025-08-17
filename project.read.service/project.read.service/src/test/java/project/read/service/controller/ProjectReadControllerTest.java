package project.read.service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import project.read.service.entity.Project;
import project.read.service.service.ProjectReadService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectReadController.class)
public class ProjectReadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectReadService projectReadService;

    @Test
    public void testGetProjectsByLevel() throws Exception {
        Project project = new Project();
        project.setProjectLevel("BSc Diss");
        List<Project> projects = Collections.singletonList(project);

        when(projectReadService.getProjectsByLevel("BSc Diss")).thenReturn(projects);

        mockMvc.perform(get("/read/level/BSc Diss"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProjectsByLevelAndStaffEmail() throws Exception {
        Project project = new Project();
        project.setProjectLevel("BSc PP");
        project.setStaffEmail("test@test.com");
        List<Project> projects = Collections.singletonList(project);

        when(projectReadService.getProjectsByLevelAndStaffEmail("BSc PP", "test@test.com")).thenReturn(projects);

        mockMvc.perform(get("/read/level/BSc PP/staff/test@test.com"))
                .andExpect(status().isOk());
    }
}
