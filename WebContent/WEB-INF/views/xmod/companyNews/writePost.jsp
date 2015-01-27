<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<% Map<String, Object> post = (Map<String, Object>)request.getAttribute("post"); %>

<div id="contentPage">
<form id="postForm" action="/xmod/companyNews/writePostSubmit" method="post">
  <input type="hidden" id="id" name="post[id]" value="<%=post.get("title") == null? "0":post.get("id") %>" />
	<div class="input-group input-group-sm">
	  <span class="input-group-addon">标 题：</span>
	  <input type="text" id="postTitle" name="post[title]" class="form-control" value="<%=post.get("title") == null? "":post.get("title") %>"/>
	</div>
  <div class="form-group text-center" style="margin:10px 0px;">
    <textarea id="postDiv" name="post[content]" class="form-control"><%=post.get("content") == null? "":post.get("content") %></textarea>
  </div>
	<br>
	<button  onclick="submitPost()" type="button">提 交</button>
	<button  onclick="gotoPage('/xmod/companyNews/companyNews')" type="button">撤 销</button>
</form>
</div>


<script>
  $("#postDiv").cleditor({
            width : '95%',
            height : 400
          });
  
  var uploadCallback = function(code, msg) {
    if (code == 0) {
      url = "/file/download/" + msg.split("\\|")[0].split(":")[0];
      var editor = $("#postDiv").cleditor()[0];
      editor.execCommand("inserthtml", "<img style=\"max-width:760px;width:expression(this.width>760?'760px':this.width);\" src=\"" + url + "\">",
        null, $.cleditor.buttons.qzsituimage.button);
      editor.hidePopups();
      editor.focus();
    }
  };
  
  var submitPost = function() {
    submitForm($("#postForm"));
  };
</script>