<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>


<div class="bs-callout bs-callout-primary">
  <h4>任务描述<small>创建者：<%=request.getAttribute("initiator") %></small></h4>
  <%=request.getAttribute("description") %>
</div>