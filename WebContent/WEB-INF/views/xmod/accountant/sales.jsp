<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<% List<Map<String,Object>> contactUnitList = (List<Map<String,Object>>)request.getAttribute("contactUnitList"); %>
<% List<Map<String,Object>> materielList = (List<Map<String,Object>>)request.getAttribute("materielList"); %>
<% List<Map<String,Object>> salesList = (List<Map<String,Object>>)request.getAttribute("salesList"); %>
<% List<Map<String,Object>> userList = (List<Map<String,Object>>)request.getAttribute("userList"); %>
<div id="contentPage">
<div id="azdProjectList" class="panel panel-default">
  <div class="panel-heading">销售单记录</div>
  <div class="panel-body">
    <table class="table">
      <thead><tr><th>编号</th><th>往来对象</th><th>收货人</th><th>物料</th><th>数量</th><th>总价格</th><th>备注</th><th>业务人员</th><th>审批人员</th><th></th></tr></thead>
      <tbody>
    <% 
        for(int i = 0; i < salesList.size(); i++) {
        	Map<String,Object> sales = (Map<String,Object>) salesList.get(i);
    %>
          <tr id="<%=sales.get("id") %>">
            <td><%=sales.get("number") %></td>
            <td><%=((Map)sales.get("inventory")).get("contactUnit") %></td>
            <td><%=sales.get("receiver") %></td>
            <td><%=((Map)sales.get("inventory")).get("materiel") %></td>
            <td><%=((Map)sales.get("inventory")).get("quantity") %></td>
            <td><%=((Map)sales.get("inventory")).get("totalPrice") %></td>
            <td><%=sales.get("comment") %></td>
            <td><%=sales.get("businessUserId") %></td>
            <td><%=sales.get("reviewUserId") %></td>
            <td>
            	<button onclick="editSales('<%=sales.get("id") %>')">编辑</button>
            	<button onclick="removeSales('<%=sales.get("id") %>')">删除</button>
            </td>
          </tr>
    <%
        } 
    %>
    	<tr>
    		<td colspan="9"><button onclick="addSales()">新销售单</button></td>
    	</tr>
      </tbody>
    </table>
  </div>
</div>


</div>


<div class="modal fade" id="salesFormModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog  modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">物料信息</h4>
      </div>
      <div class="modal-body">
	      <div class="form-group">
		    <div class="input-group">
				<div class="input-group-addon">销售单编号</div>
				<input class="form-control" id="number" type="text" placeholder="销售单编号">
				<div class="input-group-addon">对象</div>
				<select id="contactUnit" name="contactUnit" class="form-control">
					    <% for(Map<String, Object> cu : contactUnitList) { %>
					    	<option value='<%=cu.get("id") %>'><%=cu.get("code") + "|" + cu.get("name") %></option>
					    <% } %>
				</select>
				<div class="input-group-addon">收货人</div>
				<input class="form-control" id="receiver" type="text" placeholder="收货人">
				<div class="input-group-addon">日期</div>
				<input class="form-control" id="date" type="text" placeholder="日期">
			</div>
			<div class="pull-right" style="padding:3px;">
				
				销售人员:
					    <select id="businessUserId" name="businessUserId">
					    <% for(Map<String, Object> u : userList) { %>
					    	<option value='<%=u.get("id") %>'><%=u.get("name") %></option>
					    <% } %>
					    </select>
				审批人员:
					    <select id="reviewUserId" name="reviewUserId" >
					    <% for(Map<String, Object> u : userList) { %>
					    	<option value='<%=u.get("id") %>'><%=u.get("name") %></option>
					    <% } %>
					    </select>
			</div>
			
			<table class="table">
		      <thead><tr><th>物料</th><th>数量</th><th>单价</th><th>总价</th><th>备注</th><th></th></tr></thead>
		      <tbody>
		          <tr id="salesItemTemplate" style="display:none;">
		            <td>
					    <select id="materiel" name="materiel" class="form-control">
					    <% for(Map<String, Object> m : materielList) { %>
					    	<option value='<%=m.get("id") %>'><%=m.get("code") + "|" + m.get("name") %></option>
					    <% } %>
					    </select>
					</td>
		            <td><input type="text" id="quantity" class="form-control" placeholder="数量" /></td>
		            <td><input type="text" id="price" class="form-control" placeholder="单价" /></td>
		            <td><input type="text" id="totalPrice" class="form-control" placeholder="总价格" /></td>
		            <td><input type="text" id="comment" class="form-control" placeholder="备注" /></td>
					<td><button onclick="$(this).parent().parent().remove()">X</button></td>
		          </tr>
		          <tr><td colspan="8"><button onclick="addSalesItem()">添加</button></td></tr>
		      </tbody>
		    </table>
		  </div>
	  </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="editSalesConfirm()">确认</button>
      </div>
    </div>
  </div>
</div>

<style>
#salesFormModal select{
	padding:0px;
	width:160px;
}
input#quantity{
	padding:0px;
	width:60px;
}
input#totalPrice{
	padding:0px;
	width:100px;
}
input#comment{
	padding:0px;
	width:330px;
}
</style>

<script>
$("#date").datepicker({format:'yyyy-mm-dd', language:'zh-CN'});

var addSalesItem = function() {
	$("#salesItemTemplate").before($("#salesItemTemplate").clone().attr("id", "salesItem").attr("name", "salesItem").show());
};

var editSalesId = 0;

var addSales = function() {
	editSalesId = 0;
	$("#salesFormModal input:text").val("");
	$("#salesFormModal select option:eq(0)").attr('selected','selected');
	$("#salesFormModal").modal("show");
};
var editSales = function(id) {
	editSalesId = id;
	$("#editMaterielModal #code").val($("#"+id).find("td:eq(0)").text());
	$("#editMaterielModal #name").val($("#"+id).find("td:eq(1)").text());
	$("#editMaterielModal #specification").val($("#"+id).find("td:eq(2)").text());
	$("#editMaterielModal #unit").val($("#"+id).find("td:eq(3)").text());
	$("#editMaterielModal #tradePrice").val($("#"+id).find("td:eq(4)").text());
	$("#editMaterielModal #customerPrice").val($("#"+id).find("td:eq(5)").text());
	$("#editMaterielModal #type option[value='"+$("#"+id).find("td:eq(6)").text()+"']").attr('selected','selected');
	
	$("#editMaterielModal").modal("show");
};
var removeSales = function(id) {

};
var editSalesConfirm = function() {
	var parameters = {};
	
	parameters["id"] = editSalesId;
	parameters["number"] = $("#number").val();
	parameters["businessUserId"] = $("#businessUserId").val();
	parameters["reviewUserId"] = $("#reviewUserId").val();
	parameters["receiver"] = $("#receiver").val();
	parameters["contactUnit"] = $("#contactUnit option:selected").val();
	parameters["date"] = $("#date").val();
	
	var itemQuantity = 0;
	$("tr[name='salesItem']").each(function(index, obj){
		itemQuantity += 1;
		parameters["materiel"+index] = $(obj).find("#materiel option:selected").val();
		parameters["quantity"+index] = $(obj).find("#quantity").val();
		parameters["totalPrice"+index] = $(obj).find("#totalPrice").val();
		parameters["comment"+index] = $(obj).find("#comment").val();
	});
	parameters["itemQuantity"] = itemQuantity;
	
	$("div.modal-backdrop").remove();
	gotoPage("/xmod/accountant/salesSubmit", parameters);
};
</script>