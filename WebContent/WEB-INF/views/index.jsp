<html>
<head>
  <meta charset="utf-8">
  <title>丝途商道</title>
  <link rel="stylesheet" href="resources/css/bootstrap.min.css">
  <link rel="stylesheet" href="resources/css/stourway.css">
  <link rel="stylesheet" type="text/css" href="resources/js/CLEditor/jquery.cleditor.css" />
  <link rel="stylesheet" href="resources/css/datepicker.css">
  <script src="resources/js/jquery-1.8.3.js"></script>
  <script src="resources/js/jquery.form.js"></script>
  <script src="resources/js/bootstrap.min.js"></script>
  <script src="resources/js/bootstrap-datepicker.js"></script>
  <script src="resources/js/bootstrap-datepicker.zh-CN.js"></script>
  <script src="resources/js/CLEditor/jquery.cleditor.js"></script>
  <script src="resources/js/CLEditor/jquery.cleditor.table.min.js"></script>
  <script src="resources/js/CLEditor/jquery.cleditor.qzsituimage.js"></script>
  <script src="resources/js/stourway.js"></script>
</head>
<body>
	<div id="headDiv">
   <div id="head_box"></div>
	 <div id="bannerDiv" style="padding-bottom:5px">
		 <h1>丝途商道<small>智慧的企业</small></h1>
		 <p class="text-right" style="margin-top:-30px"><%=request.getAttribute("today") %></p>
	 </div>
	</div>
  <div id="bodyDiv">
	  <div style="width: 100%; float: right; height: 360px;">
	    <div style="padding-left:237px;">
	      <div id="mainDiv" class="panel panel-default" style="min-height:570px; height:auto!important; height:570px; padding:10px;"> 
	  
	      </div>
	    </div>
	  </div>
	  <div id="panelDiv" style="font-size:12px;width:232px;float:right; margin-right:-232px;"> 
	    <div style="clear:both;"></div>
	    <div id="accordion">
		    <div class="list-group">
			    <a class="list-group-item active">我的工作台</a>
			    <a class="list-group-item" id="myWorkspace" onclick="gotoPage('/todo/infoCenter')">任务中心</a>
          <a class="list-group-item" id="knowledge" onclick="gotoPage('/knowledge/knowledge/all/all/1')">知识经验</a>
          <a class="list-group-item" id="companyNews" onclick="gotoPage('/xmod/taobao/taobao')">淘宝业务</a>
	    	  <a class="list-group-item" id="projectProcess" onclick="gotoPage('/xmod/azdProject/azdProject')">项目流程</a>
          <a class="list-group-item" id="accountant" onclick="gotoPage('/xmod/accountant/accountant')">会计财务</a>
	        <a class="list-group-item" id="companyNews" onclick="gotoPage('/xmod/companyNews/companyNews')">新闻公告</a>
	      </div>
	      
	      <div class="list-group">
	        <a class="list-group-item active">系统管理</a>
	        <a class="list-group-item" id="group" onclick="gotoPage('/group')">角色管理</a>
	        <a class="list-group-item" id="employee" onclick="gotoPage('/employee')">用户管理</a>
	        <a class="list-group-item" id="xmodManage" onclick="gotoPage('/xmodManage')">模块权限</a>
	        <a class="list-group-item" id="permission" onclick="gotoPage('/permission')">系统权限</a>
	      </div>
	      <div class="list-group">
	        <a class="list-group-item active">我的信息</a>
	        <a class="list-group-item" id="chPassword" onclick="gotoPage('/chPassword')">修改密码</a>
	        <a href="/logout" class="list-group-item">退出登录<span class="text-primary">(<%=request.getAttribute("userName") %>)</span></a>
	      </div>
	    </div>
	  </div>&nbsp;
  </div>
  <iframe id="uploadFileIFrame" name="uploadFileIFrame" style="display:none;"></iframe>
</body>
</html>