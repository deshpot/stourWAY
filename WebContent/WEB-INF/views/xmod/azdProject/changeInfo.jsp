<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.qzsitu.stourway.xmod.azdproject.AzdProject"%>
<%@ page import="com.qzsitu.stourway.domain.ProcessHistoryRecord" %>
<%@ page import="java.text.SimpleDateFormat"%>

<div id="contentPage">

<div id="projectBasicInfo" class="panel panel-default">
<% AzdProject p = (AzdProject)request.getAttribute("azdProject"); %>
  <div class="panel-heading"><%=p.getProjectName() %></div>
  <div class="panel-body">
   项目名称：<%=p.getProjectName() %><br>
   项目概况：<%=p.getDescription() %><br>
   甲方公司：<%=p.getBuyerCompany() %><br>
   甲方联系人：<%=p.getBuyerContact() %><br>
   
    业务经理：<%=(String)request.getAttribute("businessManager") %><br>
    项目经理：<%=(String)request.getAttribute("projectManager") %><br>
    财务经理：<%=(String)request.getAttribute("finacialManager") %><br>
    概念设计负责人：<%=(String)request.getAttribute("conceptSolutionManager") %><br>
    详细设计负责人：<%=(String)request.getAttribute("detailedSolutionManager") %><br>
    扩初设计负责人：<%=(String)request.getAttribute("conceptDesignManager") %><br>
    施工图负责人：<%=(String)request.getAttribute("detailedDesignManager") %><br>
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
  </div>
</div>


<div class="panel panel-default">
<div class="panel-heading">补充信息</div>
  <div class="panel-body">
  <form id="taskForm" target="uploadFileIFrame" class="form" action="file/upload" method="post" enctype="multipart/form-data">
    <div class="form-group">
      <label for="comment">留言：</label>
      <textarea id="comment" class="form-control" rows="3" placeholder="填写留言信息，可以给后续的任务人员带来帮助，建议留下您做决策的理由"></textarea>
    </div>
    <div class="form-group">
      <label for="attachments">文件附件：</label>
      <button type="button" class="btn btn-default" onclick="addFile(this)">添加附件</button>
      <p class="help-block">点击“添加附件”按钮增加附件，点击“X”按钮删除对应附件</p>
    </div>
    <div class="form-group">
      <button type="button" class="form-control" onclick="upload()">完成</button>
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
    completeTask();
  }
};

var completeTask = function() {
  var comment=$("#comment").val();
  gotoPage("/xmod/azdProject/submitChangeInfo", 
      {projectId:"<%=p.getId() %>", processId:"<%=p.getProcessId() %>", comment:comment, decisionInfo:"补充信息", attachmentIds:attachmentIds});
};
</script>