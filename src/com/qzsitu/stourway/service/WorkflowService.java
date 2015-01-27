package com.qzsitu.stourway.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.qzsitu.stourway.dao.GenericDao;
import com.qzsitu.stourway.domain.FileItem;
import com.qzsitu.stourway.domain.KnowledgeLink;
import com.qzsitu.stourway.domain.ProcessHistoryRecord;
import com.qzsitu.stourway.domain.Todo;
import com.qzsitu.stourway.domain.User;
import com.qzsitu.stourway.exception.StourWAYException;

@Service
public class WorkflowService {
	@Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private GenericDao<Todo> todoDao;
	@Autowired
	private GenericDao<ProcessHistoryRecord> phrDao;
	@Autowired
	private GenericDao<KnowledgeLink> linkDao;
	@Autowired
	private GenericDao<FileItem> fileItemDao;
	
	public List<Map<String, String>> getTaskInfoListByUser(String userId) {
		List<Map<String, String>> taskInfoList = new ArrayList<Map<String, String>>();
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();

		for(Task task : tasks) {
			Map<String, String> taskInfo = new HashMap<String, String>();
			taskInfo.put("startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(task.getCreateTime()));
			taskInfo.put("initiator", (String)runtimeService.getVariable(task.getExecutionId(), "initiator"));
			taskInfo.put("info", (String)runtimeService.getVariable(task.getExecutionId(), "info"));
			String description = (String)runtimeService.getVariable(task.getExecutionId(), "description");
			taskInfo.put("description", HtmlUtils.htmlEscape(description));
			taskInfo.put("taskId", task.getId());
			taskInfo.put("taskName", task.getName());
			taskInfo.put("taskMod", task.getProcessDefinitionId().split("_")[0]);
			taskInfoList.add(taskInfo);
		}
		return taskInfoList;
	}
	
	public List<Map<String, String>> getTaskInfoListByUser(String processName, String userId) {
		List<Map<String, String>> taskInfoList = new ArrayList<Map<String, String>>();
		List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(processName).taskAssignee(userId).orderByTaskCreateTime().desc().list();

		for(Task task : tasks) {
			Map<String, String> taskInfo = new HashMap<String, String>();
			taskInfo.put("startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(task.getCreateTime()));
			taskInfo.put("initiator",(String)runtimeService.getVariable(task.getExecutionId(), "initiator"));
			taskInfo.put("description",(String)runtimeService.getVariable(task.getExecutionId(), "description"));
			taskInfo.put("taskId", task.getId());
			taskInfo.put("taskName", task.getName());
			taskInfoList.add(taskInfo);
		}
		return taskInfoList;
	}

	public Task getTaskById(String id) {
		Task task = taskService.createTaskQuery().taskId(id).singleResult();
		return task;
	}
	
/**
 * 做任务记录，记录下留言，并将附件关联至此流程
 * @param processId  流程的ID
 * @param user   任务执行者
 * @param comment  留言
 * @param decision  决策
 * @param attachmentIds  附件列表(格式为 文件id:文件名字|文件id:文件名字|文件id:文件名字)
 * @return
 */
	@Transactional
	public ProcessHistoryRecord makeRecord(String processId, User user, String comment, String decision,
			String attachmentIds) {
		Date date = new Date();
		
		String[] attachments = attachmentIds.split("\\|");
		for (int j = 0; j < attachments.length; j++) {
			if (attachments[j].indexOf(':') == -1)
				continue;
			
			String pType = ProcessInstance.class.getName();
			String fileItemId = attachments[j].split(":")[0];
			KnowledgeLink link = linkDao.queryOne("KnowledgeLink.getLink",
					new Object[] {processId, pType, fileItemId, FileItem.class.getName()});
			if(link == null) {
				link = new KnowledgeLink();
				link.setParentId(processId);
				link.setParentType(pType);
				link.setChildId(fileItemId);
				link.setChildType(FileItem.class.getName());
				link.setDatetime(fileItemDao.read(FileItem.class, fileItemId).getCreatedTime());
				linkDao.create(link);
			}
		}
		
		ProcessHistoryRecord phr = new ProcessHistoryRecord();
		phr.setProcessId(processId);
		phr.setUser(user.getName() + "(" + user.getId() +")");
		phr.setComment(comment);
		phr.setDecision(decision);
		phr.setAttachmentIds(attachmentIds);
		phr.setDatetime(date);
		phrDao.create(phr);
		
		return phr;
	}

	//通过流程ID获取历史记录
	@Transactional
	public List<ProcessHistoryRecord> getHistoryRecord(String processId) {
		List<ProcessHistoryRecord> phrList = phrDao.queryAll("ProcessHistoryRecord.queryHistoryRecordListByProcessId", new Object[]{processId});
		return phrList;
	}

	public String getTaskPage(String mod, String taskId) {
		Task task = getTaskById(taskId);
		String taskPageName = ("todo".equals(mod)?"todo/":"")+task.getTaskDefinitionKey();
		return taskPageName;
	}

	@Transactional
	public String startTodo(String initiatorId, String assigneeId, String description) {
		Map<String, Object> initForm = new HashMap<String, Object>();
		initForm.put("initiator", initiatorId);
		initForm.put("assignee", assigneeId);
		initForm.put("info", "");
		initForm.put("description", description);
		
		String processId = runtimeService.startProcessInstanceByKey(
				"System_TodoProcess", initForm).getProcessInstanceId();
		Todo todo = new Todo();
		todo.setInitiator(initiatorId);
		todo.setAssignee(assigneeId);
		todo.setDescription(description);
		todo.setStartTime(new Date());
		todo.setProcessId(processId);
		todoDao.create(todo);
		
		return processId;
	}
	
	public String startProcess(String processId, Map<String, Object> initForm) throws StourWAYException {
		if(initForm.get("initiator") == null) throw new StourWAYException("需要提供创建者信息");
		if(initForm.get("info") == null) throw new StourWAYException("需要提供简要信息");
		if(initForm.get("description") == null) throw new StourWAYException("需要提供详细描述信息");
		return runtimeService.startProcessInstanceByKey(
				processId, initForm).getProcessInstanceId();
	}

	//其实应该返回process instance id才对
	public String getProcessId(String taskId) {
		return getTaskById(taskId).getProcessInstanceId();
	}

	public void completeTask(String taskId, Map<String, Object> taskForm) {
		taskService.complete(taskId, taskForm);
	}

	public boolean isProcessEnded(String processId) {
		ProcessInstance p = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
		return p == null?true:p.isEnded();
	}
}
