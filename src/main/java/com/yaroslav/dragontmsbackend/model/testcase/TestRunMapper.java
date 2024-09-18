package com.yaroslav.dragontmsbackend.model.testcase;

import com.yaroslav.dragontmsbackend.model.project.Project;
import com.yaroslav.dragontmsbackend.model.testplan.TestPlan;
import com.yaroslav.dragontmsbackend.model.user.User;
import com.yaroslav.dragontmsbackend.repository.ProjectRepository;
import com.yaroslav.dragontmsbackend.repository.TestPlanRepository;
import com.yaroslav.dragontmsbackend.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class TestRunMapper {

    private final UserRepository userRepository;
    private final TestPlanRepository testPlanRepository;

    private final ProjectRepository projectRepository;

    public TestRunMapper(UserRepository userRepository, TestPlanRepository testPlanRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.testPlanRepository = testPlanRepository;
        this.projectRepository = projectRepository;
    }

    public TestRunDTO toTestRunDTO (TestRun testRun){
        User user = userRepository.findByEmail(testRun.getUserEmail()).orElse(null);
        TestPlan testPlan = testPlanRepository.findById(testRun.getTestPlanId()).orElse(null);
        Project project = projectRepository.findById(testRun.getProjectId()).orElse(null);
        TestRunDTO dto = new TestRunDTO();
        dto.setId(testRun.getId());
        if (user != null){
            dto.setUserName(user.getName());
        }else {
            dto.setUserName("Пользователь не найден");
        }

        if (testPlan != null){
            dto.setTestPlanName(testPlan.getName());
        }else {
            dto.setTestPlanName("Запуск производился не в тест-плане");
        }
        assert project != null;
        dto.setProjectName(project.getName());
        dto.setCreatedDate(testRun.getCreated().toString());
        dto.setAutotestResultList(testRun.getAutotestResults());

        return dto;
    }
}
