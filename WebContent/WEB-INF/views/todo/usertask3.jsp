<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.task.Task"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<div id="contentPage">

<div id="basicInfo"></div>
<div id="historyRecord"></div>

<div class="bs-callout bs-callout-danger">
  <h4>决策区域</h4>
  <form id="taskForm" target="uploadFileIFrame" class="form" action="file/upload" method="post" enctype="multipart/form-data">
    <div class="form-group">
      <label for="comment">留言：</label>
      <textarea id="comment" class="form-control" rows="3" placeholder="填写留言信息，可以给后续的任务人员带来帮助，建议留下您做决策的理由"></textarea>
    </div>
    <div class="form-group">
      <label for="attachments">文件附件：</label>
      <button type="button" class="btn btn-default" onclick="addFile(this)">添加附件</button>
      <p class="help-block">点击“添加附件”按钮增加附件，点击“X”按钮删除对应附件</p>
    </div>
    <div class="form-group">
      <label for="comment">决策：</label>
      <div class="radio">
        <label>
          <input type="radio" name="decision" id="decision1" value="ok" checked><span>任务顺利完成</span>
        </label>
      </div>
      <div class="radio">
        <label>
          <input type="radio" name="decision" id="decision2" value="nok"><span>需要重新执行</span>
        </label>
      </div>
      <button type="button" class="form-control" onclick="upload()">完成</button>
    </div>
  </form>
</div>

</div>

<script>
loadPage($("#basicInfo"),"/todo/basicInfo/<%=request.getAttribute("taskId") %>");
loadPage($("#historyRecord"),"/todo/historyRecord/<%=request.getAttribute("taskId") %>");

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
    completeTask();
  }
};

var completeTask = function() {
  var comment=$("#comment").val();
  var decision=$("input[name='decision']:checked").val();
  var decisionInfo=$("input[name='decision']:checked").next("span").html();
  gotoPage("/todo/completeTask/<%=request.getAttribute("taskId") %>",
      {comment:comment, decision:decision, decisionInfo:decisionInfo, attachmentIds:attachmentIds});
};
</script>