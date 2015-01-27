package com.qzsitu.stourway.xmod.taobao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qzsitu.stourway.dao.GenericDao;
import com.qzsitu.stourway.domain.CustomizationXMod;
import com.qzsitu.stourway.domain.CustomizationXModFunction;
import com.qzsitu.stourway.domain.User;
import com.qzsitu.stourway.util.BeanUtil;
import com.qzsitu.stourway.xmod.accountant.ContactUnit;
import com.qzsitu.stourway.xmod.system.XModResponse;
import com.qzsitu.stourway.xmod.system.XModService;

@Service
public class TaobaoService extends XModService  {
	@Autowired
	private GenericDao<TaobaoAccount> taobaoAccountDao;
	@Autowired
	private GenericDao<FakeBuyTask> fakeBuyTaskDao;
	
	@Transactional
	public XModResponse taobao(HttpServletRequest request, User user) {
		XModResponse rs = new XModResponse();
		rs.setPageReturn("taobao");
		return rs;
	}
	@Transactional
	public XModResponse accountManage(HttpServletRequest request, User user) {
		List<TaobaoAccount> taList = taobaoAccountDao.queryAll(TaobaoAccount.class);
		List<Map<String,Object>> taobaoAccountList = new BeanUtil<ContactUnit>().beanList2MapList(taList, new Object(){
//			public String getIsCustomer(Boolean isCustomer) {return isCustomer!=null?(isCustomer?" 客户  ":""):"";}
//			public String getIsSupplier(Boolean isSupplier) {return isSupplier!=null?(isSupplier?" 供应商  ":""):"";}
		});
		
		XModResponse rs = new XModResponse();
		rs.put("taobaoAccountList", taobaoAccountList);
		rs.setPageReturn("accountManage");
		return rs;
	}
	@Transactional
	public XModResponse editAccount(HttpServletRequest request, User user) {
		List<TaobaoAccount> taList = new BeanUtil<TaobaoAccount>().request2Bean(request, new TaobaoAccount(), new Object(){
			Object setDate(Date date) { return new Date();}
		}, taobaoAccountDao);
		for(TaobaoAccount ta : taList)
			taobaoAccountDao.create(ta);
		
		return accountManage(request, user);
	}
	@Transactional
	public XModResponse removeAccount(HttpServletRequest request, User user) {
		TaobaoAccount ta = new TaobaoAccount();
		ta.setId(request.getParameter("id"));
		taobaoAccountDao.delete(ta);
		
		return accountManage(request, user);
	}
	@Transactional
	public XModResponse fakeBuyTask(HttpServletRequest request, User user) {
		
		List<FakeBuyTask> fbList = fakeBuyTaskDao.queryAll(FakeBuyTask.class);
		List<Map<String,Object>> fakeBuyTaskList = new BeanUtil<ContactUnit>().beanList2MapList(fbList, new Object(){
//			public String getIsCustomer(Boolean isCustomer) {return isCustomer!=null?(isCustomer?" 客户  ":""):"";}
//			public String getIsSupplier(Boolean isSupplier) {return isSupplier!=null?(isSupplier?" 供应商  ":""):"";}
		});
		XModResponse rs = new XModResponse();
		rs.put("fakeBuyTaskList", fakeBuyTaskList);
		rs.put("assignee", "duhongke");
		rs.setPageReturn("fakeBuyTask");
		return rs;
	}
	@Transactional
	public XModResponse newFakeBuy(HttpServletRequest request, User user) {
		List<FakeBuyTask> fbList = new BeanUtil<FakeBuyTask>().request2Bean(request, new FakeBuyTask(), new Object(){

		}, fakeBuyTaskDao);
		for(FakeBuyTask fb : fbList) {
			fakeBuyTaskDao.create(fb);
			
		}
		return fakeBuyTask(request, user);
	}	
	@Transactional
	public XModResponse doFakeBuy(HttpServletRequest request, User user) {
		List<FakeBuyTask> fbList = new BeanUtil<FakeBuyTask>().request2Bean(request, new FakeBuyTask(), new Object(){

		}, fakeBuyTaskDao);
		for(FakeBuyTask fb : fbList)
			fakeBuyTaskDao.update(fb);
		return fakeBuyTask(request, user);
	}	
	
	
	@Override
	public CustomizationXMod getXMod() {
		CustomizationXMod xmod = new CustomizationXMod();
		xmod.setId("taobao");
		xmod.setModName("淘宝业务");
		xmod.setModDescription("");
		xmod.setEnabled(false);
		
		List<CustomizationXModFunction> functionList = new ArrayList<CustomizationXModFunction>();
		CustomizationXModFunction function = new CustomizationXModFunction();
		function.setFunctionName("进入淘宝业务模块");
		function.setFunctionDescription("");
		function.setPermissionName("taobao");
		functionList.add(function);
		function = new CustomizationXModFunction();
		function.setFunctionName("账号管理");
		function.setFunctionDescription("");
		function.setPermissionName("accountManage");
		functionList.add(function);
		function = new CustomizationXModFunction();
		function.setFunctionName("刷单任务");
		function.setFunctionDescription("");
		function.setPermissionName("fakeBuyTask");
		functionList.add(function);
		xmod.setFunctionList(functionList);
		
		return xmod;
	}

}
