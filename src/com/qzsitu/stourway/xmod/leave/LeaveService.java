package com.qzsitu.stourway.xmod.leave;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.qzsitu.stourway.service.WorkflowService;
import com.qzsitu.stourway.xmod.system.XModResponse;
import com.qzsitu.stourway.xmod.system.XModService;

@Service
public class LeaveService extends XModService {
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private GenericDao<Leave> leaveDao;

	public XModResponse taskPage(HttpServletRequest request, User user) {
		String taskId = (String) request.getParameter("taskId");
		String ProcessId = workflowService.getProcessId(taskId);
		XModResponse rs = new XModResponse();
		
		Leave leave = leaveDao.queryOne("Leave.getLeaveByExecutionId", new Object[]{ProcessId});
		
		rs.put("historyRecord", workflowService.getHistoryRecord(ProcessId));
		rs.put("emp", leave.getInitiator());
		rs.put("reason", leave.getReason());
		rs.put("startDate", leave.getLeaveDate());
		rs.put("endDate", leave.getReturnDate());
		rs.setPageReturn("approvalLeave");
		
		rs.put("taskId", taskId);
		rs.setPageReturn(workflowService.getTaskPage("leave", taskId));
		return rs;
	}
	
	private XModResponse leave(User user){
		XModResponse rs = new XModResponse();
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("leaveEmp", user.getName() + "(" + user.getId() + ")");
		rs.setAttributtes(m);
		rs.setPageReturn("leave");
		return rs;
		
		
		
		
	}
	
	public XModResponse askLeave(HttpServletRequest request, User user){
		XModResponse rs = new XModResponse();
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("leaveEmp", user.getName() + "(" + user.getId() + ")");
		rs.setAttributtes(m);
		rs.setPageReturn("askLeave");
		return rs;
	}
	
	@Transactional
	public XModResponse askLeaveSubmit(HttpServletRequest request, User user) throws StourWAYException{

		String userId = user.getId();
		String decisionInfo = request.getParameter("decisionInfo");
		String commit=request.getParameter("reason");
		String attachmentIds = request.getParameter("attachmentIds");
		
		Map<String,Object> askleave = new HashMap<String, Object>();
		askleave.put("initiator", userId);
		askleave.put("info", "xxx发起的请假申请");
		askleave.put("description", request.getParameter("reason"));
		askleave.put("startDate", request.getParameter("startDate"));
		askleave.put("endDate", request.getParameter("endDate"));
		askleave.put("confirmBy", request.getParameter("confirmBy"));
		String ProcessId = workflowService.startProcess("Leave_LeaveProcess", askleave);
		workflowService.makeRecord(ProcessId, user, commit , decisionInfo, attachmentIds);
		Leave leave =new Leave();
		leave.setProcessId(ProcessId);
		leave.setReason(request.getParameter("reason"));
		leave.setInitiator(userId);
		leave.setWorkDays(0);
		try {
			Date leaveDate =new SimpleDateFormat("yyyy-MM-dd").parse((String)request.getParameter("startDate"));
			Date returnDate =new SimpleDateFormat("yyyy-MM-dd").parse((String)request.getParameter("endDate"));
			leave.setLeaveDate(leaveDate);
			leave.setReturnDate(returnDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		leaveDao.create(leave);
		
		return leave(user);
	}
	
	@Transactional
	public XModResponse completeTask(HttpServletRequest request, User user){
	String taskId = (String) request.getParameter("taskId");


	String decisionInfo = request.getParameter("decisionInfo");	
	String ProcessId = workflowService.getProcessId(taskId);	
	String comment = request.getParameter("comment");
	String way= request.getParameter("way");
	String decision =request.getParameter("decision");
	
	String attachmentIds = request.getParameter("attachmentIds");
	workflowService.makeRecord(ProcessId, user, comment, decisionInfo, attachmentIds);	
	
	Map<String,Object> askleave = new HashMap<String, Object>();
	if(!"".equals(way)){
	askleave.put("way", way);
	}
	else if(!"".equals(decision)){
	askleave.put("decision", decision);
	}
	workflowService.completeTask(taskId, askleave);
	
	return leave(user);
	}

	
	public CustomizationXMod getXMod() {
		CustomizationXMod xmod = new CustomizationXMod();
		xmod.setId("leave");
		xmod.setModName("请假模块");
		xmod.setModDescription("系统默认请假模块");
		xmod.setEnabled(false);
		
		List<CustomizationXModFunction> functionList = new ArrayList<CustomizationXModFunction>();
		CustomizationXModFunction function = new CustomizationXModFunction();
		function.setFunctionName("进入请假系统");
		function.setFunctionDescription("");
		function.setPermissionName("leave");
		functionList.add(function);
		function = new CustomizationXModFunction();
		function.setFunctionName("申请请假");
		function.setFunctionDescription("");
		function.setPermissionName("askLeave");
		functionList.add(function);
		xmod.setFunctionList(functionList);
		
		return xmod;
	}
	
	
}
