<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<% List<Map<String,String>> materielList = (List<Map<String,String>>)request.getAttribute("materielList"); %>
<% List<Map<String,String>> inventoryList = (List<Map<String,String>>)request.getAttribute("inventoryList"); %>
<div id="contentPage">

<div id="azdProjectList" class="panel panel-default">
  <div class="panel-heading">库存情况</div>
  <div class="panel-body">
    <table class="table">
      <thead><tr><th>编码</th><th>名字</th><th>库存数量</th><th>库存成本</th><th>单位成本</th></tr></thead>
      <tbody>
    <% 
        for(int i = 0; i < materielList.size(); i++) {
        	Map<String,String> materiel = (Map<String,String>) materielList.get(i);
    %>
          <tr id="<%=materiel.get("id") %>">
            <td><%=materiel.get("code") %></td>
            <td><%=materiel.get("name") %></td>
            <td><%=materiel.get("quantity") %></td>
            <td><%=materiel.get("totalCost") %></td>
            <td><%=materiel.get("cost") %></td>
          </tr>
    <%
        } 
    %>
      </tbody>
    </table>
  </div>
</div>

<div id="azdProjectList" class="panel panel-default">
  <div class="panel-heading">操作记录</div>
  <div class="panel-body">
    <table class="table">
      <thead><tr><th>操作</th><th>往来对象</th><th>物料</th><th>数量</th><th>总价格</th><th>总成本</th><th>仓库</th><th>日期</th></tr></thead>
      <tbody>
    <% 
        for(int i = 0; i < inventoryList.size(); i++) {
        	Map<String,String> inventory = (Map<String,String>) inventoryList.get(i);
    %>
          <tr id="<%=inventory.get("id") %>">
            <td><%=inventory.get("operation") %></td>
            <td><%=inventory.get("contactUnit") %></td>
            <td><%=inventory.get("materiel") %></td>
            <td><%=inventory.get("quantity") %></td>
            <td><%=inventory.get("totalPrice") %></td>
            <td><%=inventory.get("totalCost") %></td>
            <td><%=inventory.get("warehouse") %></td>
            <td><%=inventory.get("date") %></td>
          </tr>
    <%
        } 
    %>
      </tbody>
    </table>
  </div>
</div>

</div>
