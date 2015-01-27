<%@page import="com.qzsitu.stourway.domain.*"%>
<%@page import="java.util.*"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<% List<Group> groups= (List<Group>)request.getAttribute("groups"); %>
<% Map<Group, List<User>> groupUsers= (Map<Group, List<User>>)request.getAttribute("groupUsers"); %>
<% List<User> allUser = (List<User>)request.getAttribute("allUser"); %>
<div id="groupList">
	<% for(Group group : groups) { %>
    <div id="<%=group.getId() %>" pid="<%=group.getParentGroup() %>">
      <span class="glyphicon <%=("role".equals(group.getType())?"glyphicon-user":"glyphicon-home") %>"><%=group.getName() %></span>
      <% if("role".equals(group.getType())) { %>
        <% List<User> userList = (List<User>)groupUsers.get(group); %>
        <% for(User user:userList) { %>
            <br><span class="userName" name="<%=user.getId() %>"><%=user.getName() %></span>
        <% } %>
      <% } %>
    </div>
  <% } %>
  <br><br>
  <button type="button" class="btn btn-danger btn-xs" style="margin-left:35px;"  onclick="gotoPage('/groupManage')">组织架构管理</button>
</div>
<script>
$.each($("#groupList div"), function(index, obj) {
  if($(obj).attr("pid") != "") {
    $("#"+$(obj).attr("pid")).append($(obj));
  }
});
</script>