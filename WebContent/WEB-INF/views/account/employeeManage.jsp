<%@page import="com.qzsitu.stourway.domain.Group"%>
<%@page import="com.qzsitu.stourway.domain.User"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<% List<User> users= (List<User>)request.getAttribute("groups"); %>
<div id="manage">
<table id="userTable" class="table table-hover table-condensed">
		<thead>
			<tr>
				<th>姓名</th>
				<th>邮箱</th>
				<th>电话</th>
				<th>手机</th>
				<th>部门/团队</th>
				<th>管理</th>
			</tr>
		</thead>
		<tbody>
				<% for(User user : users) { %>
		          <tr id="<%=user.getId() %>">
		            <td><%=user.getName() %></td>
		            <td><%=user.getEmail() %></td>
		            <td><%=user.getTelephone() %></td>
		            <td><%=user.getMobilephone() %></td>
		            <td>
		            	<%List<Group> groups=user.getGroupList(); %>
		            	<%for(Group group : groups){ %>
		            	<%=group.getName() %>,&nbsp;
		            	<%} %>
		            </td>
		            <td>
		            	<button onclick="beginEdit('<%=user.getId() %>')" type="button">修改</button>
		            	<button onclick="removeUser('<%=user.getId() %>')" type="button">删除</button>
		            	<button onclick="resetPsd('<%=user.getId() %>')">重置密码</button>
		            </td>
		          </tr>
		        <% } %>

		</tbody>
	</table>
	<button type="button" id="newUser" >添加员工</button>
</div>
<div id="edit" style="width: 420px;">
	<div>
		<input type="hidden" id="operate" />
	<div class="input-group input-group-sm">
		<span class="input-group-addon" id="uId">账  号：</span> 
		<input type="text" id="userId" class="form-control"/>
		<span class="input-group-addon">姓  名：</span> 
		<input type="text" id="userName" class="form-control"/>
	</div>
	<div class="input-group input-group-sm">
		<span class="input-group-addon">手  机：</span>
		<input type="text" id="mobilephone" class="form-control"/>
	</div>
	<div class="input-group input-group-sm">
		<span  class="input-group-addon">固定电话：</span>
		<input type="text" id="telephone" class="form-control"/>
	</div>
	<div class="input-group input-group-sm">
		<span  class="input-group-addon">Email：</span>
		<input type="text" id="email" class="form-control"/>
	</div>
	<div>
</div>
	<button id="submitUser" type="button">提 交</button>
	<button id="cancel" type="button">撤 销</button>
</div>
</div>
<script>
var resetPsd =function(id){
	$.ajax({
		type : "post",
		url : "/resetPsd/"+id,
		dataType : "json",
		success : function(data) {
			alert("重置成功为默认密码：123456");
		}
	});
};
var groupCheck=function(){
	$.ajax({
		type : "post",
		url : "/getAllGroup",
		dataType : "json",
		success : function(data) {
			var groupCheckHtml = "";
			$(data).each(
					function(index, element) {
						groupCheckHtml +="<lable>" 
							+"<input id=\"group\" name=\"group\" type=\"checkbox\" value=\""+ element.id + "\" />"
							+ element.name
							+" </label>&nbsp;";
						});
			$("#groupCheck").html(groupCheckHtml);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
};
var beginManage=function(){
	$("#edit").hide();
	$("#manage").show();

};
var beginEdit = function(id) {
	$("#manage").hide();
	$("#edit").show();
	$("#userId").hide();
	$("#uId").hide();
	$("input[type='checkbox']").each(function() { 
		$(this).attr("checked", false); 
		}); 
	$.ajax({
		type : "post",
		url : "/getUser/" + id,
		dataType : "json",
		success : function(data) {
			$("#operate").val("0");
		    $("#userId").val(data.id);
		    $("#userName").val(data.name);
		    $("#telephone").val(data.telephone);
		    $("#mobilephone").val(data.mobilephone);
			$("#email").val(data.email);
			$.each(data.groupList, function(index, group) {

			});
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
};
var removeUser = function(id){
	$.ajax({
		type : "post",
		url : "/removeUser/" + id,
		 dataType: "json",
	        success: function (data) {
	        	
	        	gotoPage("/employeeManage");
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	          alert(errorThrown);
	        }
	});
};
$("#newUser").bind("click", function(){
	$("#manage").hide();
	$("#edit").show();
	$("#userId").show();
	$("#uId").show();
	$("#operate").val("1");
    $("#userId").val("");
    $("#userName").val("");
    $("#password").val("");
    $("#telephone").val("");
    $("#mobilephone").val("");
    $("#email").val("");
    $("input[type='checkbox']").each(function(){    
        $(this).attr("checked", false);    
    });
});

$("#submitUser").bind("click", function() {
	var operate = $("#operate").val();
	var userId = $("#userId").val();
	re = new RegExp("^[a-zA-Z][a-zA-Z0-9_]{5,31}$");
	if(!re.test(userId)) {
	  alert("用户名只能是字母、数字和下划线组成的6-32位字符");
	  return;
	}
	var userName = $("#userName").val();
	var password =$("#password").val();
	var telephone = $("#telephone").val();
	var mobilephone = $("#mobilephone").val();
	var email = $("#email").val();
	var groupListInfo = "";
	 	$.each($("input[type='checkbox']:checked"),function(index, value){
	 		groupListInfo = groupListInfo + "-" + $(value).val();
		  });
	$.ajax({
		type : "post",
		url : "/editUser/" + operate,
		data : JSON.stringify({
			id : userId,
			name : userName,
			password : password,
			telephone : telephone,
			mobilephone : mobilephone,
			email : email
		}),
		dataType : "text",
		contentType : 'application/json;charset=UTF-8',
		success : function(data) {
			if(data==0){
			  gotoPage('/employeeManage');
			}else{
				alert("用户名重复！请从新输入！");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
});
$("#cancel").bind("click", function(){
	gotoPage('/employeeManage');
});
beginManage();
groupCheck();
</script>