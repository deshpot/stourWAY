<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<div id="contentPage">
	<ul class="media-list">
	  <li class="media">
	    <a class="pull-left" >
	      <button class="btn btn-primary" style="width: 64px; height: 64px;" onclick="gotoPage('/xmod/accountant/contactUnit')">往来<br>对象</button>
	    </a>
	    <div class="media-body">
	      <h4 class="media-heading">往来对象维护</h4>
	      包括客户与供应商
	    </div>
	  </li>
	  <li class="media">
	    <a class="pull-left" >
	      <button class="btn btn-primary" style="width: 64px; height: 64px;" onclick="gotoPage('/xmod/accountant/materiel')">物料<br>维护</button>
	    </a>
	    <div class="media-body">
	      <h4 class="media-heading">物料维护</h4>
	      物料包括产品、在产品、原材料、包装物、低值易耗品等
	    </div>
	  </li>
	  <li class="media">
	    <a class="pull-left" >
	      <button class="btn btn-primary" style="width: 64px; height: 64px;" onclick="gotoPage('/xmod/accountant/inventory')">库存<br>管理</button>
	    </a>
	    <div class="media-body">
	      <h4 class="media-heading">库存管理</h4>
	      包括采购记录、销售记录、库存转移、其他损耗（内部使用、外部招待、样品）等
	    </div>
	  </li>
	  <li class="media">
	    <a class="pull-left" >
	      <button class="btn btn-primary" style="width: 64px; height: 64px;" onclick="gotoPage('/xmod/accountant/purchase')">采购<br>费用</button>
	    </a>
	    <div class="media-body">
	      <h4 class="media-heading">采购费用</h4>
	      采购的入库
	    </div>
	  </li>
	  <li class="media">
	    <a class="pull-left" >
	      <button class="btn btn-primary" style="width: 64px; height: 64px;" onclick="gotoPage('/xmod/accountant/sales')">销售<br>收入</button>
	    </a>
	    <div class="media-body">
	      <h4 class="media-heading">销售收入</h4>
	      销售的出库
	    </div>
	  </li>
	  <li class="media">
	    <a class="pull-left" >
	      <button class="btn btn-primary" style="width: 64px; height: 64px;" onclick="gotoPage('/xmod/accountant/subjectManage')">会计<br>科目</button>
	    </a>
	    <div class="media-body">
	      <h4 class="media-heading">会计科目</h4>
	      会计的科目
	    </div>
	  </li>
	 <li class="media">
	    <a class="pull-left" >
	      <button class="btn btn-primary" style="width: 64px; height: 64px;" onclick="gotoPage('/xmod/accountant/voucher')">凭证<br>管理</button>
	    </a>
	    <div class="media-body">
	      <h4 class="media-heading">凭证管理</h4>
	      录入凭证，查看凭证等
	    </div>
	  </li>
	</ul>
</div>

