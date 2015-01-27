<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.qzsitu.stourway.domain.*"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<% List<KnowledgeTag> tagList = (List<KnowledgeTag>) request.getAttribute("tagList"); %>
<% List<KnowledgeMenu> menuList = (List<KnowledgeMenu>) request.getAttribute("menuList"); %>
<% String currentMenu = (String)request.getAttribute("menu"); %>
<% String currentTag = (String)request.getAttribute("tag"); %>
<% List<Post> posts= (List<Post>)request.getAttribute("posts"); %>
<% int currentPage = ((Number)request.getAttribute("page")).intValue(); %>
<% int pageCount = ((Number)request.getAttribute("pageCount")).intValue(); %>

<div id="contentPage">
  <table class="table table-hover">
    <thead>
       <tr>
        <td colspan="4">目录：
        <a  onclick="gotoPage('/knowledge/knowledgeManage/all/<%=currentTag %>/<%=currentPage %>')" id="menuall">所有</a>
        <% for(KnowledgeMenu menu : menuList) { %>
         &nbsp;|&nbsp;<a  onclick="gotoPage('/knowledge/knowledgeManage/<%=menu.getId() %>/<%=currentTag %>/<%=currentPage %>')" id="menu<%=menu.getId() %>"><%=menu.getName() %></a>
        <% } %>
        </td>
      </tr>
      <tr>
        <td colspan="4">标签：
        <a  onclick="gotoPage('/knowledge/knowledgeManage/<%=currentMenu %>/all/<%=currentPage %>')" id="tagall">所有</a>
        <% for(KnowledgeTag tag : tagList) { %>
          &nbsp;|&nbsp;<a  onclick="gotoPage('/knowledge/knowledgeManage/<%=currentMenu %>/<%=tag.getId() %>/<%=currentPage %>')" id="tag<%=tag.getId() %>"><%=tag.getName() %></a>
        <% } %>
        </td>
      </tr>
      <tr>
        <th>标题</th>
        <th>创建者</th>
        <th>创建时间</th>
        <th></th>
      </tr>
    </thead>
    <tbody>
      <% for(Post post : posts) { %>
      <tr id="<%=post.getId() %>">
        <td><%=post.getTitle().replaceAll("<", "&lt;").replaceAll("<", "&gt;") %></td>
        <td><%=post.getAuthor() %></td>
        <td><%=new SimpleDateFormat("yyyy年MM月dd日 hh时mm分").format(post.getDatetime()) %></td>
        <td>
          <button onclick="editKnowledge('<%=post.getId() %>')">编辑</button>
          <button onclick="removeKnowledge('<%=post.getId() %>')">删除</button>
        </td>
      </tr>
      <% } %>
    </tbody>
    <tfoot>
      <tr>
        <td colspan="4"><span>分页：</span> <span> 
            <% for(int i = 1; i <= pageCount; i++) { %>
            <%   if(i == currentPage) { %> <span style="color: red;"><%=i %></span>
            <%   } else { %> 
              <a onclick="gotoPage('/knowledge/knowledgeManage/<%=currentMenu %>/<%=currentTag %>/<%=i %>')" ><%=i %></a>
            <%   }  %> 
            <% } %>
        </span></td>
      </tr>
    </tfoot>
  </table>
 <!-- <button onclick="gotoPage('/knowledge/newPost')">添加条目</button> --> 
  <button  onclick="gotoPage('/knowledge/knowledge/all/all/1')" type="button">退出管理</button>
</div>

<script>
var editKnowledge = function(postId) {
  gotoPage('/knowledge/<%=currentMenu %>/<%=currentTag %>/<%=currentPage %>/editPost/'+postId);
};
var removeKnowledge = function(postId) {
  gotoPage('/knowledge/<%=currentMenu %>/<%=currentTag %>/<%=currentPage %>/removePost/'+postId);
};
$("#menu<%=currentMenu %>").css("color","red");
$("#tag<%=currentTag %>").css("color","red");
</script>