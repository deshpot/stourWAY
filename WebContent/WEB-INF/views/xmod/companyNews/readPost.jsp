<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<% Map<String, Object> post = (Map<String, Object>)request.getAttribute("post"); %>
<div>
<h3><%=post.get("title") %><small><%=post.get("author") %>发表于<%=post.get("datetime") %></small></h3>
<%=post.get("content") %><br>

</div>