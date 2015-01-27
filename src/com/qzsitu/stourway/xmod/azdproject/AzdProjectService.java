package com.qzsitu.stourway.xmod.azdproject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qzsitu.stourway.dao.GenericDao;
import com.qzsitu.stourway.domain.CustomizationXMod;
import com.qzsitu.stourway.domain.CustomizationXModFunction;
import com.qzsitu.stourway.domain.User;
import com.qzsitu.stourway.exception.StourWAYException;
import com.qzsitu.stourway.service.AccountService;
import com.qzsitu.stourway.service.WorkflowService;
import com.qzsitu.stourway.util.BeanUtil;
import com.qzsitu.stourway.xmod.system.XModResponse;
import com.qzsitu.stourway.xmod.system.XModService;

@Service
public class AzdProjectService extends XModService {
	@Autowired
	public WorkflowService workflowService;
	@Autowired
	public AccountService accountService;
	@Autowired
	public GenericDao<AzdProject> azdProjectDao;

	@Transactional
	public XModResponse azdProject(HttpServletRequest request,User user) {
		List<AzdProject> azdProjectList = azdProjectDao.queryAll("AzdProject.getAzdProjectList", new Object[]{});
		List<Map<String, String>> azdProjectInfo = new ArrayList<Map<String, String>>();
		Map<String,String> pMap;
		XModResponse rs = new XModResponse();
		new BeanUtil<AzdProject>().beanList2MapList(azdProjectList);
		for(AzdProject azdProject : azdProjectList) {
			pMap = new HashMap<String,String>();
			pMap.put("startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(azdProject.getStartTime()));
			pMap.put("endTime", workflowService.isProcessEnded(azdProject.getProcessId())?new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(azdProject.getEndTime()):"运行中");
			pMap.put("projectName", azdProject.getProjectName()); 
			pMap.put("businessManager", accountService.getUser(azdProject.getBusinessManager()).getName());
			pMap.put("projectManager", accountService.getUser(azdProject.getProjectManager()).getName());
			pMap.put("projectId", azdProject.getId());
			azdProjectInfo.add(pMap);
		}
		rs.put("azdProjectList", azdProjectInfo);
		rs.setPageReturn("azdProject");
		return rs;
	}
	
	@Transactional
	public XModResponse projectInfo(HttpServletRequest request, User user) {
		String projectId = (String) request.getParameter("projectId");
		AzdProject azdProject = azdProjectDao.read(AzdProject.class, projectId);

		XModResponse rs = new XModResponse();
		rs.put("historyRecord", workflowService.getHistoryRecord(azdProject.getProcessId()));
		rs.put("azdProject", azdProject);
		rs.put("businessManager", accountService.getUser(azdProject.getBusinessManager()).getName());
		rs.put("projectManager", accountService.getUser(azdProject.getProjectManager()).getName());
		rs.put("finacialManager", accountService.getUser(azdProject.getFinancialManager()).getName());
		rs.put("conceptSolutionManager", azdProject.getConceptSolutionManager() == null ? "待定":
			accountService.getUser(azdProject.getConceptSolutionManager()).getName());
		rs.put("detailedSolutionManager", azdProject.getDetailedSolutionManager() == null ? "待定":
			accountService.getUser(azdProject.getDetailedSolutionManager()).getName());
		rs.put("conceptDesignManager", azdProject.getConceptDesignManager() == null ? "待定":
			accountService.getUser(azdProject.getConceptDesignManager()).getName());
		rs.put("detailedDesignManager", azdProject.getDetailedDesignManager() == null ? "待定":
			accountService.getUser(azdProject.getDetailedDesignManager()).getName());
		
		rs.setPageReturn("projectInfo");
		return rs;
	}
	
	@Transactional
	public XModResponse startProject(HttpServletRequest request,User user) {
		XModResponse rs = new XModResponse();
		rs.put("businessManager", user.getName() + "(" + user.getId() + ")");
		rs.setPageReturn("startProject");
		return rs;
	}

	//处理启动新的项目流程
	@Transactional
	public XModResponse startProjectSubmit(HttpServletRequest request, User user) throws StourWAYException {
		String userId = user.getId();
		String comment = request.getParameter("comment");
		String decisionInfo = request.getParameter("decisionInfo");
		String attachmentIds = request.getParameter("attachmentIds");

		Map<String, Object> initForm = new HashMap<String, Object>();
		initForm.put("initiator", userId);
		initForm.put("info", request.getParameter("projectName"));
		initForm.put("description", request.getParameter("description"));
		initForm.put("businessManager", userId);
		initForm.put("projectManager", request.getParameter("projectManager"));
		initForm.put("financialManager", request.getParameter("financialManager"));
		initForm.put("buyerCompany", request.getParameter("buyerCompany"));
		initForm.put("buyerContact", request.getParameter("buyerContact"));
		initForm.put("projectName", request.getParameter("projectName"));
		initForm.put("conceptSolutionManager", "");
		initForm.put("detailedSolutionManager", "");
		initForm.put("conceptDesignManager", "");
		initForm.put("detailedDesignManager", "");

		String executionId = workflowService.startProcess("AzdProject_ProjectProcess", initForm);

		workflowService.makeRecord(executionId, user, comment, decisionInfo, attachmentIds);
		
		AzdProject azdProject = new AzdProject();
		azdProject.setProcessId(executionId);
		azdProject.setInitiator(userId);
		azdProject.setBusinessManager(userId);
		azdProject.setProjectManager(request.getParameter("projectManager"));
		azdProject.setFinancialManager(request.getParameter("financialManager"));
		azdProject.setBuyerCompany(request.getParameter("buyerCompany"));
		azdProject.setBuyerContact(request.getParameter("buyerContact"));
		azdProject.setProjectName(request.getParameter("projectName"));
		azdProject.setDescription(request.getParameter("description"));
		azdProject.setStartTime(new Date());
		azdProjectDao.create(azdProject);

		return azdProject(request, user);
	}

	@Transactional
	public XModResponse taskPage(HttpServletRequest request, User user) {
		String taskId = (String) request.getParameter("taskId");
		String executionId = workflowService.getProcessId(taskId);
		AzdProject azdProject = azdProjectDao.queryOne("AzdProject.getAzdProjectByProcessId", new Object[]{executionId});
		
		XModResponse rs = new XModResponse();
		rs.put("taskId", taskId);
		rs.put("historyRecord", workflowService.getHistoryRecord(executionId));
		rs.put("azdProject", azdProject);
		rs.put("businessManager", accountService.getUser(azdProject.getBusinessManager()).getName());
		rs.put("projectManager", accountService.getUser(azdProject.getProjectManager()).getName());
		rs.put("finacialManager", accountService.getUser(azdProject.getFinancialManager()).getName());
		rs.put("conceptSolutionManager", azdProject.getConceptSolutionManager() == null ? "待定":
			accountService.getUser(azdProject.getConceptSolutionManager()).getName());
		rs.put("detailedSolutionManager", azdProject.getDetailedSolutionManager() == null ? "待定":
			accountService.getUser(azdProject.getDetailedSolutionManager()).getName());
		rs.put("conceptDesignManager", azdProject.getConceptDesignManager() == null ? "待定":
			accountService.getUser(azdProject.getConceptDesignManager()).getName());
		rs.put("detailedDesignManager", azdProject.getDetailedDesignManager() == null ? "待定":
			accountService.getUser(azdProject.getDetailedDesignManager()).getName());
		
		rs.setPageReturn(workflowService.getTaskPage("azdProject", taskId));
		return rs;
	}

	@Transactional
	public XModResponse completeTask(HttpServletRequest request, User user) {
		String taskId = (String) request.getParameter("taskId");
		String executionId = workflowService.getProcessId(taskId);
		
		String comment = request.getParameter("comment");
		String decisionInfo = request.getParameter("decisionInfo");
		String attachmentIds = request.getParameter("attachmentIds");
		workflowService.makeRecord(executionId, user, comment, decisionInfo, attachmentIds);	

		AzdProject azdProject = azdProjectDao.queryOne("AzdProject.getAzdProjectByExecutionId", new Object[]{executionId});
		if(request.getParameter("conceptDesignManager") != null) azdProject.setConceptDesignManager(request.getParameter("conceptDesignManager"));
		if(request.getParameter("detailedDesignManager") != null) azdProject.setDetailedDesignManager(request.getParameter("detailedDesignManager"));
		if(request.getParameter("conceptSolutionManager") != null) azdProject.setConceptSolutionManager(request.getParameter("conceptSolutionManager"));
		if(request.getParameter("detailedSolutionManager") != null) azdProject.setDetailedSolutionManager(request.getParameter("detailedSolutionManager"));
		azdProject.setEndTime(new Date());
		azdProjectDao.update(azdProject);
        
		//遍历传入参数，根据参数名称来决定处理方式
		@SuppressWarnings("unchecked")
		Enumeration<String> e = request.getParameterNames();
		Map<String, Object> taskForm = new HashMap<String, Object>();
		while(e.hasMoreElements()) {
			String key = (String)e.nextElement();
			if("comment".equals(key)) continue;
			if("decisionInfo".equals(key)) continue;
			if("attachmentIds".equals(key)) continue;
			if("tasks".equals(key)) {
				List<List<String>> taskList = new ArrayList<List<String>>();
				for(String taskInfo : request.getParameter("tasks").split("-=-=STSD=-=-")) {
					if("".equals(taskInfo)) continue;
					List<String> task = new ArrayList<String>();
					String[] infos = taskInfo.split("-=STSD=-");
					task.add(infos[0]);
					task.add(infos[1]);
					task.add(infos[2]);
					task.add(user.getId());
					taskList.add(task);
				}

				taskForm.put("taskList", taskList);
				continue;
			}
			//decision与decisionInfo不同，会放入taskForm
			taskForm.put(key, request.getParameter(key));
		}
		workflowService.completeTask(taskId, taskForm);
		
		return azdProject(request, user);
	}

	@Transactional
	public XModResponse changeInfo(HttpServletRequest request, User user) {
		String projectId = (String) request.getParameter("projectId");
		AzdProject azdProject = azdProjectDao.read(AzdProject.class, projectId);

		XModResponse rs = new XModResponse();
		rs.put("historyRecord", workflowService.getHistoryRecord(azdProject.getProcessId()));
		rs.put("azdProject", azdProject);
		rs.put("businessManager", accountService.getUser(azdProject.getBusinessManager()).getName());
		rs.put("projectManager", accountService.getUser(azdProject.getProjectManager()).getName());
		rs.put("finacialManager", accountService.getUser(azdProject.getFinancialManager()).getName());
		rs.put("conceptSolutionManager", azdProject.getConceptSolutionManager() == null ? "待定":
			accountService.getUser(azdProject.getConceptSolutionManager()).getName());
		rs.put("detailedSolutionManager", azdProject.getDetailedSolutionManager() == null ? "待定":
			accountService.getUser(azdProject.getDetailedSolutionManager()).getName());
		rs.put("conceptDesignManager", azdProject.getConceptDesignManager() == null ? "待定":
			accountService.getUser(azdProject.getConceptDesignManager()).getName());
		rs.put("detailedDesignManager", azdProject.getDetailedDesignManager() == null ? "待定":
			accountService.getUser(azdProject.getDetailedDesignManager()).getName());
		
		rs.setPageReturn("changeInfo");
		return rs;
	}
	
	@Transactional
	public XModResponse submitChangeInfo(HttpServletRequest request,
			User user) {
		String processId = (String) request.getParameter("processId");
		
		String comment = request.getParameter("comment");
		String decisionInfo = request.getParameter("decisionInfo");
		String attachmentIds = request.getParameter("attachmentIds");
		workflowService.makeRecord(processId, user, comment, decisionInfo, attachmentIds);	
		
		return projectInfo(request, user);
	}

	public CustomizationXMod getXMod() {
		CustomizationXMod xmod = new CustomizationXMod();
		xmod.setId("azdProject");
		xmod.setModName("安之道业务流程");
		xmod.setModDescription("安之道景观设计公司专门定制公司业务流程模块");
		xmod.setEnabled(false);
		
		List<CustomizationXModFunction> functionList = new ArrayList<CustomizationXModFunction>();
		CustomizationXModFunction function = new CustomizationXModFunction();
		function.setFunctionName("进入业务流程系统");
		function.setFunctionDescription("可以启动流程，查看流程信息");
		function.setPermissionName("azdProject");
		functionList.add(function);
		function = new CustomizationXModFunction();
		function.setFunctionName("查看项目信息");
		function.setFunctionDescription("");
		function.setPermissionName("projectInfo");
		functionList.add(function);
		function = new CustomizationXModFunction();
		function.setFunctionName("追加项目信息");
		function.setFunctionDescription("");
		function.setPermissionName("changeInfo");
		functionList.add(function);
		xmod.setFunctionList(functionList);
		
		return xmod;
	}

}
