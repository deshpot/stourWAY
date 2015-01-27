<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<% List<Map<String, Object>> xmodList = (List<Map<String, Object>>)request.getAttribute("xmodList"); %>
<% List<String> roleList = (List<String>)request.getAttribute("roleList"); %>
<% List<String> rolePermissionList = (List<String>)request.getAttribute("rolePermissionList"); %>
<div id="xmodManage">
  <table class="table table-hover table-condensed">
    <thead><tr><th></th><% for(String role:roleList) { %> 
            <th><h5><%=role.split(":")[0] %><br>
            <% if(role.split(":").length == 3) {%>
            <small><%=role.split(":")[2].replaceAll("-", "<br>") %></small>
            <% } %>
            </h5></th> 
            <% } %></tr></thead>
    <tbody  style="font-size:12px">
  <% for(Map<String, Object> xmod : xmodList) { %>
    <tr class="info"><td><strong><%=xmod.get("modName") %></strong></td><td colspan="<%=roleList.size() %>"><%=xmod.get("modDescription") %></td></tr>
      <% if (xmod.get("functionList") != null) %>
      <% for(Map<String, Object> func : (List<Map<String, Object>>)xmod.get("functionList")) { %>
		    <tr><td>-<%=func.get("functionName") %></td><% for(String role:roleList) { %> <td>
		      <input type="checkbox" id="<%=role.split(":")[1] %>-<%=xmod.get("id") + "." + func.get("permissionName") %>"
             <%=(rolePermissionList.contains(role.split(":")[1] +"-"+ xmod.get("id") + "." + func.get("permissionName")))?"checked":"" %>>
        </td> <% } %></tr>
		  <% } %>
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
  gotoPage("/xmodManageSubmit", {permission:permission});
};
var reset = function(){
  gotoPage("/xmodManage");
};
var beginModify = function(){
  $("input:checkbox").removeAttr("disabled");
  $("#bt2").show();
  $("#bt3").show();
  $("#bt1").hide();
};
</script>