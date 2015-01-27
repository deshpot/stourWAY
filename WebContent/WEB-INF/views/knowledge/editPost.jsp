<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.qzsitu.stourway.domain.*"%>
<%@page import="java.util.List"%>
<% Post post = (Post)request.getAttribute("post"); %>
<% String tag = (String)request.getAttribute("tag"); %>
<% List<KnowledgeMenu> menuList = (List<KnowledgeMenu>) request.getAttribute("menuList"); %>
<% List<KnowledgeTag> tagList = (List<KnowledgeTag>) request.getAttribute("tagList"); %>
<% String currentMenu = (String)request.getAttribute("currentMenu"); %>
<% String currentTag = (String)request.getAttribute("currentTag"); %>
<% int currentPage = (Integer.parseInt((String)request.getAttribute("currentPage"))); %>

<div id="contentPage">
  <input type="hidden" id="postId" value="<%=post.getId() %>" />
	<div class="input-group input-group-sm">
	  <span class="input-group-addon">标 题：</span>
	  <input type="text" id="postTitle" class="form-control" value="<%=post.getTitle() %>"/>
	</div>
  <div class="form-group text-center" style="margin:10px 0px;">
    <textarea id="postDiv" class="form-control"><%=post.getContent() %></textarea>
  </div>

	<div class="input-group input-group-sm">
    <select class="form-control" id="menu">
     <% for(KnowledgeMenu m : menuList) { %>
      <option value="<%=m.getId() %>" <%=(m.getId().equals(currentMenu))?"selected":"" %>><%=m.getName() %></option>
     <% } %>
    </select>
		<span class="input-group-addon">标签：</span>
    <input type="text" id="tags" class="form-control" placeholder="多个标签用空格隔开" value="<%=tag %>" />
	</div>
	<br>
	<button id="submitPost" onclick="submitPost()" type="button">提 交</button>
	<button id="cancelPost" onclick="cancelPost()" type="button">撤 销</button>
</div>


<script>
  $("#postDiv").cleditor({
            width : '95%', // width not including margins, borders or padding
            height : 400, // height not including margins, borders or padding
          });
  
  var uploadCallback = function(code, msg) {
    if (code == 0) {
      url = "/file/download/" + msg.split("\\|")[0].split(":")[0];
      var editor = $("#postDiv").cleditor()[0];
      editor.execCommand("inserthtml", "<img style=\"max-width: 800px;width:expression(this.width>800?'800px':this.width);\" src=\"" + url + "\">",
          null, $.cleditor.buttons.qzsituimage.button);
      editor.hidePopups();
      editor.focus();
    }
  };
  
  var submitPost = function() {
    var postId=$("#postId").val();
    var postTitle=$("#postTitle").val();
    var postContent=$("#postDiv").val();
    var tags=$("#tags").val();
    var menu=$("#menu").val();
    gotoPage("/knowledge/<%=currentMenu %>/<%=currentTag %>/<%=currentPage %>/editPost/"+postId, {postId:postId, postTitle:postTitle,
      postContent:postContent, tags:tags, menu:menu});
  };
  
  var cancelPost = function() {
    gotoPage("/knowledge/knowledgeManage/<%=currentMenu %>/<%=currentTag %>/<%=currentPage %>");
  };
</script>