<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.qzsitu.stourway.domain.*"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>

<% List<Map<String,Object>> posts= (List<Map<String,Object>>)request.getAttribute("posts"); %>
<% List<Map> taskList = (List<Map>)request.getAttribute("taskList"); %>

<% if(posts.size() != 0) { %>
	  <table class="table table-hover">
	    <thead><tr><th><h4>新闻公告</h4></th><th>作者</th><th>时间</th></tr></thead>
	    <tbody>
	      <% for(Map<String,Object> post : posts) { %>
	      <tr id="<%=post.get("id") %>"
	        onclick="gotoPage('/knowledge/all/all/1/post/<%=post.get("id") %>')">
	        <td><%=post.get("title") %></td>
	        <td><%=post.get("author") %></td>
	        <td><%=post.get("datetime") %></td>
	      </tr>
	      <% } %>
	    </tbody>
	  </table>
	  <br>
<% } %>	  

<ul class="media-list">
  <li class="media">
    <a class="pull-left" >
      <button class="btn btn-primary" style="width: 64px; height: 64px;" onclick="gotoPage('/todo/startTodo')">待办<br>任务</button>
    </a>
    <div class="media-body">
      <h4 class="media-heading">待办任务</h4>
   创建一个待办任务，提醒自己安排时间解决。
    </div>
  </li>
</ul>

<ul class="media-list">
  <li class="media">
    <a class="pull-left" >
      <button class="btn btn-primary" style="width: 64px; height: 64px;" onclick="gotoPage('/todo/startCooperation')">发起<br>协作</button>
    </a>
    <div class="media-body">
      <h4 class="media-heading">协作流程</h4>
   创建一个协作流程，选择指定人员帮忙完成某项工作。
    </div>
  </li>
</ul>

    <table class="table table-hover">
      <thead><tr><th><h4>待办任务</h4></th><th>描述</th><th>开始时间</th></tr></thead>
      <tbody>
        <% for(Map task : taskList) { %>
          <tr id="task<%=task.get("taskId") %>" title="<%=task.get("description") %>" onclick="gotoPage('/todo/taskPage/<%=task.get("taskMod") %>/<%=task.get("taskId") %>')">
            <td><%=task.get("taskName") %></td>
            <td><%=((task.get("info") == null || "".equals(task.get("info")))?task.get("description"):task.get("info")) %></td>
            <td><%=task.get("startTime") %></td>
          </tr>
        <% } %>
      </tbody>
    </table>


