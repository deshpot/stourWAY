<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>

  <div class="form-group">
    <label>旧密码：</label>
    <input type="password" id="password" name="password" placeholder="请输入旧密码">
  </div>
  <div class="form-group">
    <label>输入新密码：</label>
    <input type="password" id="password1" name="password1" placeholder="请输入新密码">
  </div>
  <div>
  	<label>确认新密码：</label>
  	 <input type="password" id="password2" name="password2" placeholder="请确认新密码">
  </div>
  <button id="submit" class="btn btn-default">确定</button>
<script>
$("#submit").bind("click",function(){
	$.ajax({
		type : "post",
		url : "/changePassword",
		data : {
			password : $("#password").val(),
			password1 : $("#password1").val(),
			password2 : $("#password2").val()
		},
		success: function (data) {
			if(data=="1"){
				alert("输入新密码与确认新密码不一致,请从新输入!");
			}else if(data=="2"){
				alert("密码错误,请从新输入!");
			}else if(data=="3"){
				alert("请输入新密码!");
			}else{
				alert("修改成功");
				gotoPage("/todo/infoCenter");
			}
	    },
	    error: function (XMLHttpRequest, textStatus, errorThrown) {
	      alert(errorThrown);
	    }
	});	
});
</script>