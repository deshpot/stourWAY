<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<% List<String> roleList= (List<String>)request.getAttribute("roleList"); %>
<% List<String> rolePermissionList= (List<String>)request.getAttribute("rolePermissionList"); %>
<div id="contentPage">
    <table class="table table-hover table-condensed">
      <thead><tr><th></th><th>日常工作</th><th>知识体系</th><th>系统管理</th></tr></thead>
      <tbody  style="font-size:12px">
      <% for(String role:roleList){ %>
        <tr>
          <td id="role<%=role.split(":")[1] %>">
            <h4><%=role.split(":")[0] %><br>
            <% if(role.split(":").length == 3) {%>
            <small><%=role.split(":")[2].replaceAll("-", "<br>") %></small>
            <% } %>
            </h4>
          </td>
          <td id="todo">
            <input type="checkbox" id="<%=role.split(":")[1] %>-startCooperationProcess"
             <%=(rolePermissionList.contains(role.split(":")[1] +"-startCooperationProcess"))?"checked":"" %>>发起协作流程<br>
            <input type="checkbox" id="<%=role.split(":")[1] %>-watchWorkProcess"
             <%=(rolePermissionList.contains(role.split(":")[1] +"-watchWorkProcess"))?"checked":"" %>>查看所有协作<br>
            <input type="checkbox" id="<%=role.split(":")[1] %>-watchAllWorkRecord"
             <%=(rolePermissionList.contains(role.split(":")[1] +"-watchAllWorkRecord"))?"checked":"" %>>查看工作日志
          </td>
          <td id="knowledge">
            <input type="checkbox" id="<%=role.split(":")[1] %>-manageCompanyNews"
             <%=(rolePermissionList.contains(role.split(":")[1] +"-manageCompanyNews"))?"checked":"" %>>管理新闻公告<br>
            <input type="checkbox" id="<%=role.split(":")[1] %>-manageKnowledge"
             <%=(rolePermissionList.contains(role.split(":")[1] +"-manageKnowledge"))?"checked":"" %>>管理知识库
          </td>
          <td id="system">
            <input type="checkbox" id="<%=role.split(":")[1] %>-manageGroup"
             <%=(rolePermissionList.contains(role.split(":")[1] +"-manageGroup"))?"checked":"" %>>管理组织架构<br>
            <input type="checkbox" id="<%=role.split(":")[1] %>-manageEmployee"
             <%=(rolePermissionList.contains(role.split(":")[1] +"-manageEmployee"))?"checked":"" %>>管理人力资源<br>
            <input type="checkbox" id="<%=role.split(":")[1] %>-managePermission"
             <%=(rolePermissionList.contains(role.split(":")[1] +"-managePermission"))?"checked":"" %>>管理系统权限<br>
          </td>
        </tr>
      <% } %>
      </tbody>
    </table>
    <button id="bt1" onclick="beginModify()" type="button" class="btn btn-primary">修改权限</button>
    <button id="bt2" onclick="submit()" type="button" class="btn btn-primary">完成</button>
    <button id="bt3" onclick="reset()" type="button" class="btn btn-default">撤销</button>
</div>
<script>
$("input:checkbox").attr("disabled","disabled");
$("#bt2").hide();
$("#bt3").hide();
var submit = function(){
  var permission = "";
  $("input:checkbox:checked").each(function(index,element){
    permission = permission + "-=STSD=-" + $(element).attr("id");
  });
  gotoPage("/submitPermission", {permission:permission});
};
var reset = function(){
  gotoPage("/permission");
};
var beginModify = function(){
  $("input:checkbox").removeAttr("disabled");
  $("#bt2").show();
  $("#bt3").show();
  $("#bt1").hide();
};
</script>