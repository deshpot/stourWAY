<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<% List<Map<String,String>> azdProjectList = (List<Map<String,String>>)request.getAttribute("azdProjectList"); %>
<div id="contentPage">

<ul class="media-list">
  <li class="media">
    <a class="pull-left" >
      <button class="btn btn-primary" style="width: 64px; height: 64px;" onclick="gotoPage('/xmod/azdProject/startProject')">启动<br>项目</button>
    </a>
    <div class="media-body">
      <h4 class="media-heading">项目流程</h4>
      安之道项目主流程
    </div>
  </li>
</ul>

    <table class="table">
      <thead><tr><th>项目名称</th><th>启动时间</th><th>结束时间</th><th>业务经理</th><th>项目经理</th></tr></thead>
      <tbody>
    <% 
        for(int i = 0; i < azdProjectList.size(); i++) {
        	Map<String,String> project = (Map<String,String>) azdProjectList.get(i);
    %>
          <tr onclick="gotoPage('/xmod/azdProject/projectInfo?projectId=<%=project.get("projectId") %>')">
            <td><%=project.get("projectName") %></td>
            <td><%=project.get("startTime") %></td>
            <td><%=project.get("endTime") %></td>
            <td><%=project.get("businessManager") %></td>
            <td><%=project.get("projectManager") %></td>
          </tr>
    <%
        } 
    %>
      </tbody>
    </table>
</div>



</div>