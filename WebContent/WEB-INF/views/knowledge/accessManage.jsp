<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.qzsitu.stourway.domain.*"%>
<%@page import="java.util.List"%>
<% List<KnowledgeMenu> menuList = (List<KnowledgeMenu>) request.getAttribute("menuList"); %>
<% List<String> roleList= (List<String>)request.getAttribute("roleList"); %>
<% List<String> rolePermissionList= (List<String>)request.getAttribute("rolePermissionList"); %>
<div id="contentPage">
<table class="table table-hover">
    <thead>
      <tr>
        <th style="width:120px;"></th>
        <th>一般员工</th>
        <% for(String role:roleList){ %>
        <th>
            <h4><%=role.split(":")[0] %><br>
            <% if(role.split(":").length == 3) {%>
            <small><%=role.split(":")[2].replaceAll("-", "<br>") %></small>
            <% } %>
            </h4>
        </th>
        <% } %>
      </tr>
    </thead>
    <tbody>
    <% for(KnowledgeMenu menu : menuList) { %>
      <tr>
        <td id="<%=menu.getId() %>"><label><%=menu.getName() %></label>
          <br><a  onclick="editMenu('<%=menu.getId() %>')">重命名</a>
          <br><a  onclick="removeMenu('<%=menu.getId() %>')">删除</a>
        </td>
        <td>
          <input type="checkbox" id="everyone-<%=menu.getId() %>_read"
					   <%=(rolePermissionList.contains("everyone-" + menu.getId() + "_read"))?"checked":"" %>>
					<label>阅读</label><br>
          <input type="checkbox" id="everyone-<%=menu.getId() %>_publish"
					   <%=(rolePermissionList.contains("everyone-" + menu.getId() + "_publish"))?"checked":"" %>>
					<label>发布</label><br>
					<input type="checkbox" id="everyone-<%=menu.getId() %>_manage"
             <%=(rolePermissionList.contains("everyone-" + menu.getId() + "_manage"))?"checked":"" %>>
          <label>管理</label><br>
        </td>
        <% for(String role:roleList){ %>
          <td>
            <input type="checkbox" id="<%=role.split(":")[1] %>-<%=menu.getId() %>_read"
              <%=(rolePermissionList.contains(role.split(":")[1] + "-" + menu.getId() + "_read"))?"checked":"" %>>
            <label>阅读</label><br>
            <input type="checkbox" id="<%=role.split(":")[1] %>-<%=menu.getId() %>_publish"
              <%=(rolePermissionList.contains(role.split(":")[1] + "-" + menu.getId() + "_publish"))?"checked":"" %>>
            <label>发布</label><br>
            <input type="checkbox" id="<%=role.split(":")[1] %>-<%=menu.getId() %>_manage"
              <%=(rolePermissionList.contains(role.split(":")[1] + "-" + menu.getId() + "_manage"))?"checked":"" %>>
            <label>管理</label><br>
          </td>
        <% } %>
      </tr>
    <% } %>
    </tbody>
</table>
<div class="text-right">
  <button id="bt1" onclick="beginModify()" type="button" class="btn btn-primary">修改访问权限</button>
  <button id="bt2" onclick="submit()" type="button" class="btn btn-primary">完成</button>
  <button id="bt3" onclick="gotoPage('/knowledge/accessManage')" type="button" class="btn btn-default">撤销</button>
</div>
<div>
  <button onclick="createMenu()" type="button">创建新目录</button>
	<br><button onclick="gotoPage('/knowledge/knowledge/all/all/1')" type="button">返回知识库</button>
</div>
</div>

<div class="modal fade" id="newMenuNameModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">请输入新名字</h4>
      </div>
      <div class="modal-body">
          <div class="input-group input-group-sm">
				    <span class="input-group-addon">目录名字：</span>
				    <input type="text" id="newMenuName" class="form-control" value=""/>
				  </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="newMenuNameConfirm()">确认</button>
      </div>
    </div>
  </div>
</div>

<script>
//访问控制修改
$("input:checkbox").attr("disabled","disabled");
$("#bt2").hide();
$("#bt3").hide();
var beginModify = function(){
  $("input:checkbox").removeAttr("disabled");
  $("#bt2").show();
  $("#bt3").show();
  $("#bt1").hide();
};
var submit = function(){
  var permission = "";
  $("input:checkbox:checked").each(function(index,element){
    permission = permission + "-=STSD=-" + $(element).attr("id");
  });
  gotoPage("/knowledge/submitAccessManage", {permission:permission});
};
//访问控制修改


var menuId = "0";//menuId为0创建新的，否则修改名字
//创建目录
var createMenu = function() {
  menuId = "0";
  $("#newMenuName").val("");
  $("#newMenuNameModal").modal("show");
};

var editMenu = function(id) {
  menuId = id;
  $("#newMenuName").val($("#"+menuId).find("label").text());
  $("#newMenuNameModal").modal("show");
};
var newMenuNameConfirm = function() {
  $.ajax({
    type : "post",
    url : "/knowledge/setMenuName",
    data : {menuId:menuId, menuName:$("#newMenuName").val()},
    success : function(data) {
      if(menuId != "0") {
        $("#"+menuId).find("label").text($("#newMenuName").val());
        $("#newMenuNameModal").modal("hide");
      } else {
        $("#newMenuNameModal").modal("hide");
        gotoPage("/knowledge/accessManage");
      }
    },
    error : function(XMLHttpRequest, textStatus, errorThrown) {
      $("#newMenuNameModal").modal("hide");
      alert(errorThrown);
    }
  });
};
//修改目录名字


//删除目录
var removeMenu = function(id) {
  $.ajax({
    type : "post",
    url : "/knowledge/removeMenu",
    data : {menuId:id},
    success : function(data) {
      if(data == "ok"){
        $("#"+id).parent().remove();
        alert("删除成功。");
      }
      if(data == "nok")
        alert("该目录下有内容，无法删除。");
      $("#newMenuNameModal").modal("hide");
    },
    error : function(XMLHttpRequest, textStatus, errorThrown) {
      $("#newMenuNameModal").modal("hide");
      alert(errorThrown);
    }
  });
};
//删除目录
</script>