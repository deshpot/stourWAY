package com.qzsitu.stourway.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qzsitu.stourway.domain.CustomizationView;
import com.qzsitu.stourway.domain.CustomizationXMod;
import com.qzsitu.stourway.domain.Group;
import com.qzsitu.stourway.domain.Permission;
import com.qzsitu.stourway.domain.User;
import com.qzsitu.stourway.exception.StourWAYException;
import com.qzsitu.stourway.service.AccountService;
import com.qzsitu.stourway.service.CustomizationService;
import com.qzsitu.stourway.service.PermissionService;
import com.qzsitu.stourway.util.BeanUtil;

@Controller
public class SystemController {
	@Autowired
	private AccountService accountService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private CustomizationService customizationService;
	
	//进入组织架构管理页面
	@RequestMapping(value="/group")
	public String viewGroup(HttpServletRequest request, Model model) throws StourWAYException{
		List<Group> groups = accountService.getGroupList();
		Map<Group, List<User>> groupUsers = new HashMap<Group, List<User>>();
		List<User> allUser = accountService.getUserList();
		for(Group group:groups){
			List<User> userList = accountService.getUserListByGroupId(group.getId());
			groupUsers.put(group, userList);
		}
		model.addAttribute("groups", groups);	
		model.addAttribute("groupUsers", groupUsers);
		model.addAttribute("allUser", allUser);	
		return "account/group";
	}
	//进入组织架构管理页面
	@RequestMapping(value="/groupManage")
	public String viewGroupManage(HttpServletRequest request, Model model) throws StourWAYException{
		List<Group> groups = accountService.getGroupList();
		Map<Group, List<User>> groupUsers = new HashMap<Group, List<User>>();
		List<User> allUser = accountService.getUserList();
		for(Group group:groups){
			List<User> userList = accountService.getUserListByGroupId(group.getId());
			groupUsers.put(group, userList);
		}
		model.addAttribute("groups", groups);	
		model.addAttribute("groupUsers", groupUsers);
		model.addAttribute("allUser", allUser);
		return "account/groupManage";
	}

	@RequestMapping(value="/employee")
	public String viewEmployee(HttpServletRequest request, Model model) throws StourWAYException{
		List<User> users = accountService.getUserList();
		model.addAttribute("users", users);		
		
		return "account/employee";
	}
	
	@RequestMapping(value="/employeeManage")
	public String viewEmployeeManage(HttpServletRequest request, Model model) throws StourWAYException{
		List<User> users = accountService.getUserList();
		model.addAttribute("groups", users);		
		
		return "account/employeeManage";
	}
	
	//进入权限管理页面
	@RequestMapping(value="/permission")
	public String viewPermission(HttpServletRequest request, Model model) throws StourWAYException{
		List<String> roleList = accountService.getRoleList();
		List<String> rolePermissionList = new ArrayList<String>();
		List<Permission> permissionList = permissionService.getPermissionList(Permission.PERMISSION_TYPE_SYSTEM);
		for(Permission p:permissionList){
			rolePermissionList.add(p.getRoleId() + "-" + p.getPermission());
		}
		
		model.addAttribute("roleList", roleList);
		model.addAttribute("rolePermissionList", rolePermissionList);
		return "account/permission";
	}
	//提交权限变更
	@RequestMapping(value="/submitPermission")
	public String managePermission(HttpServletRequest request, Model model) throws StourWAYException{
		String permissionS = request.getParameter("permission");
		List<Permission> permissionList = new ArrayList<Permission>();
		for(String p : permissionS.split("-=STSD=-")) {
			if(p.equals("")) continue;
			Permission permission = new Permission();
			permission.setRoleId(p.split("-")[0]);
			permission.setPermission(p.split("-")[1]);
			permission.setType(Permission.PERMISSION_TYPE_SYSTEM);
			permissionList.add(permission);
		}
		permissionService.resetPermision(permissionList, Permission.PERMISSION_TYPE_SYSTEM);
		return "forward:/permission";
	}
	//取得所有部门及团队信息
	@RequestMapping("/getAllGroup")
	@ResponseBody
	public List<Group> ajaxGetAllGroup(HttpServletRequest request, Model model) throws StourWAYException{
		List<Group> groupList = accountService.getGroupList();
		for(int i=0;i<groupList.size();){
			if(groupList.get(i).getType().equals("role")){
				groupList.remove(i);
			}
			else
			{
				i++;
			}
		}
		return groupList;
	}
	//取得所有角色信息
	@RequestMapping("/getGroupRole")
	@ResponseBody
	public List<Group> ajaxGetGroupRole(HttpServletRequest request, Model model) throws StourWAYException{
		List<Group> roleList = accountService.getGroupList();
		for(int i=0;i<roleList.size();){
			if(!(roleList.get(i).getType().equals("role"))){
				roleList.remove(i);
			}
			else{
				i++;
			}
		}
		return roleList;
	}
	//根据部门或团队取得角色信息
	@RequestMapping("/getRoleByParentGroup/{parentGroupId}")
	@ResponseBody
	public List<Group> ajaxGetRoleByParentId(HttpServletRequest request, @PathVariable String parentGroupId) throws StourWAYException{
		List<Group> roleList = accountService.getGroupByParentGroup(parentGroupId);
		for(int i=0;i<roleList.size();){
			if(!(roleList.get(i).getType().equals("role"))){
				roleList.remove(i);
			}
			else{
				i++;
			}
		}
		return roleList;
	}
	//取得子部门或子团队信息
	@RequestMapping("/getGroupByParentGroup/{parentGroupId}")
	@ResponseBody
	public List<Group> ajaxGetGroupByParentId(HttpServletRequest request, @PathVariable String parentGroupId) throws StourWAYException{
		return accountService.getGroupByParentGroup(parentGroupId);
	}
	//获得指定部门信息
	@RequestMapping("/getGroup/{groupId}")
	@ResponseBody
	public Group ajaxGetGroupById(HttpServletRequest request, @PathVariable String groupId) throws StourWAYException{
		return accountService.getGroup(groupId);
	}

	//取得部门或团队或角色对应用户列表
	@RequestMapping("/getUserListByGroupId/{groupId}")
	@ResponseBody
	public List<User> ajaxGetUserListByGroupId(HttpServletRequest request, @PathVariable String groupId) throws StourWAYException{
		
		return accountService.getUserListByGroupId(groupId);
	}
	//删除部门团队角色
	@RequestMapping(value="/removeGroup/{groupId}")
	@ResponseBody
	public int ajaxRemoveGroup(HttpServletRequest request, @PathVariable String groupId) throws StourWAYException{
		Group group =accountService.getGroup(groupId);
		if(accountService.getGroupByParentGroup(group.getId()).size() ==0){
			accountService.removeGroup(groupId);
			return 0;
		}else
			return 1;
	}
	//修改部门团队角色信息
	@RequestMapping(value="/editGroup")
	@ResponseBody
	public Group ajaxManageGroup(HttpServletRequest request, @RequestBody Group group) throws StourWAYException{
		if(group.getId().equals("0")){
			accountService.createGroup(group);
		}
		else{
			accountService.updateGroup(group);
		}
		return group;
	}
	//从部门团队角色中移除用户
	@RequestMapping(value="/removeUserToRole")
	@ResponseBody
	public String ajaxRemoveUserToRole(HttpServletRequest request) throws StourWAYException{
		String roleId = request.getParameter("roleId");
		String userId = request.getParameter("userId");
		accountService.removeUserFromGroup(roleId, userId);
		return null;
	}
	//添加用户至部门团队角色
	@RequestMapping(value="/addUserToRole")
	@ResponseBody
	public String ajaxSetUserListInRole(HttpServletRequest request) throws StourWAYException{
		int userSize = Integer.parseInt(request.getParameter("userSize"));
		String roleId = request.getParameter("roleId");
		accountService.removeUsersInGroup(roleId);
		for(int i = 0; i < userSize; i++) {
			String userId = request.getParameter("user"+i);
			accountService.addUserToGroup(roleId, userId);
		}
		
		return null;
	}
	//取得所有用户信息
	@RequestMapping("/getUserList")
	@ResponseBody
	public List<User> ajaxGetUserList(HttpServletRequest request, Model model) throws StourWAYException{
		
		return accountService.getUserList();
	}
	//删除用户
	@RequestMapping(value="/removeUser/{userId}")
	@ResponseBody
	public void ajaxRemoveUser(HttpServletRequest request, @PathVariable String userId) throws StourWAYException{
		accountService.removeUser(userId);
	}
	//编辑用户
	@RequestMapping(value="/editUser/{operate}")
	@ResponseBody
	public int ajaxEditUser(HttpServletRequest request, @RequestBody User user,@PathVariable String operate)  throws StourWAYException{
		if(operate.equals("0")){
			String userId=user.getId();
			User user1=accountService.getUser(userId);
			user.setGroupList(user1.getGroupList());
			user.setPassword(user1.getPassword());
			accountService.updateUser(user);
		}
		else{
			if(accountService.getUser(user.getId())!=null)
				return 1;
			user.setPassword(user.getId());
			accountService.createUser(user);
		}
		return 0;
	}
	//重置密码为123456
	@RequestMapping(value="/resetPsd/{userId}")
	@ResponseBody
	public int ajaxResetPsd(HttpServletRequest request, @PathVariable String userId)  throws StourWAYException{
		String password="123456";
		accountService.resetPassword(userId, password);
		return 0;
	}
	//根据ID取得用户信息
	@RequestMapping(value="/getUser/{userId}")
	@ResponseBody
	public User ajaxGetUserById(HttpServletRequest request, @PathVariable String userId) throws StourWAYException{
		return accountService.getUser(userId);
	}
	//取得用户对应角色部门团队信息
	@RequestMapping(value="/getGroupListByUserId/{userId}")
	@ResponseBody
	public List<Group> ajaxGetGroupListByUserId(HttpServletRequest request, @PathVariable String userId) throws StourWAYException{
		return accountService.getGroupListByUserId(userId);
	}
	
//暂不启用
//	@RequestMapping(value="/viewManage")
//	public String viewManage(HttpServletRequest request, Model model, HttpSession session) throws Exception {
//		List<CustomizationXMod> xmodList = customizationService.getAllCustomizationXMod();
//		model.addAttribute("xmodList",
//				new BeanUtil<CustomizationXMod>().beanList2MapList(xmodList,null, 2));
//		List<CustomizationView> viewList = customizationService.getAllCustomizationView();
//		model.addAttribute("viewList",
//				new BeanUtil<CustomizationXMod>().beanList2MapList(viewList,null, 2));
//		
//		return "systemManage/viewManage";
//	}
	@RequestMapping(value="/xmodManage")
	public String xmodManage(HttpServletRequest request, Model model, HttpSession session) throws Exception {
		List<CustomizationXMod> xmodList = customizationService.getAllCustomizationXMod();
		model.addAttribute("xmodList",
				new BeanUtil<CustomizationXMod>().beanList2MapList(xmodList,null, 2));
		
		model.addAttribute("roleList", accountService.getRoleList());
		
		List<String> rolePermissionList = new ArrayList<String>();
		List<Permission> permissionList = permissionService.getPermissionList(Permission.PERMISSION_TYPE_XMOD);
		for(Permission p:permissionList){
			rolePermissionList.add(p.getRoleId() + "-" + p.getPermission());
		}
		model.addAttribute("rolePermissionList", rolePermissionList);
		
		return "systemManage/xmodManage";
	}
	@RequestMapping(value="/xmodManageSubmit")
	public String xmodManageSubmit(HttpServletRequest request, Model model, HttpSession session) throws Exception {
		String permissionS = request.getParameter("permission");
		List<Permission> permissionList = new ArrayList<Permission>();
		for(String p : permissionS.split("-=STSD=-")) {
			if(p.equals("")) continue;
			Permission permission = new Permission();
			permission.setRoleId(p.split("-")[0]);
			permission.setPermission(p.split("-")[1]);
			permission.setType(Permission.PERMISSION_TYPE_XMOD);
			permissionList.add(permission);
		}
		permissionService.resetPermision(permissionList, Permission.PERMISSION_TYPE_XMOD);
		return "forward:/xmodManage";
	}
}
