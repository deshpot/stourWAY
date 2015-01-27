<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<% List<Map<String,Object>> taobaoAccountList = (List<Map<String,Object>>)request.getAttribute("taobaoAccountList"); %>
<button onclick="addAccount()">新增账号</button>
<table class="table table-hover">
  <thead>
    <tr>
      <th>#</th><th>淘宝账号</th><th>淘宝密码</th><th>手机号</th><th style="display:none;">备注</th>
      <th>支付密码</th><th>邮箱</th><th style="display:none;">邮箱密码</th><th>姓名</th><th style="display:none;">身份证号码</th><th></th>
    </tr>
  </thead>
  <tbody>
    <% for(Map<String, Object> ta : taobaoAccountList) { %>
      <tr id="info_<%=ta.get("id") %>">
        <td><%=ta.get("type") %></td><td><%=ta.get("name") %></td><td><%=ta.get("password") %></td>
        <td><%=ta.get("phone") %></td><td style="display:none;"><%=ta.get("comment") %></td><td><%=ta.get("payPassword") %></td>
        <td><%=ta.get("mail") %></td><td style="display:none;"><%=ta.get("mailPassword") %></td><td><%=ta.get("realName") %></td>
        <td style="display:none;"><%=ta.get("realId") %></td><td><button onclick="editAccount(this)">编辑</button></td>
        <td><button onclick="removeAccount('<%=ta.get("id") %>')">删除</button></td>
      </tr>
    <% } %>
  </tbody>
</table>

<div class="modal fade" id="editAccountModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">淘宝账号</h4>
      </div>
      <div class="modal-body">
        <form id="accountForm" action="/xmod/taobao/editAccount" method="post">
          <input type="hidden" name="taobaoAccount[id]" value="0" />
          <div class="input-group input-group-sm">
					  <span class="input-group-addon">类型</span>
					  <select name="taobaoAccount[type]" class="form-control"><option value="buy">买家</option><option value="sale">卖家</option></select>
					  <span class="input-group-addon">淘宝账号</span>
            <input type="text" name="taobaoAccount[name]" class="form-control"/>
            <span class="input-group-addon">淘宝密码</span>
            <input type="text" name="taobaoAccount[password]" class="form-control"/>
					</div>
					<br>
					<div class="input-group input-group-sm">
            <span class="input-group-addon" style="width:80px">手机号</span>
            <input type="text" name="taobaoAccount[phone]" style="width:204px" class="form-control"/>
            <span class="input-group-addon" style="width:80px">支付密码</span>
            <input type="text" name="taobaoAccount[payPassword]" style="width:204px" class="form-control"/>
          </div>
          <br>
          <div class="input-group input-group-sm">
            <span class="input-group-addon" style="width:80px">邮箱</span>
            <input type="text" name="taobaoAccount[mail]" style="width:204px" class="form-control"/>
            <span class="input-group-addon" style="width:80px">邮箱密码</span>
            <input type="text" name="taobaoAccount[mailPassword]" style="width:204px" class="form-control"/>
          </div>
          <br>
          <div class="input-group input-group-sm">
            <span class="input-group-addon" style="width:80px">身份证名字</span>
            <input type="text" name="taobaoAccount[realName]" style="width:204px" class="form-control"/>
            <span class="input-group-addon" style="width:80px">身份证号码</span>
            <input type="text" name="taobaoAccount[realId]" style="width:204px" class="form-control"/>
          </div>
          <br>
          <div class="form-group">
            <textarea id="comment" name="taobaoAccount[comment]" class="form-control" rows="3" placeholder="备注信息"></textarea>
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
var editAccountId = 0;

var editAccount = function(o) {
  var trInfo = $(o).parent().parent();
  editAccountId = trInfo.attr("id").split("_")[1];
  
  $("#accountForm").find("input[name='taobaoAccount[id]']").val(editAccountId);
  $("#accountForm").find("select[name='taobaoAccount[type]']").val(trInfo.find("td:eq(0)").text());
  $("#accountForm").find("input[name='taobaoAccount[name]']").val(trInfo.find("td:eq(1)").text());
  $("#accountForm").find("input[name='taobaoAccount[password]']").val(trInfo.find("td:eq(2)").text());
  $("#accountForm").find("input[name='taobaoAccount[phone]']").val(trInfo.find("td:eq(3)").text());
  $("#accountForm").find("input[name='taobaoAccount[payPassword]']").val(trInfo.find("td:eq(5)").text());
  $("#accountForm").find("input[name='taobaoAccount[mail]']").val(trInfo.find("td:eq(6)").text());
  $("#accountForm").find("input[name='taobaoAccount[mailPassword]']").val(trInfo.find("td:eq(7)").text());
  $("#accountForm").find("input[name='taobaoAccount[realName]']").val(trInfo.find("td:eq(8)").text());
  $("#accountForm").find("input[name='taobaoAccount[realId]']").val(trInfo.find("td:eq(9)").text());
  $("#accountForm").find("textarea[name='taobaoAccount[comment]']").val(trInfo.find("td:eq(4)").text());
  $("#editAccountModal").modal("show");
}

var addAccount = function() {
  editAccountId = 0;
  $("#accountForm").find("input[name='taobaoAccount[id]']").val(editAccountId);
  $("#editAccountModal input:text").val("");
  $("#comment").val("");
  $("#editAccountModal").modal("show");
};
var removeAccount = function(id) {
  if(confirm("确定要删除吗?")) { 
    gotoPage("/xmod/taobao/removeAccount?id=" + id); 
  }
};
var editContactUnitConfirm = function() {
  $("div.modal-backdrop").remove();
  submitForm($("#accountForm"));
};
</script>