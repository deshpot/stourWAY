package com.qzsitu.stourway.test;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContextTest.xml")
public class MyBusinessProcessTest {
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;

	//@Test
	public void TaskProcessTest() {
		Execution execution;
		Task task;
		Map<String, Object> taskForm;
		
		List<Execution> executions = runtimeService.createExecutionQuery().list();
		for(int i = 0; i < executions.size(); i++){
			execution = executions.get(i);
			System.out.println(execution.getProcessInstanceId());
			
		}
		List<Task> tasks = taskService.createTaskQuery().list();
		for(int i = 0; i < tasks.size(); i++){
			task = tasks.get(i);
			System.out.println(task.getId());
			System.out.println(task.getAssignee());
			System.out.println(task.getName());
		}
		
		Map<String, Object> initForm = new HashMap<String, Object>();
		initForm.put("initiator", "duhongtu");
		initForm.put("assignee", "duhongke");
		initForm.put("description", "这是一个测试用的任务");
		initForm.put("comment", "请认真执行任务");
		runtimeService.startProcessInstanceByKey("taskProcess", initForm);
		task = taskService.createTaskQuery().taskAssignee("duhongke").singleResult();
		assertEquals("签收任务", task.getName());
		
		assertEquals("这是一个测试用的任务", runtimeService.getVariable(task.getExecutionId(), "taskDescription"));
		assertEquals("请认真执行任务", runtimeService.getVariable(task.getExecutionId(), "comment"));
		
		taskForm = new HashMap<String, Object>();
		taskForm.put("decision", "nok");
		taskForm.put("comment", "这个任务太难了");
		taskService.complete(task.getId(), taskForm);
		
		task = taskService.createTaskQuery().taskAssignee("duhongtu").singleResult();
		assertEquals("验收任务", task.getName());
		assertEquals("这个任务太难了",
				runtimeService.getVariable(task.getExecutionId(), "comment"));
		taskForm = new HashMap<String, Object>();
		taskForm.put("decision", "nok");
		taskForm.put("comment", "任务内容减少，请完成");
		taskService.complete(task.getId(), taskForm);
		
		task = taskService.createTaskQuery().taskAssignee("duhongke").singleResult();
		assertEquals("签收任务", task.getName());
		taskForm = new HashMap<String, Object>();
		taskForm.put("decision", "ok");
		taskForm.put("comment", "任务预计一个月完成");
		taskService.complete(task.getId(), taskForm);
		
		task = taskService.createTaskQuery().taskAssignee("duhongke").singleResult();
		assertEquals("完成任务", task.getName());
		taskForm = new HashMap<String, Object>();
		taskForm.put("comment", "任务已顺利完成");
		taskService.complete(task.getId(), taskForm);
		
		task = taskService.createTaskQuery().taskAssignee("duhongtu").singleResult();
		assertEquals("验收任务", task.getName());
		taskForm = new HashMap<String, Object>();
		taskForm.put("decision", "ok");
		taskForm.put("comment", "任务验收通过");
		taskService.complete(task.getId(), taskForm);
	}

//	@Test
	public void MutipleTaskProcessTest(){
		Execution execution;
		Task task;
		
		List<Execution> executions = runtimeService.createExecutionQuery().list();
		for(int i = 0; i < executions.size(); i++){
			execution = executions.get(i);
			System.out.println(execution.getProcessInstanceId());
		}
		
		List<Task> tasks = taskService.createTaskQuery().list();
		for(int i = 0; i < tasks.size(); i++){
			task = tasks.get(i);
			System.out.println(task.getId());
			System.out.println(task.getAssignee());
			System.out.println(task.getName());
			Map<String, Object> taskForm = new HashMap<String, Object>();
			taskForm.put("decision", "ok");
			taskForm.put("initiator", "duhongtu");
			taskForm.put("comment", "这个任务太难了");
			taskService.complete(task.getId(), taskForm);
		}
		
		Map<String, Object> initForm = new HashMap<String, Object>();
		initForm.put("initiator", "duhongtu");
		List<List<String>> taskList = new ArrayList<List<String>>();
		List<String> taskDef = new ArrayList<String>();
		taskDef.add("dujinlong");
		taskDef.add("测试任务");
		taskDef.add("请认真对待");
		taskList.add(taskDef);
		initForm.put("taskList", taskList);
		runtimeService.startProcessInstanceByKey("mainProjectProcess", initForm);
		
		task = taskService.createTaskQuery().taskAssignee("dujinlong").singleResult();
		assertEquals("acceptTask", task.getName());
		
		assertEquals("测试任务", runtimeService.getVariable(task.getExecutionId(), "description"));
		assertEquals("请认真对待", runtimeService.getVariable(task.getExecutionId(), "comment"));
		
		Map<String, Object> taskForm = new HashMap<String, Object>();
		taskForm.put("decision", "ok");
		taskForm.put("comment", "这个任务太难了");
		taskService.complete(task.getId(), taskForm);
	}
	
//	@Test
	public void TestHistoryVariables(){
		List<Map<String, Object>> workProcessInfoList = new ArrayList<Map<String, Object>>();
		List<HistoricProcessInstance> historyProcessList = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey("taskProcess").involvedUser("duhongtu").orderByProcessInstanceEndTime().desc().list();
		
		for (HistoricProcessInstance p : historyProcessList) {
			Map<String, Object> workProcessInfo = new HashMap<String, Object>();
			workProcessInfo.put("initiator", (String)(historyService.createHistoricVariableInstanceQuery().processInstanceId(p.getId())
            		.variableName("initiator").singleResult().getValue()));
			workProcessInfo.put("startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(p.getStartTime()));
			workProcessInfo.put("endTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(p.getEndTime()));
			workProcessInfo.put("description", (String)(historyService.createHistoricVariableInstanceQuery().processInstanceId(p.getId())
            		.variableName("description").singleResult().getValue()));
			workProcessInfo.put("processId", p.getId());
			System.out.println(workProcessInfo.get("initiator") + " : " + workProcessInfo.get("description"));
			workProcessInfoList.add(workProcessInfo);
        }
	}
	
	@Test
	public void testDeleteProcess(){
//		String test="-=STSD=--=STSD=--=-=STSD=-=-";
//		List<List<String>> taskList = new ArrayList<List<String>>();
//		for(String taskInfo : test.split("-=-=STSD=-=-")) {
//			List<String> task = new ArrayList<String>();
//			String[] infos = taskInfo.split("-=STSD=-");
//			System.out.println(infos.length);
//			task.add(infos[0]);
//			task.add(infos[1]);
//			task.add(infos[2]);
//			System.out.println(infos[0]+":"+infos[1]+":"+infos[2]);
//			taskList.add(task);
//		}
//		taskService.complete("7278");
//		List<List<String>> taskList = (List<List<String>>) runtimeService.getVariable("7508", "taskList");
//		System.out.println(taskList.get(1).get(0));
//		System.out.println(taskList.get(1).get(1));
//		System.out.println(taskList.get(1).get(2));
//		System.out.println(taskList.get(1).get(3));
//		runtimeService.deleteProcessInstance("7571", "异常流程");
//		historyService.deleteHistoricProcessInstance("7571");
//		runtimeService.deleteProcessInstance("7584", "异常流程");
//		historyService.deleteHistoricProcessInstance("7584");
		runtimeService.deleteProcessInstance("2843", "异常流程");
		historyService.deleteHistoricProcessInstance("2843");
		runtimeService.deleteProcessInstance("2801", "异常流程");
		historyService.deleteHistoricProcessInstance("2801");
		runtimeService.deleteProcessInstance("469", "异常流程");
		historyService.deleteHistoricProcessInstance("469");
	}
	
	
}
