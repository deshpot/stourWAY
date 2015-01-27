<%@page import="com.qzsitu.stourway.domain.*"%>
<%@page import="java.util.*"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<% List<Group> groups= (List<Group>)request.getAttribute("groups"); %>
<% Map<Group, List<User>> groupUsers= (Map<Group, List<User>>)request.getAttribute("groupUsers"); %>
<% List<User> allUser = (List<User>)request.getAttribute("allUser"); %>
<div id="groupList">
  <% for(Group group : groups) { %>
    <div id="<%=group.getId() %>" pid="<%=group.getParentGroup() %>" >
      <span class="glyphicon <%=("role".equals(group.getType())?"glyphicon-user":"glyphicon-home") %>"><%=group.getName() %></span>
      <% if(!"role".equals(group.getType())) { %>
        <button type="button" onclick="addGroup('<%=group.getId() %>')" class="btn btn-primary btn-xs">添加子部门/角色</button>
      <% } else { %>
        <button type="button" onclick="addUser('<%=group.getId() %>')" class="btn btn-primary btn-xs">设置人员</button>
      <% } %>
      <button type="button" onclick="editGroup('<%=group.getId() %>')" class="btn btn-primary btn-xs">重命名</button>
      <button type="button" onclick="removeGroup('<%=group.getId() %>')" class="btn btn-danger btn-xs">删除</button>
      <% if("role".equals(group.getType())) { %>
        <% List<User> userList = (List<User>)groupUsers.get(group); %>
        <% for(User user:userList) { %>
            <br><span class="userName" name="<%=user.getId() %>"><%=user.getName() %></span>
        <% } %>
      <% } %>
    </div>
  <% } %>
</div>
<button type="button" class="btn btn-success btn-xs" style="margin-left:35px;"  onclick="gotoPage('/group')">返回</button>

<div class="modal fade" id="addUserModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">请选择人员</h4>
      </div>
      <div class="modal-body">
        <% for(User user:allUser) { %>
           <input name="addUserToRole" type="checkbox" value="<%=user.getId() %>" /><label><%=user.getName() %></label>
        <% } %>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="addUserConfirm()">确认</button>
      </div>
    </div>
  </div>
</div>

<script>
/************************重新排序，体现父子部门关系 ************************/
$.each($("#groupList div"), function(index, obj) {
  if($(obj).attr("pid") != "") {
    $("#"+$(obj).attr("pid")).append($(obj));
  }
});
/************************重新排序，体现父子部门关系 ************************/


/************************ 增加角色或部门 ************************/
var addGroup = function(id) {
  $("#"+id+" button:eq(2)").after("<div><span class='glyphicon glyphicon-minus'>&nbsp;<input type='hidden' value='"+id+"'/>"
      +"<input type='text' />&nbsp;<select><option value='department'>部门</option><option value='role'>角色</option></select></span></div>");
  $("#"+id+" div:eq(0)").append("&nbsp;<button type='button' class='btn btn-primary btn-xs' onclick='submitAdd(this)'>确认</button>"
      +"&nbsp;<button type='button' class='btn btn-danger btn-xs' onclick='cancelAdd(this)'>取消</button>");
};
var submitAdd = function(obj) {
  var pId = $(obj).parent().find("input:eq(0)").val();
  var name = $(obj).parent().find("input:eq(1)").val();
  var type = $(obj).parent().find("select").val();

  var addTo = $(obj).parent();
  
  $.ajax({
    type : "post",
    url : "/editGroup",
    data : JSON.stringify({
      id : "0",
      name : name,
      type : type,
      parentGroup : pId,
      description : ""
    }),
    dataType : "json",
    contentType : 'application/json;charset=UTF-8',
    success : function(data) {
      addTo.html("");
      addTo.attr("id", data.id);
      addTo.attr("pId", data.parentGroup);
      if(data.type == "role") {
        addTo.append("<span class='glyphicon glyphicon-user'>"+data.name+"</span>");
        addTo.append("&nbsp;<button type='button' onclick=\"addUser('"+data.id+"')\" class='btn btn-primary btn-xs'>设置人员</button>");
      } else {
        addTo.append("<span class='glyphicon glyphicon-home'>"+data.name+"</span>");
        addTo.append("&nbsp;<button type='button' onclick=\"addGroup('"+data.id+"')\" class='btn btn-primary btn-xs'>添加子部门/角色</button>");
      }
      addTo.append("&nbsp;<button type='button' onclick=\"editGroup('"+data.id+"')\" class='btn btn-primary btn-xs'>重命名</button>");
      addTo.append("&nbsp;<button type='button' onclick=\"removeGroup('"+data.id+"')\" class='btn btn-danger btn-xs'>删除</button>");
    },
    error : function(XMLHttpRequest, textStatus, errorThrown) {
      alert(errorThrown);
    }
  });
};
var cancelAdd = function(obj) {
  $(obj).parent().remove();  
};
/************************ 增加角色或部门 ************************/


/************************ 编辑角色或部门 ************************/
var editGroup = function(id) {
  old = $("#"+id+" span:eq(0)").html();
  $("#"+id+" span:eq(0)").html("");
  $("#"+id+" button:lt(3)").hide();
  
  $("#"+id+" span:eq(0)").append("<input type='hidden' value='"+old+"'/>"
      +"<input type='text' value='"+old+"' />");
  $("#"+id+" span:eq(0)").append("<button type='button' class='btn btn-primary btn-xs' onclick='submitEdit(this)'>确认</button>"
      +"<button type='button' class='btn btn-danger btn-xs' onclick='cancelEdit(this)'>取消</button>");
};
var submitEdit = function(obj) {
  var id = $(obj).parent().parent().attr("id");
  var pid = $(obj).parent().parent().attr("pid");
  var name = $(obj).parent().find("input:eq(1)").val();
  var type = $(obj).parent().hasClass("glyphicon-user")?"role":"";
  var addTo = $(obj).parent();
  
  $.ajax({
    type : "post",
    url : "/editGroup",
    data : JSON.stringify({
      id : id,
      name : name,
      type : type,
      parentGroup : pid,
      description : ""
    }),
    dataType : "json",
    contentType : 'application/json;charset=UTF-8',
    success : function(data) {
      addTo.html(data.name);
      $(addTo).parent().find("button:lt(3)").show();
    },
    error : function(XMLHttpRequest, textStatus, errorThrown) {
      alert(errorThrown);
    }
  });
};
var cancelEdit = function(obj) {
  $(obj).parent().parent().find("button:lt(5)").show();
  $(obj).parent().html($(obj).parent().find("input[type=hidden]").val());  
};
/************************ 编辑角色或部门 ************************/


/************************ 删除角色或部门 ************************/
var removeGroup = function(id) {
  $.ajax({
    type : "post",
    url : "/removeGroup/"+id,
    dataType : "text",
    success : function(data) {
      $("#"+id).remove();
    },
    error : function(XMLHttpRequest, textStatus, errorThrown) {
      alert(errorThrown);
    }
  });
};
/************************ 删除角色或部门 ************************/

/************************ 设置角色对应用户 ************************/
var addUserTo = "";
var addUser = function(id) {
  addUserTo = id;
  $("input:checkbox[name=addUserToRole]").attr("checked", false);
  $.each($("#"+id).find("span.userName"), function(index, obj){
    var userId = $(obj).attr("name");
    $("input:checkbox[value="+userId+"]").attr("checked", true);
  });
  $("#addUserModal").modal("show");
};
var addUserConfirm = function() {
  var parameters = {};
  var userSize = 0;
  $.each($("input:checkbox[name=addUserToRole]:checked"), function(index,obj){
    parameters["user"+index] = $(obj).val();
    userSize = userSize + 1;
  });
  parameters["userSize"] = userSize;
  parameters["roleId"] = addUserTo;
  
  $.ajax({
    type : "post",
    url : "/addUserToRole",
    data : parameters,
    success : function(data) {
      $("#"+addUserTo).find("span.userName").remove();
      $("#"+addUserTo).find("br").remove();
      $.each($("input:checkbox[name=addUserToRole]:checked"), function(index,obj){
        $("#"+addUserTo).append("<br><span class='userName' name='"+$(obj).val()+"'>"+$(obj).next("label").text()+"</span>");
      });
      $("#addUserModal").modal("hide");
    },
    error : function(XMLHttpRequest, textStatus, errorThrown) {
      $("#addUserModal").modal("hide");
      alert(errorThrown);
    }
  });
};
/************************ 设置角色对应用户 ************************/
</script>