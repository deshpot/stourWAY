package com.qzsitu.stourway.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import com.qzsitu.stourway.domain.KnowledgeMenu;
import com.qzsitu.stourway.domain.Post;
import com.qzsitu.stourway.domain.User;
import com.qzsitu.stourway.exception.StourWAYException;
import com.qzsitu.stourway.service.AccountService;
import com.qzsitu.stourway.service.KnowledgeService;
import com.qzsitu.stourway.service.WorkflowService;
import com.qzsitu.stourway.util.BeanUtil;

@Controller
@RequestMapping(value="/todo")
public class TodoController {
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private KnowledgeService knowledgeService;

	// 我的工作台
	@RequestMapping(value = "/infoCenter")
	public String getMyWorkspace(HttpServletRequest request, Model model,
			HttpSession session) throws StourWAYException {
		User user = (User) session.getAttribute("user");
		List<Map<String, String>> taskList = workflowService.getTaskInfoListByUser(user.getId());
		model.addAttribute("taskList", taskList);
		
		String companyNewsMenuId = "all";
		for (KnowledgeMenu menu : knowledgeService.getKnowledgeMenuList()) {
			if (menu.getName().equals("新闻公告"))
				companyNewsMenuId = menu.getId();
		}
		List<Post> postList = knowledgeService.getKnowledgeList(
				companyNewsMenuId, "all", null, 0, 5);
		
		model.addAttribute("posts", new BeanUtil<Post>().beanList2MapList(postList, new Object(){
			public Object getAuthor(String authorId) {
				User u = accountService.getUser(authorId);
				return u == null?authorId:u.getName();
			}
			public Object getTitle(String title) {
				return HtmlUtils.htmlEscape(title);
			}
		}));

		return "/todo/infoCenter";
	}
	
	@RequestMapping(value="/startTodo")
	public String getStartTodo(HttpServletRequest request, Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");

		model.addAttribute("user", user);
		
		return "/todo/startTodo";
	}

	@RequestMapping(value="/startCooperation")
	public String getStartCooperation(HttpServletRequest request, Model model, HttpSession session) {
		
		return "/todo/startCooperation";
	}
	
	@RequestMapping(value="/startTodoSubmit")
	public String getStartTodoSubmit(HttpServletRequest request, Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		String comment = request.getParameter("comment");
		String decisionInfo = request.getParameter("decisionInfo");
		String attachmentIds = request.getParameter("attachmentIds");

		String executionId = workflowService.startTodo(user.getId(), 
				request.getParameter("assignee"), request.getParameter("description"));
		
		workflowService.makeRecord(executionId, user, comment, decisionInfo,
				attachmentIds);
			
		return "forward:/todo/infoCenter";
	}


	@RequestMapping(value="/taskPage/{mod}/{id}")
	public String getTaskPage(HttpServletRequest request, @PathVariable String mod,
			 @PathVariable String id, Model model, HttpSession session) throws StourWAYException {
		model.addAttribute("taskId", id);
		if("System".equals(mod)) {
			return workflowService.getTaskPage("todo", id);
		} else {
			return "forward:/xmod/"+mod+"/taskPage?taskId="+id;
		}
	}

	
	@RequestMapping(value="/completeTask/{id}")
	public String completeTask(HttpServletRequest request, @PathVariable String id,
			Model model, HttpSession session) throws StourWAYException {
		User user = (User) session.getAttribute("user");
		String comment = request.getParameter("comment");
		String decisionInfo = request.getParameter("decisionInfo");
		String attachmentIds = request.getParameter("attachmentIds");
		String executionId = workflowService.getTaskById(id).getExecutionId();
		workflowService.makeRecord(executionId, user, comment, decisionInfo, attachmentIds);	
        
		@SuppressWarnings("unchecked")
		Enumeration<String> e = request.getParameterNames();
		Map<String, Object> taskForm = new HashMap<String, Object>();
		while(e.hasMoreElements()) {
			String key = (String)e.nextElement();
			if("comment".equals(key)) continue;
			if("decisionInfo".equals(key)) continue;
			if("attachmentIds".equals(key)) continue;

			taskForm.put(key, request.getParameter(key));
		}
		taskService.complete(id, taskForm);
		
		return "forward:/todo/infoCenter";
	}
	
	@RequestMapping(value="/basicInfo/{id}")
	public String getTaskBasicInfo(HttpServletRequest request, @PathVariable String id,
			Model model, HttpSession session) throws StourWAYException {
		Task task = workflowService.getTaskById(id);
		String initiator = (String) runtimeService.getVariable(task.getExecutionId(), "initiator");
		String description = (String) runtimeService.getVariable(task.getExecutionId(), "description");
		
		model.addAttribute("initiator", accountService.getUser(initiator).getName());
		model.addAttribute("description", HtmlUtils.htmlEscape(description));
		model.addAttribute("taskId", id);
		
		return "/todo/basicInfo";
	}
	@RequestMapping(value="/historyRecord/{id}")
	public String getTaskHistoryRecord(HttpServletRequest request, @PathVariable String id,
			Model model, HttpSession session)  throws StourWAYException{
		Task task = workflowService.getTaskById(id);
		model.addAttribute("historyRecord", workflowService.getHistoryRecord(task.getExecutionId()));
		
		return "/todo/historyRecord";
	}
}
