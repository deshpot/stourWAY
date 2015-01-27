<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<div id="content" >

<form id="taskForm" target="uploadFileIFrame" class="form" action="file/upload" method="post" enctype="multipart/form-data">
	<label for="emp">申请人：<%=request.getAttribute("leaveEmp") %> </label><br>
	<label for="leavereason">请假原因：</label>
	 <textarea id="reason" class="form-control" rows="3" placeholder="请说明为何请假"></textarea>
	<label>请假时间：</label>
	<input type="text" id="startDate" class="form-control" />
	<label>至</label>
	<input type="text" id="endDate" class="form-control" />
	<label>提交对象</label>
	 <select id="confirmBy" class="form-control"></select>
	 <label for="attachments">文件附件：</label>
    <button type="button" class="btn btn-default" onclick="addFile(this)">添加附件</button>
    <p class="help-block">点击“添加附件”按钮增加附件，点击“X”按钮删除对应附件</p>
	<button id="submitLeave" type="button" class="form-control" onclick="upload()">申请</button>
</form>

</div>
<script>
$("#startDate").datepicker({format:'yyyy-mm-dd', language:'zh-CN'});
$("#endDate").datepicker({format:'yyyy-mm-dd', language:'zh-CN'});

var attchmentI = 0;
var attachmentIds = "";
var addFile = function(btnObj) {
  attchmentI = attchmentI + 1;
  $(btnObj).after("<p><input type='file' name='attachment"+attchmentI+"' style='display:inline;'><button type='button' onclick='removeFile(this)'>X</button></p>");
};
var removeFile = function(btnObj) {
	  $(btnObj).parent().remove();
	};
	var upload = function() {
	  $("#taskForm").submit();
	};
	var uploadCallback = function(code, msg) {
	  if (code == 0) {
	    attachmentIds = msg;
	    submitPage();
	  }
	};
	
var submitPage = function() {
	  
	  var reason =$("#reason").val();
	  var startDate =$("#startDate").val();
	  var endDate=$("#endDate").val();
	  var confirmBy=$("#confirmBy").val();
	  
	  gotoPage("/xmod/leave/askLeaveSubmit", {reason:reason, startDate:startDate,
		  endDate:endDate, confirmBy:confirmBy,decisionInfo:"请假申请",attachmentIds:attachmentIds});
	};
	
	$.ajax({
		  type : "post",
		  url : "/getUserList",
		  dataType : "json",
		  success : function(data) {
		    var userOptionHtml = "";
		    $(data).each(function(index, element) {
		      userOptionHtml += "<option value='"+element.id+"'>"+element.name+"</option>";
		    });
		    $("#confirmBy").html(userOptionHtml);
		  },
		  error : function(XMLHttpRequest, textStatus, errorThrown) {
		    alert(errorThrown);
		  }
		});

</script>