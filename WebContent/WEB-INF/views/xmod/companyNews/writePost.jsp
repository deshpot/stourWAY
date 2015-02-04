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
  <div class="form-group" style="margin:10px 0px;">
    <!-- <textarea id="postDiv" name="post[content]" class="form-control"><%=post.get("content") == null? "":post.get("content") %></textarea> -->
    <textarea id="postDiv" name="post[content]" type="text/plain"><%=post.get("content") == null? "":post.get("content") %></textarea>
    <script type="text/javascript" src="/resources/js/UEditor/ueditor.config.js"></script>
    <script type="text/javascript" src="/resources/js/UEditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="/resources/js/UEditor/lang/zh-cn/zh-cn.js"></script>
    <!-- 实例化编辑器 -->
    <script type="text/javascript">
        var ue = UE.getEditor('postDiv');
    </script>
  </div>
	<br>
	<button  onclick="submitPost()" type="button">提 交</button>
	<button  onclick="gotoPage('/xmod/companyNews/companyNews')" type="button">撤 销</button>
</form>
</div>

<script>
  var submitPost = function() {
    submitForm($("#postForm"));
  };
</script>