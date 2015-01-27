package com.qzsitu.stourway.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qzsitu.stourway.dao.GenericDao;
import com.qzsitu.stourway.domain.Group;
import com.qzsitu.stourway.domain.Permission;
import com.qzsitu.stourway.domain.User;
import com.qzsitu.stourway.exception.NoPermissionException;
import com.qzsitu.stourway.exception.NoSessionException;

@Service
public class PermissionService {
	@Autowired
	private GenericDao<Permission> permissionDao;
	
	public static final List<String> xmodPermissionList = new ArrayList<String>();
	
	public boolean checkPermission(User user, String permission) throws NoPermissionException {
		if("stourway".equals(user.getId()))
			return true;
		for(Group group:user.getGroupList()) {
			Session s = permissionDao.openSession();
			s.beginTransaction();
			Query query = s.getNamedQuery("Permission.checkPermission");
			query.setParameter(0, group.getId());
			query.setParameter(1, permission);
			Permission p = (Permission) query.uniqueResult();
			s.getTransaction().commit();
			
			if(p != null)
				return true;
		}
		throw new NoPermissionException("没有对应权限，请跟管理员联系", null);
	}
	
	@Transactional
	public void addPermission(String roleId, String permission, String type) {
		Permission p = permissionDao.queryOne("Permission.checkPermission", new Object[]{roleId, permission});
		if(p == null) {
			p = new Permission();
			p.setRoleId(roleId);
			p.setPermission(permission);
			p.setType(type);
			permissionDao.create(p);
		}
	}

	@Transactional
	public void deletePermission(String roleId, String permission, String type) {
		Permission p = permissionDao.queryOne("Permission.checkPermission", new Object[]{roleId, permission});
		if(p != null) {
			permissionDao.delete(p);
		}
	}

	@Transactional
	public List<Permission> getRolePermission(String roleId, String type) {
		return permissionDao.queryAll("Permission.getRolePermissionList", new Object[]{roleId, type});
	}

	@Transactional
	public List<Permission> getPermissionList(String type) {
		return permissionDao.queryAll("Permission.getPermissionList", new Object[]{type});
	}

	@Transactional
	public void resetPermision(List<Permission> permissionList, String type) {
		permissionDao.execute("Permission.deletePermissionList", new String[]{"type"}, new Object[]{type});
		for(Permission p : permissionList) {
			permissionDao.create(p);
		}
	}
	
	@Transactional
	public void checkPermissionForController(JoinPoint joinPoint) throws Exception {
		String method = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		Object targetObj = args[0];
		
		User user = (User) ((HttpServletRequest)targetObj).getSession().getAttribute("user");
		
		//登陆状态验证
		if("noPermission".equals(method))
			return;
		if("noSession".equals(method))
			return;
		if(user == null && !"getIndex".equals(method) && !"getLogout".equals(method))
			throw new NoSessionException("登录超时，请重新登录", null);
		
		//组织架构及人员管理权限
		if("viewGroupManage".equals(method))
			checkPermission(user, "manageGroup");
		if("viewEmployeeManage".equals(method))
			checkPermission(user, "manageEmployee");
		
		//知识库权限
		if("getKnowledgeManage".equals(method))
			checkPermission(user, (String)args[1]+"_manage");
		if("knowledgeAccessManage".equals(method))
			checkPermission(user, "manageKnowledge");
		
		//任务系统权限
		
		
		//权限管理权限
		if("viewPermission".equals(method))
			checkPermission(user, "managePermission");
		if("managePermission".equals(method))
			checkPermission(user, "managePermission");
		
		//扩展模块XMOD权限控制支持
		if("xmodPage".equals(method)) {
			String modName = (String) args[1];
			String function = (String) args[2];
			if(PermissionService.xmodPermissionList.contains(modName + "." + function))
				checkPermission(user, modName + "." + function);
		}
		if("xmodAjax".equals(method)) {
			String modName = (String) args[1];
			String function = (String) args[2];
			if(PermissionService.xmodPermissionList.contains(modName + "." + function))
				checkPermission(user, modName + "." + function);
		}

	}
}
