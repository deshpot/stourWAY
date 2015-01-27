package com.qzsitu.stourway.xmod.accountant;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.qzsitu.stourway.exception.StourWAYException;
import com.qzsitu.stourway.service.AccountService;
import com.qzsitu.stourway.util.BeanUtil;
import com.qzsitu.stourway.xmod.system.XModResponse;
import com.qzsitu.stourway.xmod.system.XModService;

@Service
public class AccountantService extends XModService {
	@Autowired
	public GenericDao<ContactUnit> contactUnitDao;
	@Autowired
	public GenericDao<Materiel> materielDao;
	@Autowired
	public GenericDao<Inventory> inventoryDao;
	@Autowired
	public GenericDao<Purchase> purchaseDao;
	@Autowired
	public GenericDao<Sales> salesDao;
	@Autowired
	public AccountService accountService;
	@Autowired
	public GenericDao<Subject> subjectDao;
	@Autowired
	public GenericDao<Voucher> voucherDao;
	
	@Transactional
	public XModResponse subjectEdit(HttpServletRequest request, User user) {
		XModResponse rs = new XModResponse();
		
		Subject sub;
		if("0".equals(request.getParameter("id")))
			sub = new Subject();
		else
			sub = subjectDao.read(Subject.class, request.getParameter("id"));
		
		sub.setCode(request.getParameter("code"));
		sub.setName(request.getParameter("name"));
		sub.setParentId(request.getParameter("parentId"));
		subjectDao.create(sub);
		
		rs.setAjaxReturn(sub.getId());
		return rs;
	}
	
	public XModResponse accountant(HttpServletRequest request, User user) {
		XModResponse rs = new XModResponse();
		rs.setPageReturn("accountant");
		return rs;
	}

	@Transactional
	public XModResponse contactUnit(HttpServletRequest request, User user) {
		List<ContactUnit> cuList = contactUnitDao.queryAll(ContactUnit.class);
		List<Map<String,Object>> contactUnitList = new BeanUtil<ContactUnit>().beanList2MapList(cuList, new Object(){
			public String getIsCustomer(Boolean isCustomer) {return isCustomer!=null?(isCustomer?" 客户  ":""):"";}
			public String getIsSupplier(Boolean isSupplier) {return isSupplier!=null?(isSupplier?" 供应商  ":""):"";}
		});
				
		XModResponse rs = new XModResponse();
		rs.put("contactUnitList", contactUnitList);
		rs.setPageReturn("contactUnit");
		return rs;
	}
	@Transactional
	public XModResponse editContactUnit(HttpServletRequest request, User user) {
		List<ContactUnit> cuList = new BeanUtil<ContactUnit>().request2Bean(request, new ContactUnit(), new Object(){
			
		}, contactUnitDao);
		for(ContactUnit cu : cuList)
			contactUnitDao.create(cu);
		
		return contactUnit(request, user);
	}
	@Transactional
	public XModResponse removeContactUnit(HttpServletRequest request, User user) {
		ContactUnit cu = new ContactUnit();
		cu.setId(request.getParameter("id"));
		contactUnitDao.delete(cu);
		
		return contactUnit(request, user);
	}
	
	@Transactional
	public XModResponse materiel(HttpServletRequest request, User user) {
		List<Materiel> mList = materielDao.queryAll(Materiel.class);
		List<Map<String,Object>> materielList = new BeanUtil<Materiel>().beanList2MapList(mList);
		
		XModResponse rs = new XModResponse();
		rs.put("materielList", materielList);
		rs.setPageReturn("materiel");
		return rs;
	}
	@Transactional
	public XModResponse editMateriel(HttpServletRequest request, User user) {
		Materiel m;
		if("0".equals(request.getParameter("id"))){
			m = new Materiel();
		} else {
			m = materielDao.read(Materiel.class, request.getParameter("id"));
		}
		m.setCode(request.getParameter("code"));
		m.setName(request.getParameter("name"));
		m.setSpecification(request.getParameter("specification"));
		m.setUnit(request.getParameter("unit"));
		m.setTradePrice(Double.valueOf(request.getParameter("tradePrice")));
		m.setCustomerPrice(Double.valueOf(request.getParameter("customerPrice")));
		m.setType(request.getParameter("type"));
		m.setCost(0.0);
		m.setQuantity(0);
		m.setTotalCost(0.0);

		materielDao.create(m);
		
		return materiel(request, user);
	}
	@Transactional
	public XModResponse removeMateriel(HttpServletRequest request, User user) {
		Materiel m = new Materiel();
		m.setId(request.getParameter("id"));
		
		materielDao.delete(m);
		return materiel(request, user);
	}

	@Transactional
	public XModResponse inventory(HttpServletRequest request, User user) {
		List<Inventory> iList = inventoryDao.queryAll(Inventory.class);
		List<Map<String,Object>> inventoryList = new BeanUtil<Inventory>().beanList2MapList(iList, new Object(){
			public String getOperation(int i) {if(i == 0) return "采购"; if(i == 1) return "销售"; return "其他";}
			public String getContactUnit(String id) {
				ContactUnit cu = contactUnitDao.read(ContactUnit.class, id);
				return cu.getCode()+"|"+cu.getName();
			}
			public String getMateriel(String id) {
				Materiel m = materielDao.read(Materiel.class, id);
				return m.getCode()+"|"+m.getName();
			}
			public String getWarehouse(int i) {if(i == 0) return "工厂"; if(i == 1) return "店面"; return "其他";}
		});
		
		List<Materiel> mList = materielDao.queryAll(Materiel.class);
		List<Map<String,Object>> materielList = new BeanUtil<Materiel>().beanList2MapList(mList);
		
		
		XModResponse rs = new XModResponse();
		rs.put("materielList", materielList);
		rs.put("inventoryList", inventoryList);
		rs.setPageReturn("inventory");
		return rs;
	}
	@Transactional
	public XModResponse sales(HttpServletRequest request, User user) {
		List<Sales> sList = salesDao.queryAll(Sales.class);
		List<Map<String,Object>> salesList =  new BeanUtil<Sales>().beanList2MapList(sList, new Object(){
			public Object getInventory(String id) {
				Inventory inventory = inventoryDao.read(Inventory.class, id);
				return new BeanUtil().bean2Map(inventory, new Object(){
					public String getContactUnit(String id) {
						ContactUnit cu = contactUnitDao.read(ContactUnit.class, id);
						return cu.getCode()+"|"+cu.getName();
					}
					public String getMateriel(String id) {
						Materiel m = materielDao.read(Materiel.class, id);
						return m.getCode()+"|"+m.getName();
					}
				});
			}
		});
		
		List<ContactUnit> cuList = contactUnitDao.queryAll(ContactUnit.class);
		List<Map<String,Object>> contactUnitList = new BeanUtil<ContactUnit>().beanList2MapList(cuList, new Object(){
			public String getIsCustomer(Boolean isCustomer) {return isCustomer?" 客户  ":"";}
			public String getIsSupplier(Boolean isSupplier) {return isSupplier?" 供应商  ":"";}
		});
		
		List<Materiel> mList = materielDao.queryAll(Materiel.class);
		List<Map<String,Object>> materielList = new BeanUtil<Materiel>().beanList2MapList(mList);
		
		
		List<User> uList = accountService.getUserList();
		List<Map<String,Object>> userList = new BeanUtil<User>().beanList2MapList(uList);
		
		XModResponse rs = new XModResponse();
		rs.put("materielList", materielList);
		rs.put("contactUnitList", contactUnitList);
		rs.put("salesList", salesList);
		rs.put("userList", userList);
		rs.setPageReturn("sales");
		return rs;
	}
	@Transactional
	public XModResponse salesSubmit(HttpServletRequest request, User user) throws StourWAYException {
		int itemQuantity = Integer.parseInt(request.getParameter("itemQuantity"));
		
		Sales sales;
		Inventory inventory;
		for(int i = 0; i < itemQuantity; i++){
			inventory = new Inventory();
			inventory.setContactUnit(request.getParameter("contactUnit"));
			try {
				inventory.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("date")));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			inventory.setMateriel(request.getParameter("materiel"+i));
			inventory.setOperation(Inventory.INVENTORY_OPERATION_1);
			inventory.setQuantity(Integer.parseInt(request.getParameter("quantity"+i)));
			inventory.setTotalCost(0.0);
			inventory.setTotalPrice(Double.parseDouble(request.getParameter("totalPrice"+i)));
			inventory.setWarehouse(Inventory.INVENTORY_WAREHOUSE_0);
			createInventory(inventory);
			
			
			sales = new Sales();
			sales.setBusinessUserId(request.getParameter("businessUserId"));
			sales.setComment(request.getParameter("comment"+i));
			try {
				sales.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("date")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			sales.setInventory(inventory.getId());
			sales.setNumber(request.getParameter("number"));
			sales.setReviewUserId(request.getParameter("reviewUserId"));
			sales.setReceiver(request.getParameter("receiver"));
			

			salesDao.create(sales);
		}

		return sales(request, user);
	}
	@Transactional
	public XModResponse purchase(HttpServletRequest request, User user) {
		List<Purchase> pList = purchaseDao.queryAll(Purchase.class);
		List<Map<String,Object>> purchaseList = new BeanUtil<Purchase>().beanList2MapList(pList, new Object(){
			public Object getInventory(String id) {
				return inventoryDao.read(Inventory.class, id);
			}
		});
		for(Map<String, Object> purchase : purchaseList) {
			ContactUnit cu = contactUnitDao.read(ContactUnit.class, (String)((Map<String, Object>)purchase.get("inventory")).get("contactUnit"));
			((Map<String, Object>)purchase.get("inventory")).put("contactUnit", cu.getCode()+"|"+cu.getName());
			Materiel m =  materielDao.read(Materiel.class, (String)((Map)purchase.get("inventory")).get("materiel"));
			((Map<String, Object>)purchase.get("inventory")).put("materiel", m.getCode()+"|"+m.getName());
		}
		
		List<ContactUnit> cuList = contactUnitDao.queryAll(ContactUnit.class);
		List<Map<String,Object>> contactUnitList = new BeanUtil<ContactUnit>().beanList2MapList(cuList, new Object(){
			public String getIsCustomer(Boolean isCustomer) {return isCustomer?" 客户  ":"";}
			public String getIsSupplier(Boolean isSupplier) {return isSupplier?" 供应商  ":"";}
		});
		
		List<Materiel> mList = materielDao.queryAll(Materiel.class);
		List<Map<String,Object>> materielList = new BeanUtil<Materiel>().beanList2MapList(mList);
		
		
		List<User> uList = accountService.getUserList();
		List<Map<String,Object>> userList = new BeanUtil<User>().beanList2MapList(uList);
		
		XModResponse rs = new XModResponse();
		rs.put("materielList", materielList);
		rs.put("contactUnitList", contactUnitList);
		rs.put("purchaseList", purchaseList);
		rs.put("userList", userList);
		rs.setPageReturn("purchase");
		return rs;
	}
	@Transactional
	public XModResponse purchaseSubmit(HttpServletRequest request, User user) throws StourWAYException {
		int itemQuantity = Integer.parseInt(request.getParameter("itemQuantity"));
		
		Purchase purchase;
		Inventory inventory;
		for(int i = 0; i < itemQuantity; i++){
			inventory = new Inventory();
			inventory.setContactUnit(request.getParameter("contactUnit"));
			try {
				inventory.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("date")));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			inventory.setMateriel(request.getParameter("materiel"+i));
			inventory.setOperation(Inventory.INVENTORY_OPERATION_0);
			inventory.setQuantity(Integer.parseInt(request.getParameter("quantity"+i)));
			inventory.setTotalCost(Double.parseDouble(request.getParameter("totalCost"+i)));
			inventory.setTotalPrice(0.0);
			inventory.setWarehouse(Inventory.INVENTORY_WAREHOUSE_0);
			createInventory(inventory);
			
			purchase = new Purchase();
			purchase.setBusinessUserId(request.getParameter("businessUserId"));
			purchase.setComment(request.getParameter("comment"+i));
			try {
				purchase.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("date")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			purchase.setInventory(inventory.getId());
			purchase.setNumber(request.getParameter("number"));
			purchase.setReviewUserId(request.getParameter("reviewUserId"));
			

			purchaseDao.create(purchase);
		}

		return purchase(request, user);
	}

	
	
	@Transactional
	public XModResponse subject(HttpServletRequest request, User user) {
		XModResponse rs = new XModResponse();
		rs.put("subject", subjectDao.queryAll("Subject.getSubjectList", new Object[]{}));
		rs.setPageReturn("subject");
		return rs;
	}
	@Transactional
	public XModResponse subjectManage(HttpServletRequest request, User user) {
		XModResponse rs = new XModResponse();
		rs.put("subject", subjectDao.queryAll("Subject.getSubjectList", new Object[]{}));
		rs.setPageReturn("subjectManage");
		return rs;
	}

	@Transactional
	public XModResponse removeSubject(HttpServletRequest request, User user) {
		XModResponse rs = new XModResponse();
		Subject sub = new Subject();
		sub.setId(request.getParameter("id"));
		subjectDao.delete(sub);
		rs.setPageReturn("subjectManage");
		return rs;
	}
	
	
	/***
	 * 凭证管理
	 */
	@Transactional
	public XModResponse voucher(HttpServletRequest request, User user) {
		List<Voucher> vList = voucherDao.queryAll("Voucher.getVoucherList", new Object[]{});
		List<Map<String,Object>> voucherList = new BeanUtil<Voucher>().beanList2MapList(vList, new Object(){
			public String getSubject(String id) {
				String name = "";
				Subject subject = subjectDao.read(Subject.class, id);
				while(!id.isEmpty()) {
					name = subjectDao.read(Subject.class, id).getName() + "_" + name;
					id = subjectDao.read(Subject.class, id).getParentId();
				}
				
				return subject.getCode() + " " + name;
			}
		});
		
		List<Subject> sList = subjectDao.queryAll("Subject.getSubjectList", new Object[]{});
		List<Map<String,Object>> subjectList = new BeanUtil<Subject>().beanList2MapList(sList, new Object(){
			public String getSubject(String id) {
				String name = "";
				Subject subject = subjectDao.read(Subject.class, id);
				while(!id.isEmpty()) {
					name = subjectDao.read(Subject.class, id).getName() + "_" + name;
					id = subjectDao.read(Subject.class, id).getParentId();
				}
				
				return subject.getCode() + " " + name;
			}
		});
		
		XModResponse rs = new XModResponse();
		rs.put("subjectList", subjectList);
		rs.put("voucherList", voucherList);
		rs.setPageReturn("voucher");
		return rs;
	}
	@Transactional
	public XModResponse voucherDetail(HttpServletRequest request, User user) {
		Date theDate = new Date();
		try {
			theDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("date"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Voucher> vList = voucherDao.queryAll("Voucher.getVoucherDetail", new Object[]{
				theDate, request.getParameter("codeName"),request.getParameter("codeNumber")
		});
		List<Map<String,Object>> voucherList = new BeanUtil<Voucher>().beanList2MapList(vList, new Object(){
			public String getCodeName(String codeName) {  return "0".equals(codeName)?"记":codeName; }
			public String getSubject(String id) {
				String name = "";
				Subject subject = subjectDao.read(Subject.class, id);
				while(!id.isEmpty()) {
					name = subjectDao.read(Subject.class, id).getName() + "_" + name;
					id = subjectDao.read(Subject.class, id).getParentId();
				}
				
				return subject.getCode() + " " + name;
			}
		});
		
		List<Subject> sList = subjectDao.queryAll("Subject.getSubjectList", new Object[]{});
		List<Map<String,Object>> subjectList = new BeanUtil<Subject>().beanList2MapList(sList, new Object(){
			public String getSubject(String id) {
				String name = "";
				Subject subject = subjectDao.read(Subject.class, id);
				while(!id.isEmpty()) {
					name = subjectDao.read(Subject.class, id).getName() + "_" + name;
					id = subjectDao.read(Subject.class, id).getParentId();
				}
				
				return subject.getCode() + " " + name;
			}
		});
		
		XModResponse rs = new XModResponse();
		rs.put("subjectList", subjectList);
		rs.put("voucherList", voucherList);
		rs.setPageReturn("voucherDetail");
		return rs;
	}
	@Transactional
	public XModResponse voucherSubmit(HttpServletRequest request, User user) {
		int itemQuantity = Integer.parseInt(request.getParameter("itemQuantity"));
		
		Voucher voucher;
		for(int i = 0; i < itemQuantity; i++){
			voucher = new Voucher();
			try {
				voucher.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("date")));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			voucher.setListerUserId(user.getId());
			voucher.setCodeName(request.getParameter("codeName"));
			voucher.setCodeNumber(request.getParameter("codeNumber"));
			voucher.setInvoices(Integer.parseInt(request.getParameter("invoices")));
			
			voucher.setSubject(request.getParameter("subject"+i));
			voucher.setComment(request.getParameter("comment"+i));
			voucher.setDebit(Double.parseDouble(request.getParameter("debit"+i).isEmpty()?"0":request.getParameter("debit"+i)));
			voucher.setCredit(Double.parseDouble(request.getParameter("credit"+i).isEmpty()?"0":request.getParameter("credit"+i)));
			
			voucherDao.create(voucher);
		}

		return voucher(request, user);
	}
	
	/***
	 * 出入库逻辑维护
	 */
	@Transactional
	public void createInventory(Inventory inventory) throws StourWAYException {
		Materiel materiel = materielDao.read(Materiel.class, inventory.getMateriel());
		//采购入库
		if(Inventory.INVENTORY_OPERATION_0.equals(inventory.getOperation())) {
			materiel.setQuantity(materiel.getQuantity() + inventory.getQuantity());
			materiel.setTotalCost(materiel.getTotalCost() + inventory.getTotalCost());
			materiel.setCost(new BigDecimal(materiel.getTotalCost() / materiel.getQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		//销售出库
		if(Inventory.INVENTORY_OPERATION_1.equals(inventory.getOperation())) {
			if(inventory.getQuantity() > materiel.getQuantity())
				throw new StourWAYException("库存不足", null);
			
			materiel.setQuantity(materiel.getQuantity() - inventory.getQuantity());
			if(0 == materiel.getQuantity())
				inventory.setTotalCost(materiel.getTotalCost());
			else
				inventory.setTotalCost(new BigDecimal(inventory.getQuantity() / materiel.getQuantity() * materiel.getTotalCost()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			
			materiel.setTotalCost(materiel.getTotalCost() - inventory.getTotalCost());
			if(0 == materiel.getQuantity())
				materiel.setCost(0.0);
			else
				materiel.setCost(new BigDecimal(materiel.getTotalCost() / materiel.getQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		inventoryDao.create(inventory);
		materielDao.update(materiel);
	}

	public CustomizationXMod getXMod() {
		CustomizationXMod xmod = new CustomizationXMod();
		xmod.setId("accountant");
		xmod.setModName("会计财务");
		xmod.setModDescription("系统默认会计财务模块");
		xmod.setEnabled(false);
		
		List<CustomizationXModFunction> functionList = new ArrayList<CustomizationXModFunction>();
		CustomizationXModFunction function = new CustomizationXModFunction();
		function.setFunctionName("进入会计财务系统");
		function.setFunctionDescription("");
		function.setPermissionName("accountant");
		functionList.add(function);
		function = new CustomizationXModFunction();
		function.setFunctionName("进入凭证管理界面");
		function.setFunctionDescription("");
		function.setPermissionName("voucher");
		functionList.add(function);
		xmod.setFunctionList(functionList);
		
		return xmod;
	}
}
