<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<% List<Map<String,Object>> contactUnitList = (List<Map<String,Object>>)request.getAttribute("contactUnitList"); %>
<% List<Map<String,Object>> materielList = (List<Map<String,Object>>)request.getAttribute("materielList"); %>
<% List<Map<String,Object>> purchaseList = (List<Map<String,Object>>)request.getAttribute("purchaseList"); %>
<% List<Map<String,Object>> userList = (List<Map<String,Object>>)request.getAttribute("userList"); %>
<div id="contentPage">
<div id="azdProjectList" class="panel panel-default">
  <div class="panel-heading">采购单记录</div>
  <div class="panel-body">
    <table class="table">
      <thead><tr><th>编号</th><th>往来对象</th><th>物料</th><th>数量</th><th>总费用</th><th>备注</th><th>业务人员</th><th>审批人员</th><th></th></tr></thead>
      <tbody>
    <% 
        for(int i = 0; i < purchaseList.size(); i++) {
        	Map<String,Object> purchase = (Map<String,Object>) purchaseList.get(i);
    %>
          <tr id="<%=purchase.get("id") %>">
            <td><%=purchase.get("number") %></td>
            <td><%=((Map)purchase.get("inventory")).get("contactUnit") %></td>
            <td><%=((Map)purchase.get("inventory")).get("materiel") %></td>
            <td><%=((Map)purchase.get("inventory")).get("quantity") %></td>
            <td><%=((Map)purchase.get("inventory")).get("totalCost") %></td>
            <td><%=purchase.get("comment") %></td>
            <td><%=purchase.get("businessUserId") %></td>
            <td><%=purchase.get("reviewUserId") %></td>
            <td>
            	<button onclick="editPurchase('<%=purchase.get("id") %>')">编辑</button>
            	<button onclick="removePurchase('<%=purchase.get("id") %>')">删除</button>
            </td>
          </tr>
    <%
        } 
    %>
    	<tr>
    		<td colspan="9"><button onclick="addPurchase()">新采购单</button></td>
    	</tr>
      </tbody>
    </table>
  </div>
</div>


</div>


<div class="modal fade" id="purchaseFormModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog  modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">物料信息</h4>
      </div>
      <div class="modal-body">
	      <div class="form-group">
		    <div class="input-group">
				<div class="input-group-addon">采购单编号</div>
				<input class="form-control" id="number" type="text" placeholder="采购单编号">
				
				<div class="input-group-addon">日期</div>
				<input class="form-control" id="date" type="text" placeholder="日期">
			</div>
			<div class="pull-right" style="padding:3px;">
				业务人员:
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
		      <thead><tr><th>往来对象</th><th>物料</th><th>数量</th><th>总费用</th><th>备注</th><th></th></tr></thead>
		      <tbody>
		          <tr id="purchaseItemTemplate" style="display:none;">
		            <td>
						<select id="contactUnit" name="contactUnit" class="form-control">
					    <% for(Map<String, Object> cu : contactUnitList) { %>
					    	<option value='<%=cu.get("id") %>'><%=cu.get("code") + "|" + cu.get("name") %></option>
					    <% } %>
					    </select>
					</td>
		            <td>
					    <select id="materiel" name="materiel" class="form-control">
					    <% for(Map<String, Object> m : materielList) { %>
					    	<option value='<%=m.get("id") %>'><%=m.get("code") + "|" + m.get("name") %></option>
					    <% } %>
					    </select>
					</td>
		            <td><input type="text" id="quantity" class="form-control" placeholder="数量" /></td>
		            <td><input type="text" id="totalCost" class="form-control" placeholder="总费用" /></td>
		            <td><input type="text" id="comment" class="form-control" placeholder="备注" /></td>
					<td><button onclick="$(this).parent().parent().remove()">X</button></td>
		          </tr>
		          <tr><td colspan="8"><button onclick="addPurchaseItem()">添加</button></td></tr>
		      </tbody>
		    </table>
		  </div>
	  </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="editPurchaseConfirm()">确认</button>
      </div>
    </div>
  </div>
</div>

<style>
#purchaseFormModal select{
	padding:0px;
	width:120px;
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

var addPurchaseItem = function() {
	$("#purchaseItemTemplate").before($("#purchaseItemTemplate").clone().attr("id", "purchaseItem").attr("name", "purchaseItem").show());
};

var editPurchaseId = 0;

var addPurchase = function() {
	editPurchaseId = 0;
	$("#purchaseFormModal input:text").val("");
	$("#purchaseFormModal select option:eq(0)").attr('selected','selected');
	$("#purchaseFormModal").modal("show");
};
var editPurchase = function(id) {
	editPurchaseId = id;
	$("#editMaterielModal #code").val($("#"+id).find("td:eq(0)").text());
	$("#editMaterielModal #name").val($("#"+id).find("td:eq(1)").text());
	$("#editMaterielModal #specification").val($("#"+id).find("td:eq(2)").text());
	$("#editMaterielModal #unit").val($("#"+id).find("td:eq(3)").text());
	$("#editMaterielModal #tradePrice").val($("#"+id).find("td:eq(4)").text());
	$("#editMaterielModal #customerPrice").val($("#"+id).find("td:eq(5)").text());
	$("#editMaterielModal #type option[value='"+$("#"+id).find("td:eq(6)").text()+"']").attr('selected','selected');
	
	$("#editMaterielModal").modal("show");
};
var removePurchase = function(id) {

};
var editPurchaseConfirm = function() {
	var parameters = {};
	
	parameters["id"] = editPurchaseId;
	parameters["number"] = $("#number").val();
	parameters["businessUserId"] = $("#businessUserId").val();
	parameters["reviewUserId"] = $("#reviewUserId").val();
	parameters["contactUnit"] = $("#contactUnit option:selected").val();
	parameters["date"] = $("#date").val();
	
	var itemQuantity = 0;
	$("tr[name='purchaseItem']").each(function(index, obj){
		itemQuantity += 1;
		parameters["materiel"+index] = $(obj).find("#materiel option:selected").val();
		parameters["quantity"+index] = $(obj).find("#quantity").val();
		parameters["totalCost"+index] = $(obj).find("#totalCost").val();
		parameters["comment"+index] = $(obj).find("#comment").val();
	});
	parameters["itemQuantity"] = itemQuantity;
	
	$("div.modal-backdrop").remove();
	gotoPage("/xmod/accountant/purchaseSubmit", parameters);
};
</script>