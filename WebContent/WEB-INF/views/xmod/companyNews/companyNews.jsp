<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<% List<Map<String, Object>> postList = (List<Map<String, Object>>)request.getAttribute("postList"); %>
<% Integer currentPage = (Integer)request.getAttribute("currentPage"); %>
<% Integer pageCount = (Integer)request.getAttribute("pageCount"); %>
<div id="viewManage">
    <table class="table table-hover">
      <thead><tr class="active"><th>新闻公告</th><th>作者</th><th>时间</th></tr></thead>
      <tbody>
        <% for(Map<String, Object> post : postList) { %>
        <tr id="<%=post.get("id") %>"
          onclick="gotoPage('/xmod/companyNews/readPost?postId=<%=post.get("id") %>')">
          <td><%=post.get("title") %></td>
          <td><%=post.get("author") %></td>
          <td><%=post.get("datetime") %></td>
        </tr>
        <% } %>
      </tbody>
      <tfoot>
	      <tr>
	        <td colspan="3"><span>分页：</span> <span> 
	            <% for(int i = 1; i <= pageCount; i++) { %>
	            <%   if(i == currentPage) { %> <span style="color: red;"><%=i %></span>
	            <%   } else { %> 
	              <a onclick="gotoPage('/xmod/companyNews/companyNews?currentPage=<%=i %>')" ><%=i %></a>
	            <%   }  %> 
	            <% } %>
	        </span></td>
	      </tr>
      </tfoot>
    </table>
    <button onclick="gotoPage('/xmod/companyNews/writePost')">发布文章</button>
</div>