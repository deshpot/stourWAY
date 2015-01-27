package com.qzsitu.stourway.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qzsitu.stourway.dao.GenericDao;
import com.qzsitu.stourway.domain.Group;
import com.qzsitu.stourway.domain.User;
import com.qzsitu.stourway.util.SecurityUtil;

@Service
public class AccountService {
	@Autowired
	private GenericDao<User> userDao;
	@Autowired
	private GenericDao<Group> groupDao;
	
	@Transactional
	public User getUser(String userId) {
		return userDao.read(User.class, userId);
	}

	@Transactional
	public User checkUser(String userId, String password) {
		SecurityUtil su = new SecurityUtil();
		password = su.SituMd5Encode(password);
		User user = userDao.read(User.class, userId);
		if(user == null)
			return null;
		if(password.equals(user.getPassword()))
			return user;
		return null;
	}
	
	@Transactional
	public void removeUser(String userId) {
		User user = new User();
		user.setId(userId);
		userDao.delete(user);
	}
	
	@Transactional
	public void updateUser(User user) {
		userDao.update(user);
	}
	
	@Transactional
	public void resetPassword(String userId, String password) {
		User user = userDao.read(User.class, userId);
		SecurityUtil su = new SecurityUtil();
		user.setPassword(su.SituMd5Encode(password));
		userDao.update(user);
	}
	
	@Transactional
	public User createUser(User user) {
		SecurityUtil su = new SecurityUtil();
		user.setPassword(su.SituMd5Encode(user.getPassword()));
		user = userDao.create(user);
		return user;
	}
	
	@Transactional
	public List<Group> getGroupListByUserId(
			String userId) {
		return userDao.read(User.class, userId).getGroupList();
	}

	@Transactional
	public List<String> getRoleList() {
		List<String> rs = new ArrayList<String>();
		
		List<Group> groupList = groupDao.queryAll("Group.getGroupList", new Object[]{});
		Map<String, Group> groupMap = new HashMap<String, Group>();
		for(Group group : groupList){
			groupMap.put(group.getId(), group);
		}
		
		List<Group> roleList = groupDao.queryAll("Group.getRoleList", new Object[]{});
		for(Group role : roleList){
			String s = "";
			s += role.getName() + ":" + role.getId() + ":";
			Group pGroup = role;
			while(!Group.GROUP_ROOT.equals(pGroup.getParentGroup())) {
				pGroup = groupDao.read(Group.class, pGroup.getParentGroup());
				s += pGroup.getName() + "-";
			}
			rs.add(s);
		}
		return rs;
	}

	@Transactional
	public List<Group> getGroupList() {
		return groupDao.queryAll("Group.getAll", new Object[]{});
	}

	@Transactional
	public List<User> getUserList() {
		return userDao.queryAll(User.class);
	}

	@Transactional
	public List<User> getUserListByGroupId(String groupId) {
		Group group = groupDao.read(Group.class, groupId);
		group.getUserList().size();
		return group.getUserList();
	}

	@Transactional
	public List<Group> getGroupByParentGroup(String parentGroupId) {
		
		return groupDao.queryAll("Group.getGroupByParent", new Object[]{parentGroupId});
	}

	@Transactional
	public Group getGroup(String groupId) {
		return groupDao.read(Group.class, groupId);
	}

	@Transactional
	public void removeGroup(String groupId) {
		Group group = groupDao.read(Group.class, groupId);
		if(group != null)
			groupDao.delete(group);
	}

	@Transactional
	public Group createGroup(Group group) {
		return groupDao.create(group);
	}

	@Transactional
	public Group updateGroup(Group group) {
		return groupDao.update(group);
	}

	@Transactional
	public void removeUserFromGroup(String groupId, String userId) {
		User user = userDao.read(User.class, userId);
		Group group = groupDao.read(Group.class, groupId);
		List<Group> groupList = user.getGroupList();
		if(groupList == null)
			groupList = new ArrayList<Group>();
		groupList.remove(group);
		user.setGroupList(groupList);
		userDao.update(user);
	}

	@Transactional
	public void removeUsersInGroup(String groupId) {
		Group group = groupDao.read(Group.class, groupId);
		
		for(User user : group.getUserList()){
			user.getGroupList().remove(group);
			userDao.update(user);
		}
	}

	@Transactional
	public void addUserToGroup(String groupId, String userId) {
		User user = userDao.read(User.class, userId);
		Group group = groupDao.read(Group.class, groupId);
		List<Group> groupList = user.getGroupList();
		if(groupList == null)
			groupList = new ArrayList<Group>();
		groupList.add(group);
		user.setGroupList(groupList);
		userDao.update(user);
	}
}
