package com.qzsitu.stourway.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.qzsitu.stourway.domain.Group;
import com.qzsitu.stourway.domain.User;
import com.qzsitu.stourway.service.AccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContextTest.xml")
public class AccountServiceTest {
	@Autowired
	private AccountService accountService;
	
	
	@Before
	public void setUp(){
		int number = 20;
		for(int i = 0; i < number; i++) {
			User user = new User();
			user.setId("testAccount"+i);
			user.setName("测试账号");
			user.setPassword("testPassword"+i);
			user.setEmail("testEmail"+i+"@gmail.com");
			user.setMobilephone("18600"+("000000"+i).substring((""+i).length()));
			accountService.createUser(user);
		}
		number = 10;
		for(int i = 0; i < number; i++) {
			Group group = new Group();
			group.setName("testGroup"+i);
			group.setType(Group.GROUP_TYPE_GROUP);
			group.setParentGroup(Group.GROUP_ROOT);
			group.setDescription("测试分组"+i);
			
			accountService.createGroup(group);
		}
	}
	
	@After
	public void tearDown() {
		int number = 20;
		for (int i = 0; i < number; i++) {
			accountService.removeUser("testAccount" + i);
		}

		for(Group group : accountService.getGroupList()){
			accountService.removeGroup(group.getId());
		}
	}
	
	//正常创建用户
	@Test
	public void createUser001(){
		User user = new User();
		user.setId("testAccount");
		user.setPassword("123456");
		user.setEmail("hongtu.du@gmail.com");
		user.setMobilephone("18659051586");

		accountService.createUser(user);
		assertNotNull(accountService.getUser("testAccount"));
		assertNotEquals(user.getPassword(),"123456");
		accountService.removeUser("testAccount");
	}
	
	//重复创建用户
	@Test
	public void createUser002(){
		assertNotNull(accountService.getUser("testAccount1"));
		User user = new User();
		user.setId("testAccount1");
		user.setPassword("aaaa");
		user.setEmail("aaaa");
		user.setMobilephone("aaaa");
		try{
			accountService.createUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(accountService.getUser("testAccount1"));
		assertEquals("testEmail1@gmail.com", accountService.getUser("testAccount1").getEmail());
	}
	
	//验证用户
	@Test
	public void checkUser001(){
		assertNotNull(accountService.checkUser("testAccount2", "testPassword2"));
		assertNull(accountService.checkUser("testAccount2", "testAccount"));
	}
	
	//更新用户信息
	@Test
	public void updateUser001(){
		User user1 = accountService.getUser("testAccount1");
		assertEquals(user1.getEmail(),"testEmail1@gmail.com");
		
		User user0 = new User();
		user0.setId("testAccount1");
		user0.setEmail("233472543@qq.com");
		user0.setMobilephone("18659051586");
		accountService.updateUser(user0);
		
		User user2 = accountService.getUser("testAccount1");
		assertEquals(user2.getEmail(),"233472543@qq.com");
	}
	
	//重置密码
	@Test
	public void resetPassword001(){
		assertNotNull(accountService.checkUser("testAccount3", "testPassword3"));
		accountService.resetPassword("testAccount3", "testAccount");
		assertNotNull(accountService.checkUser("testAccount3", "testAccount"));
	}
	
	//删除用户
	@Test
	public void removeUser() {
		assertNotNull(accountService.getUser("testAccount1"));
		accountService.removeUser("testAccount1");
		assertNull(accountService.getUser("testAccount1"));
	}
	
	//取得全部用户
	@Test
	public void getUserList() {
		List<User> userList = accountService.getUserList();
		assertEquals(21, userList.size());
	}
	//取得全部用户
	@Test
	public void getUserList002() {
		List<User> userList = accountService.getUserList();
		assertEquals(21, userList.size());
		

		User user = new User();
		user.setId("testAccount999");
		user = accountService.createUser(user);
		
		List<User> users = new ArrayList<User>();
		users.add(user);
		
		Group group1 = new Group();
		group1.setName("test1");
		group1.setType(Group.GROUP_TYPE_ROLE);
		accountService.createGroup(group1);
		accountService.addUserToGroup(group1.getId(), user.getId());
		
		Group group2 = new Group();
		group2.setName("test2");
		group2.setType(Group.GROUP_TYPE_ROLE);
		accountService.createGroup(group2);
		accountService.addUserToGroup(group2.getId(), user.getId());
		
		assertEquals(2, accountService.getGroupListByUserId(user.getId()).size());
		
		userList = accountService.getUserList();
		assertEquals(22, userList.size());
		
		accountService.removeUser(user.getId());
		accountService.removeGroup(group1.getId());
		accountService.removeGroup(group2.getId());
		
	}
	
	//创建组
	@Test
	public void createGroup001() {
		Group group = new Group();
		group.setName("丝途商贸");
		group.setType(Group.GROUP_TYPE_GROUP);
		group.setParentGroup(Group.GROUP_ROOT);
		group.setDescription("测试分组");
		
		accountService.createGroup(group);
		
		assertNotNull(accountService.getGroup(group.getId()));
	}
	
	//查询子Group
	@Test
	public void getGroupByParentGroup001() {
		List<String> groupId = new ArrayList<String>();
		for(Group group : accountService.getGroupList()){
			groupId.add(group.getId());
		}
		
		Group group1 = accountService.getGroup(groupId.get(1));
		Group group2 = accountService.getGroup(groupId.get(2));
		Group group3 = accountService.getGroup(groupId.get(3));
		
		group1.setParentGroup(groupId.get(0));
		group2.setParentGroup(groupId.get(0));
		group3.setParentGroup(groupId.get(0));
		
		accountService.updateGroup(group1);
		accountService.updateGroup(group2);
		accountService.updateGroup(group3);
		
		List<Group> groupList = accountService.getGroupByParentGroup(groupId.get(0));
		assertEquals(3, groupList.size());
		
	}
	
	//查询全部组
	@Test
	public void getGroupList001() {
		List<Group> groupList = accountService.getGroupList();
		assertEquals(groupList.size(), 10);
	}
	
	//添加用户到组
	@Test
	public void addUserToGroup001() {
		for(Group group : accountService.getGroupList()){
			accountService.addUserToGroup(group.getId(), "testAccount1");
		}
		
		assertEquals(accountService.getUser("testAccount1").getGroupList().size(),10);
	}
	
	//移除组中的用户
	@Test
	public void removeUsersInGroup001() {
		List<String> groupId = new ArrayList<String>();
		for(Group group : accountService.getGroupList()){
			accountService.addUserToGroup(group.getId(), "testAccount1");
			groupId.add(group.getId());
		}
		
		assertEquals(accountService.getUser("testAccount1").getGroupList().size(),10);
		accountService.removeUsersInGroup(groupId.get(0));
		accountService.removeUsersInGroup(groupId.get(3));
		accountService.removeUsersInGroup(groupId.get(4));
		assertEquals(accountService.getUser("testAccount1").getGroupList().size(),7);
	}
	
	//移除用户组
	@Test
	public void removeUserFromGroup001() {
		List<String> groupId = new ArrayList<String>();
		for(Group group : accountService.getGroupList()){
			accountService.addUserToGroup(group.getId(), "testAccount1");
			groupId.add(group.getId());
		}
		
		assertEquals(accountService.getUser("testAccount1").getGroupList().size(), 10);
		
		accountService.removeUserFromGroup(groupId.get(0), "testAccount1");
		accountService.removeUserFromGroup(groupId.get(0), "testAccount1");
		accountService.removeUserFromGroup(groupId.get(2), "testAccount1");
		accountService.removeUserFromGroup(groupId.get(5), "testAccount1");
		
		assertEquals(accountService.getUser("testAccount1").getGroupList().size(), 7);
	}
	
	//取得Group下的用户列表
	@Test
	public void getUserListByGroupId() {
		List<String> groupId = new ArrayList<String>();
		for(Group group : accountService.getGroupList()){
			accountService.addUserToGroup(group.getId(), "testAccount1");
			groupId.add(group.getId());
		}
		accountService.addUserToGroup(groupId.get(2), "testAccount2");
		accountService.addUserToGroup(groupId.get(2), "testAccount4");
		accountService.addUserToGroup(groupId.get(2), "testAccount5");
		assertEquals(accountService.getUserListByGroupId(groupId.get(2)).size(),4);
	}
	
	//取得角色列表测试
	@Test
	public void getRoleList() {
		List<String> groupId = new ArrayList<String>();
		for(Group group : accountService.getGroupList()){
			groupId.add(group.getId());
		}
		
		Group group1 = accountService.getGroup(groupId.get(1));
		Group group2 = accountService.getGroup(groupId.get(2));
		Group group3 = accountService.getGroup(groupId.get(3));
		
		group1.setParentGroup(groupId.get(0));
		group2.setParentGroup(groupId.get(0));
		group3.setParentGroup(groupId.get(0));
		
		accountService.updateGroup(group1);
		accountService.updateGroup(group2);
		accountService.updateGroup(group3);
		
		Group role1 = new Group();
		role1.setDescription("测试角色");
		role1.setName("测试员1");
		role1.setType(Group.GROUP_TYPE_ROLE);
		role1.setParentGroup(groupId.get(1));
		accountService.createGroup(role1);
		
//		Group role2 = new Group();
//		role2.setDescription("测试角色");
//		role2.setName("测试员2");
//		role2.setType(Group.GROUP_TYPE_ROLE);
//		role2.setParentGroup(groupId.get(2));
//		accountService.createGroup(role2);
		
		String s1 = accountService.getRoleList().get(0);
		
		assertEquals("测试员1:" + role1.getId() + ":" 
					+ accountService.getGroup(groupId.get(1)).getName() + "-"
					+ accountService.getGroup(groupId.get(0)).getName() + "-", s1);
		
		accountService.removeGroup(role1.getId());
//		accountService.removeGroup(role2.getId());
	}
}
