<%@page import="com.qzsitu.stourway.domain.ProcessHistoryRecord"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<div id="content">
	<div class="panel panel-default">
	  <div class="panel-heading">请假申请：</div>
	  <div class="panel-body">
	  	申请人：<%=request.getAttribute("emp") %><br>
	  	请假原因：<%=request.getAttribute("reason") %><br>
	  	您申请于
	  	<%=(new SimpleDateFormat("yyyy-MM-dd").format(request.getAttribute("startDate"))) %>
	  	至
	  	<%=(new SimpleDateFormat("yyyy-MM-dd").format(request.getAttribute("endDate"))) %>
	  	期间请假已获得批准，请准时归还！
	  </div>
	  </div>
	  
	  
	<div id="projectHistoryRecord" class="panel panel-default">
	  <div class="panel-heading">流程记录</div>
	  <div class="panel-body">
	    <table class="table">
	      <thead><tr><th>操作者</th><th>留言</th><th>决策</th><th>时间</th></tr></thead>
	      <tbody>
	    <% 
	      List<ProcessHistoryRecord> phrList = (List<ProcessHistoryRecord>)request.getAttribute("historyRecord"); 
	
	        for(int i = 0; i < phrList.size(); i++) {
	        	ProcessHistoryRecord record = (ProcessHistoryRecord) phrList.get(i);
	    %>
	          <tr>
	            <td><%=record.getUser() %></td>
	            <td>
	              <%=record.getComment() %><br>

	              <% 
	                String[] attachments = record.getAttachmentIds().split("\\|");
	                for(int j = 0; j < attachments.length; j++) {
	                  if(attachments[j].indexOf(':') == -1) continue;
	              %>
	                 <a href="/file/download/<%=attachments[j].split(":")[0] %>"><%=attachments[j].split(":")[1] %></a>
	              <%
	                }
	              %>
	            </td>

	            <td><%=record.getDecision() %></td>
	            <td><%=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(record.getDatetime())) %></td>
	          </tr>
	    <%
	        } 
	    %>
	      </tbody>
	    </table>
	    <button onclick="sub()">销假</button>
	  </div>
	</div>
	
	
	<script>
	var sub= function(){
		gotoPage("/xmod/leave/completeTask", {taskId:"<%=request.getAttribute("taskId") %>",comment:comment,decision:decision,way:way,decisionInfo:decisionInfo,attachmentIds:attachmentIds});

	};
	</script>
	
	