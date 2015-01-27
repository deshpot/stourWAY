package com.qzsitu.stourway.xmod.companyNews;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qzsitu.stourway.dao.GenericDao;
import com.qzsitu.stourway.domain.CustomizationXMod;
import com.qzsitu.stourway.domain.CustomizationXModFunction;
import com.qzsitu.stourway.domain.KnowledgeMenu;
import com.qzsitu.stourway.domain.Permission;
import com.qzsitu.stourway.domain.Post;
import com.qzsitu.stourway.domain.User;
import com.qzsitu.stourway.service.AccountService;
import com.qzsitu.stourway.service.KnowledgeService;
import com.qzsitu.stourway.util.BeanUtil;
import com.qzsitu.stourway.xmod.system.XModResponse;
import com.qzsitu.stourway.xmod.system.XModService;

@Service
public class CompanyNewsService extends XModService {
	@Autowired
	private KnowledgeService knowledgeService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private GenericDao<Post> postDao;
	
	public static String COMPANYNEWS_MENU_ID="";
	
	public XModResponse companyNews(HttpServletRequest request, User user) {
		int currentPage = request.getParameter("currentPage") == null? 1 : Integer.parseInt(request.getParameter("currentPage"));
		int index = (currentPage - 1) * 12;
		int postCount = knowledgeService.getKnowledgeCount(COMPANYNEWS_MENU_ID, "all", null);
		int pageCount = ((postCount == 0))? 1 : (postCount + (12 - 1)) / 12;
		
		XModResponse rs = new XModResponse();
		List<Post> posts = knowledgeService.getKnowledgeList(COMPANYNEWS_MENU_ID, "all", null, index, 12);
		
		rs.put("postList", new BeanUtil<Post>().beanList2MapList(posts, new Object(){
			public Object getTitle(String title) { return title.replaceAll("<", "&lt;").replaceAll("<", "&gt;"); }
		}));
		rs.put("currentPage", currentPage);
		rs.put("pageCount", pageCount);
		rs.setPageReturn("companyNews");
		return rs;
	}
	
	public XModResponse readPost(HttpServletRequest request, User user) {
		String postId = request.getParameter("postId");
		Post post = knowledgeService.getPostById(postId);
		
		XModResponse rs = new XModResponse();
		rs.put("post", new BeanUtil<Post>().bean2Map(post, new Object(){
			public Object getAuthor(String author) { return accountService.getUser(author).getName(); }
			public Object getTitle(String title) { return title.replaceAll("<", "&lt;").replaceAll("<", "&gt;"); }
		}));
		rs.setPageReturn("readPost");
		return rs;
	}
	
	
	public XModResponse writePost(HttpServletRequest request, User user) {
		String postId = request.getParameter("postId");
		Post post = postId == null? null : knowledgeService.getPostById(postId);
		post = post == null?new Post():post;
		
		XModResponse rs = new XModResponse();
		rs.put("post", new BeanUtil<Post>().bean2Map(post, new Object(){
			public Object getAuthor(String author) { return accountService.getUser(author).getName(); }
			public Object getTitle(String title) { return title.replaceAll("<", "&lt;").replaceAll("<", "&gt;"); }
		}));
		rs.setPageReturn("writePost");
		return rs;
	}
	@Transactional
	public XModResponse writePostSubmit(HttpServletRequest request, User user) {
		Post post = new BeanUtil<Post>().request2Bean(request, new Post(), null, postDao).get(0);
		if(post.getAuthor() == null) post.setAuthor(user.getId());
		post = knowledgeService.editPost(post);
		knowledgeService.linkPostMenu(post.getId(), this.COMPANYNEWS_MENU_ID);
		return companyNews(request, user);
	}
	
	@Override
	public CustomizationXMod getXMod() {
		init();
		
		CustomizationXMod xmod = new CustomizationXMod();
		xmod.setId("companyNews");
		xmod.setModName("新闻公告");
		xmod.setModDescription("");
		xmod.setEnabled(false);
		
		List<CustomizationXModFunction> functionList = new ArrayList<CustomizationXModFunction>();
		CustomizationXModFunction function = new CustomizationXModFunction();
		function.setFunctionName("进入新闻公告模块");
		function.setFunctionDescription("");
		function.setPermissionName("companyNews");
		functionList.add(function);
		function = new CustomizationXModFunction();
		function.setFunctionName("阅读新闻公告");
		function.setFunctionDescription("");
		function.setPermissionName("readPost");
		functionList.add(function);
		function = new CustomizationXModFunction();
		function.setFunctionName("发布新闻公告");
		function.setFunctionDescription("");
		function.setPermissionName("writePost");
		functionList.add(function);
		xmod.setFunctionList(functionList);
		
		return xmod;
	}

	private void init() {
		for(KnowledgeMenu menu : knowledgeService.getKnowledgeMenuList()) {
			if(menu.getName().equals("新闻公告"))
				COMPANYNEWS_MENU_ID = menu.getId();
		}
		if("".equals(COMPANYNEWS_MENU_ID)) {
			COMPANYNEWS_MENU_ID = knowledgeService.createMenu("新闻公告").getId();
		}
	}
}
