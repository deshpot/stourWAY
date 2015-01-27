<%@page import="com.qzsitu.stourway.domain.Group"%>
<%@page import="com.qzsitu.stourway.domain.User"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<% List<User> users= (List<User>)request.getAttribute("users"); %>
<div id="manage">
<table id="userTable" class="table table-hover table-condensed">
		<thead>
			<tr>
				<th>姓名</th>
				<th>邮箱</th>
				<th>电话</th>
				<th>手机</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
				<% for(User user : users) { %>
		          <tr id="<%=user.getId() %>")">
		            <td><%=user.getName() %></td>
		            <td><%=user.getEmail() %></td>
		            <td><%=user.getTelephone() %></td>
		            <td><%=user.getMobilephone() %></td>
		            <td></td>
		          </tr>
		        <% } %>
		</tbody>
	</table>
	<button type="button" onclick="gotoPage('/employeeManage')" >员工管理</button>
</div>