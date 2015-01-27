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
	  	请假时间：
	  	<%=(new SimpleDateFormat("yyyy-MM-dd").format(request.getAttribute("startDate"))) %>
	  	到
	  	<%=(new SimpleDateFormat("yyyy-MM-dd").format(request.getAttribute("endDate"))) %>
	  	申请时间：
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
	              <%=record.getComment() %>
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
	  </div>
	</div>
	
	<div class="panel panel-default">
	<div class="panel-heading">决策区域</div>
	  <div class="panel-body">
	   <form id="taskForm" target="uploadFileIFrame" class="form" action="file/upload" method="post" enctype="multipart/form-data">
	    <div class="form-group">
	      <div class="form-group">
			<label for="comment">留言：</label>
			<textarea id="comment" class="form-control" rows="3" placeholder="填写留言信息，建议留下您做决策的理由"></textarea>
		  </div>
		  	
		  	<label for="attachments">文件附件：</label>
    		<button type="button" class="btn btn-default" onclick="addFile(this)">添加附件</button>
    		<p class="help-block">点击“添加附件”按钮增加附件，点击“X”按钮删除对应附件</p>
    		
	      <label for="comment">决策：</label>
	      <div class="radio">
	        <label>
	          <input type="radio" name="decision" id="decision1" value="ok" checked><span>同意请假</span>
	        </label>
	      </div>
	      <div class="radio">
	        <label>
	          <input type="radio" name="decision" id="decision2" value="nok"><span>拒绝请假</span>
	        </label>
	      </div>
	      <button id="submitLeave" type="button" class="form-control" onclick="upload()">完成</button>
	    </div>
	  </form>
	  </div>
	</div>
</div>
<script>
var attchmentI = 0;
var attachmentIds = "";
var addFile = function(btnObj) {
  attchmentI = attchmentI + 1;
  $(btnObj).after("<p><input type='file' name='attachment"+attchmentI+"' style='display:inline;'><button type='button' onclick='removeFile(this)'>X</button></p>");
};
var removeFile = function(btnObj) {
	  $(btnObj).parent().remove();
	};
	var upload = function() {
	  $("#taskForm").submit();
	};
	var uploadCallback = function(code, msg) {
	  if (code == 0) {
	    attachmentIds = msg;
	    sub();
	  }
	};



var sub =function(){
	var way="";
	var comment =$("#comment").val();
	var decision =$("input[name='decision']:checked").val();	
	var decisionInfo=$("input[name='decision']:checked").next("span").html();
	gotoPage("/xmod/leave/completeTask", {taskId:"<%=request.getAttribute("taskId") %>",comment:comment,decision:decision,way:way,decisionInfo:decisionInfo,attachmentIds:attachmentIds});
};


</script>