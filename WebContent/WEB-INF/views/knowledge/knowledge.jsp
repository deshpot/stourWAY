<%@page contentType="text/html;charset=UTF-8"%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="com.qzsitu.stourway.domain.*"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<% List<KnowledgeTag> tagList = (List<KnowledgeTag>) request.getAttribute("tagList"); %>
<% List<KnowledgeMenu> menuList = (List<KnowledgeMenu>) request.getAttribute("menuList"); %>
<% String availiablePermission = (String)request.getAttribute("availiablePermission"); %>
<% String currentMenu = (String)request.getAttribute("menu"); %>
<% String currentTag = (String)request.getAttribute("tag"); %>
<% List<Post> posts= (List<Post>)request.getAttribute("posts"); %>
<% int currentPage = ((Number)request.getAttribute("page")).intValue(); %>
<% int pageCount = ((Number)request.getAttribute("pageCount")).intValue(); %>

<div id="contentPage">
	<table class="table table-hover">
		<thead>
		   <tr>
        <td colspan="3">目录：
        <a  onclick="gotoPage('/knowledge/knowledge/all/<%=currentTag %>/1')" id="menuall">所有</a>
        <% for(KnowledgeMenu menu : menuList) { %>
         &nbsp;|&nbsp;<a  onclick="gotoPage('/knowledge/knowledge/<%=menu.getId() %>/<%=currentTag %>/1')" id="menu<%=menu.getId() %>"><%=menu.getName() %></a>
        <% } %>
        </td>
      </tr>
      <tr>
        <td colspan="3">标签：
        <a  onclick="gotoPage('/knowledge/knowledge/<%=currentMenu %>/all/<%=currentPage %>')" id="tagall">所有</a>
        <% for(KnowledgeTag tag : tagList) { %>
          &nbsp;|&nbsp;<a  onclick="gotoPage('/knowledge/knowledge/<%=currentMenu %>/<%=tag.getId() %>/1')" id="tag<%=tag.getId() %>"><%=tag.getName() %></a>
        <% } %>
        </td>
      </tr>
			<tr>
				<th>标题</th>
				<th>创建者</th>
				<th>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<% for(Post post : posts) { %>
			<tr id="<%=post.getId() %>"
				onclick="gotoPage('/knowledge/<%=currentMenu %>/<%=currentTag %>/<%=currentPage %>/post/<%=post.getId() %>')">
				<td><%=post.getTitle().replaceAll("<", "&lt;").replaceAll(">", "&gt;") %></td>
				<td><%=post.getAuthor() %></td>
				<td><%=new SimpleDateFormat("yyyy年MM月dd日 hh时mm分").format(post.getDatetime()) %></td>
			</tr>
			<% } %>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="3"><span>分页：</span> <span> 
				    <% for(int i = 1; i <= pageCount; i++) { %>
						<%   if(i == currentPage) { %> <span style="color: red;"><%=i %></span>
						<%   } else { %> 
						  <a onclick="gotoPage('/knowledge/knowledge/<%=currentMenu %>/<%=currentTag %>/<%=i %>')" ><%=i %></a>
						<%   }  %> 
						<% } %>
				</span></td>
			</tr>
		</tfoot>
	</table>
	<% if(availiablePermission.indexOf("publish") != -1) { %>
	<button onclick="gotoPage('/knowledge/newPost/<%=currentMenu %>')">发布文章</button>
	<% } %>
	<% if(availiablePermission.indexOf("manage") != -1) { %>
  <button onclick="gotoPage('/knowledge/knowledgeManage/<%=currentMenu %>/<%=currentTag %>/<%=currentPage %>')">内容管理</button>
  <% } %>
  <% if(availiablePermission.indexOf("admin") != -1) { %>
  <button onclick="gotoPage('/knowledge/accessManage')">目录管理</button>
  <% } %>
</div>

<script>
$("#menu<%=currentMenu %>").css("color","red");
$("#tag<%=currentTag %>").css("color","red");
</script>