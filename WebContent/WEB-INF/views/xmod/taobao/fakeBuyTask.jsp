<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<% String assignee = (String) request.getAttribute("assignee"); %>
<% List<Map<String,Object>> fakeBuyTaskList = (List<Map<String,Object>>)request.getAttribute("fakeBuyTaskList"); %>
<button onclick="addFakeBuyTask()">创建任务</button>
<button onclick="delFakeBuyTask()">撤销任务</button>
<table class="table table-condensed">
  <tbody>
  <% for(Map<String, Object> fb : fakeBuyTaskList) { %>
    <tr id="fackBuy_<%=fb.get("id") %>" onclick="doFakeBuy('<%=fb.get("id") %>')">
      <td rowspan="3" style="width:50px;">
        <div class="alert alert-warning" role="alert" style="padding: 10px;margin-bottom: 0px;height: 77px;width: 36px;">任务单</div>
      </td>
      <td class="td_key">任务</td><td class="td_value"><%=fb.get("fakeBuyType") %></td>
      <td class="td_key">卖家</td><td class="td_value"><%=fb.get("sale") %></td>
      <td class="td_key">产品</td><td class="td_value"><%=fb.get("product") %></td>
      <td class="td_key">搜法</td><td class="td_value"><%=fb.get("searchType") %></td>
      <td class="td_key">关键词</td><td class="td_value"><%=fb.get("keyword") %></td>
      <td class="td_key">日期</td><td class="td_value"><%=fb.get("date") %></td>
      <td class="td_key">页码</td><td class="td_value"><%=fb.get("position") == null ? "":fb.get("position") %></td>
    </tr>
    <tr onclick="doFakeBuy('<%=fb.get("id") %>')">
      <td class="td_key">买家</td><td class="td_value"><%=fb.get("buy") == null ? "":fb.get("buy") %></td>
      <td class="td_key">返现</td><td class="td_value"><%=fb.get("payBack") == null ? "":fb.get("payBack") %></td>
      <td class="td_key">佣金</td><td class="td_value"><%=fb.get("commission") == null ? "":fb.get("commission") %></td>
      <td class="td_key">空包费</td><td class="td_value"><%=fb.get("expressFee") == null ? "":fb.get("expressFee") %></td>
      <td class="td_key">QQ</td><td class="td_value"><%=fb.get("qq") == null ? "":fb.get("qq") %></td>
      <td class="td_key">收款账号</td><td class="td_value" colspan="3"><%=fb.get("payAccount") == null ? "":fb.get("payAccount") %></td>
    </tr>
    <tr onclick="doFakeBuy('<%=fb.get("id") %>')">
      <td class="td_key">备注</td><td class="td_value" colspan="13"><%=fb.get("comment") %></td>
    </tr>
  <% } %>
  </tbody>
</table>


<div class="modal fade" id="newFakeBuyModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">创建任务</h4>
      </div>
      <div class="modal-body">
        <form id="newFakeBuyForm" action="/xmod/taobao/newFakeBuy" method="post">
          <input type="hidden" name="fakeBuyTask[id]" value="0" />
          <input type="hidden" name="fakeBuyTask[status]" value="new" />
          <input type="hidden" name="fakeBuyTask[assignee]" value="<%=assignee %>" />
          <div class="input-group input-group-sm">
            <span class="input-group-addon">任务</span>
            <select class="form-control" name="fakeBuyTask[fakeBuyType]">
						  <option value="放单">放单</option>
              <option value="接单">接单</option>
						  <option value="互刷">互刷</option>
						</select>
          </div>
          <div class="input-group input-group-sm">
            <span class="input-group-addon">卖家</span>
            <select class="form-control" name="fakeBuyTask[sale]">
              <option value="黛绮斯旗舰店">黛绮斯旗舰店</option>
              <option value="deshpot">deshpot</option>
              <option value="shushi236">shushi236</option>
              <option value="du891229">du891229</option>
              <option value="丝途小铺">丝途小铺</option>
              <option value="其他">其他</option>
            </select>
          </div>
          <div class="input-group input-group-sm">
            <span class="input-group-addon">产品</span>
            <select class="form-control" name="fakeBuyTask[product]">
              <option value="磨毛四件套">磨毛四件套</option>
              <option value="路特拉索干红葡萄酒VDP">路特拉索干红葡萄酒VDP</option>
              <option value="其他">其他</option>
            </select>
          </div>
          <div class="input-group input-group-sm">
            <span class="input-group-addon">搜法</span>
            <select class="form-control" name="fakeBuyTask[searchType]">
              <option value="淘宝搜索">淘宝搜索</option>
              <option value="直通车">直通车</option>
              <option value="天猫搜索">天猫搜索</option>
              <option value="淘宝转天猫">淘宝转天猫</option>
              <option value="其他">其他</option>
            </select>
          </div>
          <div class="input-group input-group-sm">
            <span class="input-group-addon">关键词</span>
            <select class="form-control" name="fakeBuyTask[keyword]">
              <option value="磨毛四件套">磨毛四件套</option>
              <option value="床上用品套件">床上用品套件</option>
              <option value="法国红酒">法国红酒</option>
              <option value="红酒">红酒</option>
              <option value="进口红酒">进口红酒</option>
              <option value="其他">其他</option>
            </select>
          </div>
          <div class="form-group">
            <textarea id="comment" name="fakeBuyTask[comment]" class="form-control" rows="3" placeholder="备注信息"></textarea>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="newFakeBuyConfirm()">确认</button>
      </div>
    </div>
  </div>
</div>

<div class="modal fade" id="doFakeBuyModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">执行任务</h4>
      </div>
      <div class="modal-body">
        <form id="doFakeBuyForm" action="/xmod/taobao/doFakeBuy" method="post">
          <input type="hidden" name="fakeBuyTask[id]" value="0" />
          <div class="input-group input-group-sm">
            <span class="input-group-addon">页码</span>
            <input type="text" class="form-control" name="fakeBuyTask[position]">
          </div>
          <div class="input-group input-group-sm">
            <span class="input-group-addon">买家</span>
            <input type="text" class="form-control" name="fakeBuyTask[buy]">
          </div>
          <div class="input-group input-group-sm">
            <span class="input-group-addon">返现</span>
            <input type="text" class="form-control" name="fakeBuyTask[payBack]">
          </div>
          <div class="input-group input-group-sm">
            <span class="input-group-addon">佣金</span>
            <input type="text" class="form-control" name="fakeBuyTask[commission]">
          </div>
          <div class="input-group input-group-sm">
            <span class="input-group-addon">空包费</span>
            <input type="text" class="form-control" name="fakeBuyTask[expressFee]">
          </div>
          <div class="input-group input-group-sm">
            <span class="input-group-addon">QQ</span>
            <input type="text" class="form-control" name="fakeBuyTask[qq]">
          </div>
          <div class="input-group input-group-sm">
            <span class="input-group-addon">收款账号</span>
            <input type="text" class="form-control" name="fakeBuyTask[payAccount]">
          </div>
          <div class="form-group">
            <textarea id="comment" name="fakeBuyTask[comment]" class="form-control" rows="3" placeholder="备注信息"></textarea>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="doFakeBuyConfirm()">确认</button>
      </div>
    </div>
  </div>
</div>
<script>
var addFakeBuyTask = function() {
  $("#newFakeBuyModal").modal("show");
};
var newFakeBuyConfirm = function() {
  $("div.modal-backdrop").remove();
  submitForm($("#newFakeBuyForm"));
};
var doFakeBuy = function(id) {
  $("#doFakeBuyForm input[name='fakeBuyTask[id]']").val(id);
  var tr1 = $("#fackBuy_"+id);
  var tr2 = $("#fackBuy_"+id).next("tr");
  var tr3 = $("#fackBuy_"+id).next("tr").next("tr");
  $("#doFakeBuyForm input[name='fakeBuyTask[position]']").val(tr1.find("td:eq(14)").text());
  $("#doFakeBuyForm input[name='fakeBuyTask[buy]").val(tr2.find("td:eq(1)").text());
  $("#doFakeBuyForm input[name='fakeBuyTask[payBack]']").val(tr2.find("td:eq(3)").text());
  $("#doFakeBuyForm input[name='fakeBuyTask[commission]']").val(tr2.find("td:eq(5)").text());
  $("#doFakeBuyForm input[name='fakeBuyTask[expressFee]").val(tr2.find("td:eq(7)").text());
  $("#doFakeBuyForm input[name='fakeBuyTask[qq]']").val(tr2.find("td:eq(9)").text());
  $("#doFakeBuyForm input[name='fakeBuyTask[payAccount]']").val(tr2.find("td:eq(11)").text());
  $("#doFakeBuyForm textarea[name='fakeBuyTask[comment]").text(tr3.find("td:eq(1)").text());
  $("#doFakeBuyModal").modal("show");
};
var doFakeBuyConfirm = function() {
  $("div.modal-backdrop").remove();
  submitForm($("#doFakeBuyForm"));
};
</script>