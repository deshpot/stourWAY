<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<% List<Map<String,String>> voucherList = (List<Map<String,String>>)request.getAttribute("voucherList"); %>
<% List<Map<String,String>> subjectList = (List<Map<String,String>>)request.getAttribute("subjectList"); %>
<div id="contentPage">

    <table class="table" style="font-size:12px;" id="voucherList">
      <thead><tr><th style="width:80px;">日期</th><th>字号</th><th>摘要</th><th>科目</th><th>借</th><th>贷</th></tr></thead>
      <tbody>
    <% 
    	String flag = "";
    	int c = 1;
        for(int i = 0; i < voucherList.size(); i++) {
        	Map<String,String> voucher = (Map<String,String>) voucherList.get(i);
        	if (!flag.equals(voucher.get("date") + voucher.get("codeName") +voucher.get("codeNumber"))) {
        		c = c * -1;
        		flag = voucher.get("date") + voucher.get("codeName") +voucher.get("codeNumber");
        	}
        		
    %>
          <tr id="<%=voucher.get("id") %>" class="<%=c == 1 ? "info":""  %>" onclick="editVoucher('<%=voucher.get("date") %>','<%=voucher.get("codeName") %>','<%=voucher.get("codeNumber") %>')">
            <td><%=voucher.get("date") %></td>
            <td><%=voucher.get("codeName") %>-<%=voucher.get("codeNumber") %></td>
            <td><%=voucher.get("comment") %></td>
            <td><%=voucher.get("subject") %></td>
            <td><%=voucher.get("debit") %></td>
            <td><%=voucher.get("credit") %></td>
          </tr>
    <%
        } 
    %>
    	<tr>
    		<td colspan="9"><button onclick="addVoucher()">录入凭证</button></td>
    	</tr>
      </tbody>
    </table>


</div>


<style>
div.voucherHeader input#date{
	width:119px;
}
div.voucherHeader select#codeName,input#codeNumber,input#invoices{
	width:60px;
}
</style>

<script>
$("#date").datepicker({format:'yyyy-mm-dd', language:'zh-CN'});

var editVoucher = function(date, codeName, codeNumber) {

};

var editVoucherConfirm = function() {
	if($("#date").val() == ""){
		alert("请设置日期");
		return;
	}
	var balance = 0;
	$("tr[name='voucherItem']").each(function(index, obj){
			balance = balance + parseFloat($(obj).find("#debit").val());
			balance = balance - parseFloat($(obj).find("#credit").val());
	});
	if( balance != 0) {
		alert("借贷不平衡");
		return;
	}
	
	var parameters = {
			"id" : editVoucherId,
			"codeName" : $("#codeName").val(),
			"codeNumber" : $("#codeNumber").val(),
			"date" : $("#date").val(),
			"invoices" : $("#invoices").val(),
	};
	
	var itemQuantity = 0;
	$("tr[name='voucherItem']").each(function(index, obj){
		itemQuantity += 1;
		parameters["id"+index] = $(obj).attr("id");
		parameters["subject"+index] = $(obj).find("#subject option:selected").val();
		parameters["comment"+index] = $(obj).find("#comment").val();
		parameters["debit"+index] = $(obj).find("#debit").val();
		parameters["credit"+index] = $(obj).find("#credit").val();
	});
	parameters["itemQuantity"] = itemQuantity;
	
	$("div.modal-backdrop").remove();
	gotoPage("/xmod/accountant/voucherSubmit", parameters);
};
</script>