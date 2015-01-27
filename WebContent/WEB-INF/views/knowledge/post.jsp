<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.qzsitu.stourway.domain.*"%>
<%@page import="java.util.List"%>
<% Post post = (Post)request.getAttribute("post"); %>
<% List<KnowledgeMenu> menuList = (List<KnowledgeMenu>)request.getAttribute("menuList"); %>
<% List<KnowledgeTag> tagList = (List<KnowledgeTag>)request.getAttribute("tagList"); %>
<% String currentMenu = (String)request.getAttribute("currentMenu"); %>
<% String currentTag = (String)request.getAttribute("currentTag"); %>
<% int currentPage = (Integer.parseInt((String)request.getAttribute("currentPage"))); %>

<div id="post" class="col-md-10">
    <h3><%=post.getTitle() %><small><%=post.getAuthor() %>@<%=post.getDatetime() %></small></h3>
    <%=post.getContent() %><br>
</div>
<div class="col-md-2" style="margin-top: 20px;border-left: 1px solid;padding: 20px;">
<p>
  <button class="btn btn-primary" onclick="gotoPage('/knowledge/knowledge/<%=currentMenu %>/<%=currentTag %>/<%=currentPage %>')">返回知识库</button>
</p>
<p>
	<span>目录：</span> <span> <% for(KnowledgeMenu menu : menuList) { %>
		<%=menu.getName() %> <% } %>
</p>
<p>
	</span> <span>标签：</span> <span> <% for(KnowledgeTag tag : tagList) { %>
		<%=tag.getName() %> <% } %>
	</span>
</p>
</div>
<script>

</script>