package com.qzsitu.stourway.util;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.hibernate.Session;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.qzsitu.stourway.dao.GenericDao;
import com.qzsitu.stourway.domain.CustomizationXMod;
import com.qzsitu.stourway.domain.CustomizationXModFunction;
import com.qzsitu.stourway.domain.Group;
import com.qzsitu.stourway.domain.User;
import com.qzsitu.stourway.service.AccountService;
import com.qzsitu.stourway.service.KnowledgeService;
import com.qzsitu.stourway.service.PermissionService;
import com.qzsitu.stourway.xmod.system.XModService;

/**
 * 系统初始化
 * @author Hongtu
 *
 */
public class InitialStourWAY implements InitializingBean, ApplicationContextAware  {
	@Autowired
	private AccountService accountService;
	@Autowired
	private KnowledgeService knowledgeService;
	@Autowired
	private GenericDao<CustomizationXMod> xmodDao;
	
	private ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(accountService.getUser("stourway") == null) {
			User user = new User();
			user.setId("stourway");
			user.setPassword("stourway");
			user.setName("管理员");
			
			accountService.createUser(user);
		}
		
		if(accountService.getGroupList().size() == 0) {
			Group group = new Group();
			group.setName("丝途商道");
			group.setType(Group.GROUP_TYPE_GROUP);
			group.setParentGroup(Group.GROUP_ROOT);
			
			accountService.createGroup(group);
		}
		
		knowledgeService.getKnowledgeMenuList();
		
		for(String s:applicationContext.getBeanNamesForType(XModService.class)) {
			XModService xmodService = (XModService) applicationContext.getBean(s);
			CustomizationXMod xmod = xmodService.getXMod();
			if(xmod == null) continue;
			Session session = xmodDao.openSession();
			session.beginTransaction();
			CustomizationXMod oldXMod = (CustomizationXMod) session.get(CustomizationXMod.class, xmod.getId());
			if(oldXMod != null) {
				oldXMod.setModName(xmod.getModName());
				oldXMod.setModDescription(xmod.getModDescription());
				if(oldXMod.getFunctionList() == null)
					oldXMod.setFunctionList(new ArrayList<CustomizationXModFunction>());
				oldXMod.getFunctionList().clear();
				if(xmod.getFunctionList() != null) {
					oldXMod.getFunctionList().addAll(xmod.getFunctionList());
					for(CustomizationXModFunction f : xmod.getFunctionList()) {
						PermissionService.xmodPermissionList.add(xmod.getId() + "." + f.getPermissionName());
					}
				}
			} else {
				oldXMod = xmod;
			}
			session.save(oldXMod);
			session.getTransaction().commit();
			
			
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
	}

}
