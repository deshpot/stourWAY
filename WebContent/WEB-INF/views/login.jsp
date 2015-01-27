<%@page contentType="text/html;charset=UTF-8"%>
<%@page language="java" pageEncoding="UTF-8"%>

<% String msg = (null == request.getAttribute("msg") ? "" : request.getAttribute("msg").toString()); %>
<html>
<head>
  <meta charset="utf-8">
  <title>丝途商道</title>
  <link rel="stylesheet" href="resources/css/bootstrap.min.css">
  <link rel="stylesheet" href="resources/css/stourway.css">
  <link rel="stylesheet" type="text/css" href="resources/js/CLEditor/jquery.cleditor.css" />
  <script src="resources/js/jquery-1.8.3.js"></script>
  <script src="resources/js/bootstrap.min.js"></script>
</head>
<body>

<div id="loginDiv" class="container" style="width:360px;margin-top:20%;">
<form role="form" method="post" action="/">
  <div class="form-group">
	  <input style="font-size:16px;height:48px;" type="text" id="id" name="id" value="" placeholder="用户名" class="form-control">
	</div>
  <div class="form-group">
    <input style="font-size:16px;height:48px;" type="password" id="password" name="password" value="" placeholder="密码" class="form-control">
  </div>
  <div class="form-group">
    <input style="font-size:16px;height:48px;" class="btn btn-primary btn-block border-none" name="commit" type="submit" value="登录">
  </div>
</form>
</div>
</body>
</html>