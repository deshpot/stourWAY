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
<button class="btn btn-danger btn-block" onclick="gotoPage('/xmod/azdProject/changeInfo?projectId=<%=p.getId() %>')">补充信息</button>
</div>

<script>

</script>