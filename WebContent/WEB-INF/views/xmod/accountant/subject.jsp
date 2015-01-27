<%@page import="com.qzsitu.stourway.xmod.accountant.Subject"%>
<%@page import="com.qzsitu.stourway.domain.*"%>
<%@page import="java.util.*"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<% List<Subject> subjects= (List<Subject>)request.getAttribute("subject"); %>

<div id="subjectList">
	<% for(Subject subject : subjects) { %>
    <div id="<%=subject.getId() %>" pid="<%=subject.getParentId() %>">
          <span><%=subject.getCode() +":"+subject.getName() %></span>
          <br>
    </div>
  <% } %>
  <br><br>
  <button type="button" class="btn btn-danger btn-xs" style="margin-left:35px;"  onclick="gotoPage('/xmod/accountant/subjectManage')">科目管理</button>
</div>

<script>
$.each($("#subjectList div"), function(index, obj) {
	  if($(obj).attr("pid") != "") {
	    $("#"+$(obj).attr("pid")).append($(obj));
	  }
	});


</script>