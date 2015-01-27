<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<% List<Map<String,String>> contactUnitList = (List<Map<String,String>>)request.getAttribute("contactUnitList"); %>
<div id="contentPage">
<div id="azdProjectList" class="panel panel-default">
  <div class="panel-heading">往来对象</div>
  <div class="panel-body">
    <table class="table">
      <thead><tr><th>编码</th><th>名字</th><th>类型</th><th>电话</th><th>地址</th><th>备注</th><th></th></tr></thead>
      <tbody>
    <% 
        for(int i = 0; i < contactUnitList.size(); i++) {
        	Map<String,String> contactUnit = (Map<String,String>) contactUnitList.get(i);
    %>
          <tr id="<%=contactUnit.get("id") %>">
            <td><%=contactUnit.get("code") %></td>
            <td><%=contactUnit.get("name") %></td>
            <td><%=contactUnit.get("isCustomer") + contactUnit.get("isSupplier") %></td>
            <td><%=contactUnit.get("phone") %></td>
            <td><%=contactUnit.get("address") %></td>
            <td><%=contactUnit.get("comment") %></td>
            <td>
            	<button onclick="editContactUnit('<%=contactUnit.get("id") %>')">编辑</button>
            	<button onclick="removeContactUnit('<%=contactUnit.get("id") %>')">删除</button>
            </td>
          </tr>
    <%
        } 
    %>
    	<tr>
    		<td colspan="7"><button onclick="addContactUnit()">添加对象</button></td>
    	</tr>
      </tbody>
    </table>
  </div>
</div>


</div>


<div class="modal fade" id="editContactUnitModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">往来对象</h4>
      </div>
      <div class="modal-body">
        <form id="contactUnitForm" action="/xmod/accountant/editContactUnit" method="post">
          <input type="hidden" id="id" name="contactUnit[id]" value="0" />
		      <div class="form-group">
				    <label for="code">编码：</label>
				    <input type="text" id="code" name="contactUnit[code]" class="form-control" placeholder="编码" />
				  </div>
				  <div class="form-group">
				    <label for="name">名称：</label>
				    <input type="text" id="name" name="contactUnit[name]" class="form-control" placeholder="名称" />
				  </div> 
          <div class="form-group">
          	<label for="phone">联系电话：</label>
          	<input type="text" id="phone" name="contactUnit[phone]" class="form-control" placeholder="联系电话" />
          </div>
          <div class="form-group">
          	<label for="address">地址：</label>
          	<input type="text" id="address" name="contactUnit[address]" class="form-control" placeholder="地址" />
          </div>
          <div class="form-group"> 
	          <input id="isCustomer" type="checkbox" name="contactUnit[isCustomer]" value="true" />是客户
	          <input id="isSupplier" type="checkbox" name="contactUnit[isSupplier]" value="true" />是供应商
          </div>
          <div class="form-group">
          	<textarea id="comment" name="contactUnit[comment]" class="form-control" rows="3" placeholder="备注信息"></textarea>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="editContactUnitConfirm()">确认</button>
      </div>
    </div>
  </div>
</div>

<script>
var editContactUnitId = 0;

var addContactUnit = function() {
	editContactUnitId = 0;
	$("#editContactUnitModal input:text").val("");
	$("#comment").val("");
	$("#editContactUnitModal input:checkbox").attr("checked", false);
	$("#editContactUnitModal").modal("show");
};
var editContactUnit = function(id) {
  $("#editContactUnitModal #id").val(id);
	$("#editContactUnitModal #code").val($("#"+id).find("td:eq(0)").text());
	$("#editContactUnitModal #name").val($("#"+id).find("td:eq(1)").text());
	$("#editContactUnitModal #phone").val($("#"+id).find("td:eq(3)").text());
	$("#editContactUnitModal #address").val($("#"+id).find("td:eq(4)").text());
	$("#editContactUnitModal #comment").html($("#"+id).find("td:eq(5)").text());
	
	if($("#"+id).find("td:eq(2)").text().indexOf("客户")>=0)
		$("input:checkbox[name=isCustomer]:eq(0)").attr("checked", true);
	if($("#"+id).find("td:eq(2)").text().indexOf("供应商")>=0)
		$("input:checkbox[name=isSupplier]:eq(0)").attr("checked", true);
	$("#editContactUnitModal").modal("show");
};
var removeContactUnit = function(id) {
	if(confirm("确定要删除这个往来对象吗?")) { 
		gotoPage("/xmod/accountant/removeContactUnit?id=" + id); 
	}
};
var editContactUnitConfirm = function() {
  $("div.modal-backdrop").remove();
  submitForm($("#contactUnitForm"));
};
</script>