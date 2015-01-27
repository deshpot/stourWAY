<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<% List<Map<String,String>> materielList = (List<Map<String,String>>)request.getAttribute("materielList"); %>
<div id="contentPage">
<div id="azdProjectList" class="panel panel-default">
  <div class="panel-heading">往来对象</div>
  <div class="panel-body">
    <table class="table">
      <thead><tr><th>编码</th><th>名字</th><th>规格</th><th>单位</th><th>参考批发价</th><th>参考零售价</th><th></th></tr></thead>
      <tbody>
    <% 
        for(int i = 0; i < materielList.size(); i++) {
        	Map<String,String> materiel = (Map<String,String>) materielList.get(i);
    %>
          <tr id="<%=materiel.get("id") %>">
            <td><%=materiel.get("code") %></td>
            <td><%=materiel.get("name") %></td>
            <td><%=materiel.get("specification") %></td>
            <td><%=materiel.get("unit") %></td>
            <td><%=materiel.get("tradePrice") %></td>
            <td><%=materiel.get("customerPrice") %></td>
            <td style="display:none;"><%=materiel.get("type") %></td>
            <td>
            	<button onclick="editMateriel('<%=materiel.get("id") %>')">编辑</button>
            	<button onclick="removeMateriel('<%=materiel.get("id") %>')">删除</button>
            </td>
          </tr>
    <%
        } 
    %>
    	<tr>
    		<td colspan="7"><button onclick="addMateriel()">添加物料</button></td>
    	</tr>
      </tbody>
    </table>
  </div>
</div>


</div>


<div class="modal fade" id="editMaterielModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">物料信息</h4>
      </div>
      <div class="modal-body">
	      <div class="form-group">
		    <label for="code">编码：</label>
		    <input type="text" id="code" class="form-control" placeholder="编码" />
		    <label for="name">名称：</label>
		    <input type="text" id="name" class="form-control" placeholder="名称" />
		    <label for="specification">规格：</label>
		    <input type="text" id="specification" class="form-control" placeholder="规格" />
		    <label for="unit">单位：</label>
		    <input type="text" id="unit" class="form-control" placeholder="单位" />
		    <label for="tradePrice">批发价：</label>
		    <input type="text" id="tradePrice" class="form-control" placeholder="批发价" />
		    <label for="customerPrice">零售价：</label>
		    <input type="text" id="customerPrice" class="form-control" placeholder="零售价" />
		    <label for="type">类型：</label>
		    <select id="type" name="type">
		    	<option value='product'>产品</option>
		    	<option value='package'>包装物</option>
		    	<option value='consumables'>低值易耗品</option>
		    </select>
		  </div>
	  </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="editMaterielConfirm()">确认</button>
      </div>
    </div>
  </div>
</div>

<script>
var editMaterielId = 0;

var addMateriel = function() {
	editMaterielId = 0;
	$("#editMaterielModal input:text").val("");
	$("#editMaterielModal select option:eq(0)").attr('selected','selected');
	$("#editMaterielModal").modal("show");
};
var editMateriel = function(id) {
	editMaterielId = id;
	$("#editMaterielModal #code").val($("#"+id).find("td:eq(0)").text());
	$("#editMaterielModal #name").val($("#"+id).find("td:eq(1)").text());
	$("#editMaterielModal #specification").val($("#"+id).find("td:eq(2)").text());
	$("#editMaterielModal #unit").val($("#"+id).find("td:eq(3)").text());
	$("#editMaterielModal #tradePrice").val($("#"+id).find("td:eq(4)").text());
	$("#editMaterielModal #customerPrice").val($("#"+id).find("td:eq(5)").text());
	$("#editMaterielModal #type option[value='"+$("#"+id).find("td:eq(6)").text()+"']").attr('selected','selected');
	
	$("#editMaterielModal").modal("show");
};
var removeMateriel = function(id) {
	var parameters = {};
	parameters["id"] = id;
	if(confirm("确定要删除这个物料吗?")) { 
		gotoPage("/xmod/accountant/removeMateriel", parameters); 
	}
};
var editMaterielConfirm = function() {
	var parameters = {};
	
	parameters["id"] = editMaterielId;
	parameters["code"] = $("#code").val();
	parameters["name"] = $("#name").val();
	parameters["specification"] = $("#specification").val();
	parameters["unit"] = $("#unit").val();
	parameters["costPrice"] = $("#costPrice").val();
	parameters["tradePrice"] = $("#tradePrice").val();
	parameters["customerPrice"] = $("#customerPrice").val();
	parameters["name"] = $("#name").val();
	parameters["type"] = $("#type option:selected").val();
	
	$("div.modal-backdrop").remove();
	gotoPage("/xmod/accountant/editMateriel", parameters);
};
</script>