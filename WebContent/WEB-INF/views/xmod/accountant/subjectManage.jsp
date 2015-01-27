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
          <span><%=subject.getCode() +":"+subject.getName() %>
          <button type="button" onclick="addSubject('<%=subject.getId() %>')" class="btn btn-primary btn-xs">添加子科目</button>
          <button type="button" onclick="editSubject('<%=subject.getId() %>')" class="btn btn-primary btn-xs">修改</button>
          <button type="button" onclick="removeSubject('<%=subject.getId() %>')" class="btn btn-danger btn-xs">删除</button>
          </span>
    </div>
    
  <% } %>

</div>

<script>
/*************排序******************/
$.each($("#subjectList div"), function(index, obj) {
	  if($(obj).attr("pid") != "") {
	    $("#"+$(obj).attr("pid")).append($(obj));
	  }
	});
/*************排序******************/

/*************增加******************/
var addSubject =function(id){
	 $("#"+id+" button:eq(2)").after("<div><span class='glyphicon glyphicon-minus'>&nbsp;<input type='hidden' value='"+id+"'/>"
		      +"<span>编号:</span><input type='text' />&nbsp;<span>名称:</span><input type='text' /></span></div>");
		  $("#"+id+" div:eq(0)").append("&nbsp;<button type='button' class='btn btn-primary btn-xs' onclick='submitAdd(this)'>确认</button>"
		      +"&nbsp;<button type='button' class='btn btn-danger btn-xs' onclick='cancelAdd(this)'>取消</button>");
};
var submitAdd = function(obj) {
	  var pId = $(obj).parent().parent().parent().attr("id");
	  var code = $(obj).parent().find("input:eq(1)").val();
	  var name = $(obj).parent().find("input:eq(2)").val();
	  var addTo = $(obj).parent();
	  
	  $.ajax({
	    type : "post",
	    url : "/xmod/ajax/accountant/subjectEdit",
	    data : {
	      id : "0",
	      name : name,
	      code : code,
	      parentId : pId,
	    },
	    dataType : "text",
	    success : function(data) {
  	      	addTo.html("");
  	      	addTo.attr("id", data.id);
 	      	addTo.attr("pId", pId);
      		addTo.append("<span>"+code+":"+name+"</span>");
          	addTo.append("&nbsp;<button type='button' onclick=\"addSubject('"+data.id+"')\" class='btn btn-primary btn-xs'>添加子科目</button>");
         	addTo.append("&nbsp;<button type='button' onclick=\"editGroup('"+data.id+"')\" class='btn btn-primary btn-xs'>重命名</button>");
       		addTo.append("&nbsp;<button type='button' onclick=\"removeGroup('"+data.id+"')\" class='btn btn-danger btn-xs'>删除</button>");
	    },
	    error : function(XMLHttpRequest, textStatus, errorThrown) {
	      alert(errorThrown);
	    }
	  });
	};
	var cancelAdd = function(obj) {
	  $(obj).parent().remove();  
	};
/*************增加******************/
var removeSubject =function(id){
  gotoPage("/xmod/accountant/removeSubject", {id:id});
};
</script>