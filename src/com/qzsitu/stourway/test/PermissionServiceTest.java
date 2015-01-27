package com.qzsitu.stourway.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qzsitu.stourway.domain.Group;
import com.qzsitu.stourway.domain.Permission;
import com.qzsitu.stourway.domain.User;
import com.qzsitu.stourway.exception.NoPermissionException;
import com.qzsitu.stourway.service.AccountService;
import com.qzsitu.stourway.service.PermissionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContextTest.xml")
public class PermissionServiceTest {
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private AccountService accountService;
	
	@Before
	public void setUp(){
			User user = new User();
			user.setId("testAccount");
			user.setName("测试账号");
			user.setPassword("testPassword");
			user.setEmail("testEmail@gmail.com");
			user.setMobilephone("186000000001");
			accountService.createUser(user);
		
		for(int i = 0; i < 3; i++) {
			Group group = new Group();
			group.setName("testGroup"+i);
			group.setType(Group.GROUP_TYPE_GROUP);
			group.setParentGroup(Group.GROUP_ROOT);
			group.setDescription("测试分组"+i);
			
			accountService.createGroup(group);
			
			accountService.addUserToGroup(group.getId(), "testAccount");
		}
		
		Group group1 = accountService.getGroupList().get(0);
		Group group2 = accountService.getGroupList().get(1);
		Group group3 = accountService.getGroupList().get(2);
		
		permissionService.addPermission(group1.getId(), "testPermission1", Permission.PERMISSION_TYPE_SYSTEM);
		permissionService.addPermission(group2.getId(), "testPermission2", Permission.PERMISSION_TYPE_SYSTEM);
		permissionService.addPermission(group3.getId(), "testPermission3", Permission.PERMISSION_TYPE_KNOWLEDGE);
		
		assertEquals(2, permissionService.getPermissionList(Permission.PERMISSION_TYPE_SYSTEM).size());
		assertEquals(1, permissionService.getPermissionList(Permission.PERMISSION_TYPE_KNOWLEDGE).size());
	}
	
	@After
	public void tearDown() {
		accountService.removeUser("testAccount");

		for(Group group : accountService.getGroupList()){
			accountService.removeGroup(group.getId());
		}
		
		permissionService.resetPermision(new ArrayList<Permission>(), Permission.PERMISSION_TYPE_SYSTEM);
		permissionService.resetPermision(new ArrayList<Permission>(), Permission.PERMISSION_TYPE_KNOWLEDGE);
		
		assertEquals(0, permissionService.getPermissionList(Permission.PERMISSION_TYPE_SYSTEM).size());
		assertEquals(0, permissionService.getPermissionList(Permission.PERMISSION_TYPE_KNOWLEDGE).size());
	}
	
	//添加权限测试
	@Test
	public void addPermission()	{
		Group group1 = accountService.getGroupList().get(0);
		Group group2 = accountService.getGroupList().get(1);
		Group group3 = accountService.getGroupList().get(2);
		
		permissionService.addPermission(group1.getId(), "testPermission4", Permission.PERMISSION_TYPE_SYSTEM);
		permissionService.addPermission(group2.getId(), "testPermission5", Permission.PERMISSION_TYPE_SYSTEM);
		permissionService.addPermission(group3.getId(), "testPermission6", Permission.PERMISSION_TYPE_KNOWLEDGE);
		
		assertEquals(4, permissionService.getPermissionList(Permission.PERMISSION_TYPE_SYSTEM).size());
		assertEquals(2, permissionService.getPermissionList(Permission.PERMISSION_TYPE_KNOWLEDGE).size());
	}
	
	//测试权限验证
	@Test
	public void checkPermission() {
		User user = accountService.getUser("testAccount");
		try {
			permissionService.checkPermission(user, "testPermission11");
			fail("Expect NoPermissionException");
		} catch (NoPermissionException e) {
			
		}
		try {
			assertTrue(permissionService.checkPermission(user, "testPermission1"));
		} catch (NoPermissionException e) {
			
		}
	}
}
