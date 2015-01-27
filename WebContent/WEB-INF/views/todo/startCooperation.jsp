<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<div id="contentPage">

<form id="taskForm" target="uploadFileIFrame" class="form" action="file/upload" method="post" enctype="multipart/form-data">
  <div class="form-group">
    <label for="assignee">协作人员：</label>
    <select id="assignee" class="form-control">
    </select>
  </div>
  <div class="form-group">
    <label for="description">协作内容：</label>
    <textarea id="description" class="form-control" rows="3" placeholder="请输入需要协作的工作内容，例如跟客户会面、提交某个文档、组织一个会议等等"></textarea>
  </div>
  <div class="form-group">
    <label for="comment">留言：</label>
    <textarea id="comment" class="form-control" rows="3" placeholder="跟具体工作内容无关的一些提醒信息，例如请尽快完成、电话沟通等等，可以不填"></textarea>
  </div>

  <div class="form-group">
    <label for="attachments">文件附件：</label>
    <button type="button" class="btn btn-default" onclick="addFile(this)">添加附件</button>
    <p class="help-block">点击“添加附件”按钮增加附件，点击“X”按钮删除对应附件</p>
  </div>
 
  <button type="button" class="form-control" onclick="upload()">发起协作</button>
</form>

</div>

<script>
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
    startCooperation();
  }
};

var startCooperation = function() {
  var assignee=$("#assignee").val();
  var description=$("#description").val();
  var comment=$("#comment").val();
  gotoPage("/todo/startTodoSubmit", {assignee:assignee, description:description, comment:comment,
    decisionInfo:"启动协作流程", attachmentIds:attachmentIds});
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
    $("#assignee").html(userOptionHtml);
  },
  error : function(XMLHttpRequest, textStatus, errorThrown) {
    alert(errorThrown);
  }
});
</script>