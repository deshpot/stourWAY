<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<div id="contentPage">

<form id="taskForm" target="uploadFileIFrame" class="form" action="file/upload" method="post" enctype="multipart/form-data">
  <div class="form-group">
    <label for="buyer">甲方公司：</label>
    <input type="text" id="buyerCompany" class="form-control" />
    <label for="buyer">甲方联系人：</label>
    <input type="text" id="buyerContact" class="form-control" />
  </div>
  <div class="form-group">
    <label for="projectManager">业务经理：<%=request.getAttribute("businessManager") %></label>
  </div>
  <div class="form-group">
    <label for="projectManager">项目经理：</label>
    <select id="projectManager" class="form-control"></select>
    <label for="financialManager">财务经理：</label>
    <select id="financialManager" class="form-control"></select>
  </div>
  <div class="form-group">
    <label for="projectName">项目名字：</label>
    <input type="text" id="projectName" class="form-control" placeholder="项目名称" />
  </div>
  <div class="form-group">
    <label for="description">项目概况：</label>
    <textarea id="description" class="form-control" rows="3" placeholder="项目的简要概况信息"></textarea>
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
 
  <button id="submitProjectInfo" type="button" class="form-control" onclick="upload()">提交项目概况</button>
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
    submitPage();
  }
};

var submitPage = function() {
  $("#submitProjectInfo").hide();
  var buyerCompany=$("#buyerCompany").val();
  var buyerContact=$("#buyerContact").val();
  var projectManager=$("#projectManager").val();
  var financialManager=$("#financialManager").val();
  var projectName=$("#projectName").val();
  var description=$("#description").val();
  var comment=$("#comment").val();
  gotoPage("/xmod/azdProject/startProjectSubmit", {buyerCompany:buyerCompany, buyerContact:buyerContact,
    projectManager:projectManager, financialManager:financialManager, projectName:projectName, 
    description:description, comment:comment, decisionInfo:"提交项目概况", attachmentIds:attachmentIds});
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
    $("#projectManager").html(userOptionHtml);
    $("#financialManager").html(userOptionHtml);
  },
  error : function(XMLHttpRequest, textStatus, errorThrown) {
    alert(errorThrown);
  }
});
</script>