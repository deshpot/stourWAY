<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<% List<Map<String, Object>> xmodList = (List<Map<String, Object>>)request.getAttribute("xmodList"); %>
<% List<Map<String, Object>> viewList = (List<Map<String, Object>>)request.getAttribute("viewList"); %>
<div id="viewManage">
    
  <% for(Map<String, Object> xmod : xmodList) { %>
   <%=xmod.get("modName") %>

  <% } %>
</div>