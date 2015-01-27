package com.qzsitu.stourway.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qzsitu.stourway.domain.Group;
import com.qzsitu.stourway.domain.KnowledgeMenu;
import com.qzsitu.stourway.domain.KnowledgeTag;
import com.qzsitu.stourway.domain.Permission;
import com.qzsitu.stourway.domain.Post;
import com.qzsitu.stourway.domain.User;
import com.qzsitu.stourway.exception.StourWAYException;
import com.qzsitu.stourway.service.AccountService;
import com.qzsitu.stourway.service.KnowledgeService;
import com.qzsitu.stourway.service.PermissionService;

@Controller
@RequestMapping(value="/knowledge")
public class KnowledgeController {
	@Autowired
	private KnowledgeService knowledgeService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private PermissionService permissionService;
	
	//取得当前用户可以访问目录列表
	private List<String> getAvailableMenuIdList(HttpSession session, String operation){
		User user = (User) session.getAttribute("user");
		
		List<Group> roleList = user.getGroupList();
		List<String> menuIdList = new ArrayList<String>();
		for(Group role:roleList) {
			List<Permission> permissionList = permissionService.getRolePermission(role.getId(),
					Permission.PERMISSION_TYPE_KNOWLEDGE);
			for(Permission p:permissionList) {
				if(p.getPermission().contains(operation) && !menuIdList.contains(p.getPermission().replaceAll(operation, "")))
						menuIdList.add(p.getPermission().replaceAll(operation, ""));
			}
		}

		List<Permission> permissionList = permissionService.getRolePermission(Permission.PERMISSION_ROLE_EVERYONE,
				Permission.PERMISSION_TYPE_KNOWLEDGE);
		for(Permission p:permissionList) {
			if(p.getPermission().contains(operation) && !menuIdList.contains(p.getPermission().replaceAll(operation, "")))
				menuIdList.add(p.getPermission().replaceAll(operation, ""));
		}
		
		return menuIdList;
	}
	//确认用户对当前目录的权限
	private String getAvailiablePermission(HttpSession session, String menuId) {
		String availiablePermission = "";
		User user = (User) session.getAttribute("user");
		List<Group> roleList = user.getGroupList();
		for(Group role : roleList) {
			List<Permission> permissionList = permissionService.getRolePermission(role.getId(),
					Permission.PERMISSION_TYPE_KNOWLEDGE);
			permissionList.addAll(permissionService.getRolePermission(role.getId(), 
					Permission.PERMISSION_TYPE_SYSTEM));
			permissionList.addAll(permissionService.getRolePermission(Permission.PERMISSION_ROLE_EVERYONE, 
					Permission.PERMISSION_TYPE_KNOWLEDGE));
			for(Permission p:permissionList) {
				if(p.getPermission().contains(menuId+"_publish"))
					availiablePermission += "publish";
				if(p.getPermission().contains(menuId+"_manage"))
					availiablePermission += "manage";
				if(p.getPermission().contains("manageKnowledge"))
					availiablePermission += "admin";
			}
		}
		return availiablePermission;
	}
	//管理分类知识点
	@RequestMapping(value="/knowledgeManage/{menuId}/{tagId}/{page}")
	public String getKnowledgeManage(HttpServletRequest request, @PathVariable String menuId,
			 @PathVariable String tagId, @PathVariable String page,
			 Model model, HttpSession session) throws StourWAYException{
		List<String> menuIdList = this.getAvailableMenuIdList(session, "_read");
		
		int index = (Integer.parseInt(page) - 1) * 8;
		List<Post> posts = knowledgeService.getKnowledgeList(menuId, tagId, menuIdList, index, 8);
		int postCount = knowledgeService.getKnowledgeCount(menuId, tagId, menuIdList);
		int pageCount = ((postCount == 0))? 1 : (postCount + (8 - 1)) / 8;
		List<KnowledgeTag> tagList = knowledgeService.getKnowledgeTagList();
		List<KnowledgeMenu> menuList = new ArrayList<KnowledgeMenu>();
		for(String s:menuIdList){
			KnowledgeMenu addMenu = knowledgeService.getKnowledgeMenu(s);
			if(addMenu != null)
				menuList.add(addMenu);
		}
		
		model.addAttribute("page", Integer.parseInt(page));
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("posts", posts);
		model.addAttribute("menuList", menuList);
		model.addAttribute("tagList", tagList);
		model.addAttribute("menu", menuId);
		model.addAttribute("tag", tagId);
		return "knowledge/knowledgeManage";
	}
	//取得分类知识点
	@RequestMapping(value="/knowledge/{menuId}/{tagId}/{page}")
	public String getKnowledge(HttpServletRequest request, @PathVariable String menuId,
			 @PathVariable String tagId, @PathVariable String page,
			 Model model, HttpSession session) throws StourWAYException{
		//如果没有目录，创建默认的新闻公告目录
		if(knowledgeService.getKnowledgeMenuList().size() == 0) {
			String companyNewsMenuId = knowledgeService.createMenu("新闻公告").getId();
			permissionService.addPermission(Permission.PERMISSION_ROLE_EVERYONE, companyNewsMenuId+"_read", Permission.PERMISSION_TYPE_KNOWLEDGE);
		}
		
		List<String> menuIdList = this.getAvailableMenuIdList(session, "_read");
		String availiablePermission = this.getAvailiablePermission(session, menuId);
		int index = (Integer.parseInt(page) - 1) * 8;
		List<Post> posts = knowledgeService.getKnowledgeList(menuId, tagId, menuIdList, index, 8);
		int postCount = knowledgeService.getKnowledgeCount(menuId, tagId, menuIdList);
		int pageCount = ((postCount == 0))? 1 : (postCount + (8 - 1)) / 8;
		List<KnowledgeTag> tagList = knowledgeService.getKnowledgeTagList();
		List<KnowledgeMenu> menuList = new ArrayList<KnowledgeMenu>();
		for(String s:menuIdList){
			KnowledgeMenu addMenu = knowledgeService.getKnowledgeMenu(s);
			if(addMenu != null)
				menuList.add(addMenu);
		}
		
		model.addAttribute("page", Integer.parseInt(page));
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("posts", posts);
		model.addAttribute("menuList", menuList);
//		model.addAttribute("availableMenuList", menuIdList);
		model.addAttribute("availiablePermission", availiablePermission);
		model.addAttribute("tagList", tagList);
		model.addAttribute("menu", menuId);
		model.addAttribute("tag", tagId);
		return "knowledge/knowledge";
	}
	//查看指定知识条目
	@RequestMapping(value="/{menuId}/{tagId}/{page}/post/{postId}")
	public String getKnowledgePost(HttpServletRequest request, @PathVariable String postId,@PathVariable String menuId,
			 @PathVariable String tagId, @PathVariable String page,
			Model model, HttpSession session) throws StourWAYException{
		Post post = knowledgeService.getPostById(postId);
		List<KnowledgeMenu> menuList = knowledgeService.getPostMenu(post);
		List<KnowledgeTag> tagList = knowledgeService.getPostTag(post);
		model.addAttribute("post", post);
		model.addAttribute("menuList", menuList);
		model.addAttribute("tagList", tagList);
		model.addAttribute("currentMenu", menuId);
		model.addAttribute("currentTag", tagId);
		model.addAttribute("currentPage", page);
		
		return "knowledge/post";
	}
	//创建文章
	@RequestMapping(value="/newPost/{menuId}")
	public String createKnowledge(HttpServletRequest request, @PathVariable String menuId, Model model,HttpSession session) throws StourWAYException{
		if(request.getParameter("postId") == null) {
			List<KnowledgeMenu> menuList = new ArrayList<KnowledgeMenu>();
			for(String s : this.getAvailableMenuIdList(session, "_publish")){
				KnowledgeMenu addMenu = knowledgeService.getKnowledgeMenu(s);
				if(addMenu != null)
					menuList.add(addMenu);
			}
			List<KnowledgeTag> tagList = knowledgeService.getKnowledgeTagList();
			model.addAttribute("currentMenu", menuId);
			model.addAttribute("menuList", menuList);
			model.addAttribute("tagList", tagList);
			return "knowledge/newPost";
		} else {
			String postId = request.getParameter("postId");
			String postTitle = request.getParameter("postTitle");
			String postContent = request.getParameter("postContent");
			String m = request.getParameter("menu");
			String tags = request.getParameter("tags");
			User user = (User) session.getAttribute("user");
			
			Post post = new Post();
			post.setAuthor(user.getId());
			post.setContent(postContent);
			post.setDatetime(new Date());
			post.setTitle(postTitle);
			post.setType("knowledge");
			post.setStatus("");
			if(!"0".equals(postId))
				post.setId(postId);
			
			knowledgeService.createPost(post);
			knowledgeService.linkPostMenu(post.getId(), m);
			knowledgeService.linkPostTag(post.getId(), tags);

			return "forward:/knowledge/knowledge/"+menuId+"/all/1";
		}
	}
	//编辑文章
	@RequestMapping(value="/{menuId}/{tagId}/{page}/editPost/{postId}")
	public String editKnowledge(HttpServletRequest request,@PathVariable String postId, @PathVariable String menuId,
			 @PathVariable String tagId, @PathVariable String page,
			Model model,HttpSession session) throws StourWAYException{
		if(request.getParameter("postId") == null) {
			Post post = knowledgeService.getPostById(postId);
			List<KnowledgeMenu> menuList = new ArrayList<KnowledgeMenu>();
			for(String s : this.getAvailableMenuIdList(session, "_publish")){
				KnowledgeMenu addMenu = knowledgeService.getKnowledgeMenu(s);
				if(addMenu != null)
					menuList.add(addMenu);
			}
			List<KnowledgeTag> tagList = knowledgeService.getPostTag(post);
			model.addAttribute("post", post);
//			String menu = "";
//			for(KnowledgeMenu m:menuList)
//				menu += m.getId();
			String tag = "";
			for(KnowledgeTag t:tagList)
				tag += t.getName() + " ";
//			model.addAttribute("menu", menu);
			model.addAttribute("tag", tag);
			model.addAttribute("menuList", menuList);
			model.addAttribute("tagList", knowledgeService.getKnowledgeTagList());
			model.addAttribute("currentMenu", menuId);
			model.addAttribute("currentTag", tagId);
			model.addAttribute("currentPage", page);
			return "knowledge/editPost";
		} else {
			String postTitle = request.getParameter("postTitle");
			String postContent = request.getParameter("postContent");
			String tags = request.getParameter("tags");
			String m = request.getParameter("menu");
			User user = (User) session.getAttribute("user");
			
			
			Post post = new Post();
			if(!"0".equals(postId))
				post = knowledgeService.getPostById(postId);
			
			
			post.setAuthor(user.getId());
			post.setContent(postContent);
			post.setTitle(postTitle);
			post.setType("knowledge");
			post.setStatus("");
			
			
			knowledgeService.editPost(post);
			knowledgeService.removePostMenu(post.getId());
			knowledgeService.removePostTag(post.getId());
			knowledgeService.linkPostMenu(post.getId(), m);
			knowledgeService.linkPostTag(post.getId(), tags);

			return "forward:/knowledge/knowledgeManage/"+menuId+"/"+tagId+"/"+page;
		}
	}
	//删除文章
	@RequestMapping(value = "/{menuId}/{tagId}/{page}/removePost/{postId}")
	public String removeKnowledge(HttpServletRequest request, @PathVariable String menuId,
			 @PathVariable String tagId, @PathVariable String page,
			@PathVariable String postId, Model model, HttpSession session)
			throws StourWAYException {
		knowledgeService.removePostMenu(postId);
		knowledgeService.removePostTag(postId);
		knowledgeService.removePost(postId);
		return "forward:/knowledge/knowledgeManage/"+menuId+"/"+tagId+"/"+page;
	}
	
	//管理目录
	@RequestMapping(value = "/accessManage")
	public String knowledgeAccessManage(HttpServletRequest request, Model model)
			throws StourWAYException {
		List<KnowledgeMenu> menuList = knowledgeService.getKnowledgeMenuList();
		List<String> roleList = accountService.getRoleList();
		List<String> rolePermissionList = new ArrayList<String>();
		List<Permission> permissionList = permissionService.getPermissionList(
				Permission.PERMISSION_TYPE_KNOWLEDGE);
		for(Permission p:permissionList){
			rolePermissionList.add(p.getRoleId() + "-" + p.getPermission());
		}
		
		model.addAttribute("menuList", menuList);
		model.addAttribute("roleList", roleList);
		model.addAttribute("rolePermissionList", rolePermissionList);
		return "/knowledge/accessManage";
	}
	//提交权限变更
	@RequestMapping(value="/submitAccessManage")
	public String submitAccessManage(HttpServletRequest request, Model model) throws StourWAYException{
		String permissionS = request.getParameter("permission");
		List<Permission> permissionList = new ArrayList<Permission>();
		for(String p : permissionS.split("-=STSD=-")) {
			if(p.equals("")) continue;
			Permission permission = new Permission();
			permission.setRoleId(p.split("-")[0]);
			permission.setPermission(p.split("-")[1]);
			permission.setType(Permission.PERMISSION_TYPE_KNOWLEDGE);
			permissionList.add(permission);
		}
		permissionService.resetPermision(permissionList, Permission.PERMISSION_TYPE_KNOWLEDGE);
		return "forward:/knowledge/accessManage";
	}
	//修改目录名字
	@RequestMapping("/setMenuName")
	@ResponseBody
	public KnowledgeMenu ajaxSetMenuName(HttpServletRequest request, Model model) throws StourWAYException{
		String menuId = request.getParameter("menuId");
		String menuName = request.getParameter("menuName");
		
		if("0".equals(menuId))
			return knowledgeService.createMenu(menuName);
		else
			return knowledgeService.changeMenuName(menuId, menuName);
	}
	//修改目录名字
	@RequestMapping("/removeMenu")
	@ResponseBody
	public String ajaxRemoveMenu(HttpServletRequest request, Model model) throws StourWAYException{
		String menuId = request.getParameter("menuId");
		int count = knowledgeService.getKnowledgeCount(menuId, "all", null);
		if(count == 0) {
			knowledgeService.removeMenu(menuId);
			return "ok";
		} else {
			return "nok";
		}
	}
}
