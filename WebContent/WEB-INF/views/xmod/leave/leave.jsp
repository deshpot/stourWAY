<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>


<div id="contentPage">
<table class="table table-hover">
	<thead>
	<tr><th colspan="4">请假记录</th></tr>
		<tr>
			<td>申请日期</td>	
			<td>请假日期</td>
			<td>销假日期</td>
			<td>批准人</td>
		</tr>
	</thead>
	<tbody>
		<tr></tr>
	</tbody>
</table>
<button onclick="gotoPage('/xmod/leave/askLeave')">请假</button>
</div>