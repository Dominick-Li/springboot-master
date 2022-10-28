package com.ljm.boot.flowable;

import com.alibaba.fastjson.JSONObject;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class FlowableApplicationTests {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    /**
     * 1.部署流程
     */
    @Test
    public void createDeployment() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .deploy();
        System.out.println("流程Id="+deployment.getId());
    }

    /**
     * 2.获取流程定义
     */
    @Test
    public void getProcessDefinition() {
        ProcessDefinition result = repositoryService.createProcessDefinitionQuery()
                .deploymentId("2dbd2a98-5669-11ed-aad7-005056c00001")
                .singleResult();

        System.out.println("definitionId: " + result.getId());
    }

    /**
     * 3.启动流程
     */
    @Test
    public void startProcessDefinition() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employee", "张三");
        variables.put("nrOfHolidays", 3);
        variables.put("description", "有事请假");
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("holidayRequest", variables);
        System.out.println(processInstance.getBusinessStatus());
    }

    /**
     * 4.获取任务
     */
    @Test
    public void getTask() {
        List<Task> tasks = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                .taskCandidateOrAssigned("admin")
                .list();
        System.out.println("You have " + tasks.size() + " tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println((i + 1) + ") " + "taskId: " + task.getId() + ", taskName: " + task.getName());
        }
    }

    /**
     * 5.审批任务
     */
    @Test
    public void completeTask() {
        Map variables = new HashMap<String, Object>();
        variables.put("approved", true);
        taskService.complete("a63e77f3-5669-11ed-a9b4-005056c00001", variables);
    }

    /**
     * 6.查询历史任务
     */
    @Test
    public void historyTask() {
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .taskAssignee("admin")
                .finished()
                .list();
        list.stream().map(JSONObject::toJSONString).forEach(System.out::println);
    }




    @Test
    void contextLoads() {
    }

}
