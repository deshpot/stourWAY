<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.qzsitu.stourway.domain.*"%>
<%@page import="java.util.List"%>
<% List<KnowledgeMenu> menuList = (List<KnowledgeMenu>) request.getAttribute("menuList"); %>
<% String currentMenu = (String) request.getAttribute("currentMenu"); %>
<% List<KnowledgeTag> tagList = (List<KnowledgeTag>) request.getAttribute("tagList"); %>
<div id="contentPage">
  <input type="hidden" id="postId" value="0" />
	<div class="input-group input-group-sm">
	  <span class="input-group-addon">标 题：</span>
	  <input type="text" id="postTitle" class="form-control"/>
	</div>
  <div class="form-group text-center" style="margin:10px 0px;">
    <textarea id="postDiv" class="form-control"></textarea>
  </div>

	<div class="input-group input-group-sm">
		<select class="form-control" id="menu">
		 <% for(KnowledgeMenu menu : menuList) { %>
		  <option value="<%=menu.getId() %>" <%=(menu.getId().equals(currentMenu))?"selected":"" %>><%=menu.getName() %></option>
		 <% } %>
		</select>
		<span class="input-group-addon">标签：</span>
    <input type="text" id="tags" class="form-control" placeholder="多个标签用空格隔开" />
	</div>
	<br>
	<button id="submitPost" onclick="submitPost()" type="button">提 交</button>
	<button  onclick="gotoPage('/knowledge/knowledge/<%=currentMenu %>/all/1')" type="button">撤 销</button>
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
    var postId=$("#postId").val();
    var postTitle=$("#postTitle").val();
    var postContent=$("#postDiv").val();
    var tags=$("#tags").val();
    var menu=$("#menu").val();
    gotoPage("/knowledge/newPost/<%=currentMenu %>", {postId:postId, postTitle:postTitle,
      postContent:postContent, tags:tags, menu:menu});
  };
</script>