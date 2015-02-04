package com.qzsitu.stourway.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qzsitu.stourway.dao.GenericDao;
import com.qzsitu.stourway.domain.FileItem;
import com.qzsitu.stourway.domain.KnowledgeLink;
import com.qzsitu.stourway.domain.KnowledgeMenu;
import com.qzsitu.stourway.domain.KnowledgeTag;
import com.qzsitu.stourway.domain.Post;
import com.qzsitu.stourway.util.StourWAYConfiguration;

@Service
public class KnowledgeService {
	@Autowired
	private GenericDao<KnowledgeMenu> menuDao;
	@Autowired
	private GenericDao<KnowledgeTag> tagDao;
	@Autowired
	private GenericDao<KnowledgeLink> linkDao;
	@Autowired
	private GenericDao<FileItem> fileItemDao;
	@Autowired
	private GenericDao<Post> postDao;
	
	@Transactional
	public List<Post> getKnowledgeList(int index, int count) {
		List<Post> postList = postDao.queryLimit("Post.getPostList", new Object[]{}, index, count);
		return postList;
	}


	/*********************************
	 * @param menuId 取出指定menuId目录下的文章,不限定目录时需指定为"all"
	 * @param tagId 取出指定tagId标签下的文章,不限定标签时需指定为"all"
	 * @param menuIdList 限定搜索目录范围列表，用于辅助权限控制，menuList为null时，自动设置为所有Menu(为空时，增加一个随意字符，避免hql执行失败 )
	 * @param index 起始的下标
	 * @param count 取出文章的数量
	 * @return 返回一组文章
	 */
	@Transactional
	public List<Post> getKnowledgeList(String menuId, String tagId, List<String> menuIdList,
			int index, int count) {
		List<Post> postList = new ArrayList<Post>();
		
		if(menuIdList == null) {
			List<KnowledgeMenu> mList = getKnowledgeMenuList();
			menuIdList = new ArrayList<String>();
			for(KnowledgeMenu m:mList) {
				menuIdList.add(m.getId());
			}
		}
		if(menuIdList.size() == 0)
			return postList;
		
		List<KnowledgeLink> linkList = linkDao.queryLimit("KnowledgeLink.getLinkListByMenuTag", 
				new String[]{"tagId", "menuId", "menuList"},
				new Object[]{tagId, menuId, menuIdList}, index, count);
		List<String> postIdList = new ArrayList<String>();
		for(KnowledgeLink link : linkList) {
			postIdList.add(link.getChildId());
		}
		if(postIdList.size() > 0)
			postList = postDao.queryAll("Post.getPostListByPostIdList", new String[]{"postIdList"}, new Object[]{postIdList});
		
		return postList;
	} 

	/**	
	 * menuList为null时，自动设置为所有Menu
	 * menuList为空时，增加一个随意字符，避免hql执行失败
	*/
	@Transactional
	public int getKnowledgeCount(String menuId, String tagId, List<String> menuIdList) {
		if(menuIdList == null) {
			List<KnowledgeMenu> mList = getKnowledgeMenuList();
			menuIdList = new ArrayList<String>();
			for(KnowledgeMenu m:mList) {
				menuIdList.add(m.getId());
			}
		}
		if(menuIdList.size() == 0)
			return 0;
		
		return linkDao.queryAll("KnowledgeLink.getKnowledgeCount",
				new String[]{"tagId", "menuId", "menuList"},
				new Object[]{tagId, menuId, menuIdList}).size();
	}
	
	@Transactional
	public void linkPostMenu(String postId, String menuId) {
		KnowledgeLink link = linkDao.queryOne("KnowledgeLink.getLink",
				new Object[] {menuId, KnowledgeMenu.class.getName(), postId, Post.class.getName()});
		
		if(link == null) {
			link = new KnowledgeLink();
			link.setParentId(menuId);
			link.setParentType(KnowledgeMenu.class.getName());
			link.setChildId(postId);
			link.setChildType(Post.class.getName());
			link.setDatetime(postDao.read(Post.class, postId).getDatetime());
			linkDao.create(link);
		}
	}
	
	@Transactional
	public void linkPostTag(String postId, String tags) {
		tags = tags.trim();
		if(tags.equals(""))
			return;
		for(String tagName:tags.split("\\s")) {
			tagName = tagName.trim();
			KnowledgeTag tag = tagDao.queryOne("KnowledgeTag.getTagByName", new Object[] {tagName});
			if(tag == null) {
				tag = new KnowledgeTag();
				tag.setName(tagName);
				tagDao.create(tag);
			}
			
			KnowledgeLink link = linkDao.queryOne("KnowledgeLink.getLink",
					new Object[] {tag.getId(), KnowledgeTag.class.getName(), postId, Post.class.getName()});
			if(link == null) {
				link = new KnowledgeLink();
				link.setParentId(tag.getId());
				link.setParentType(KnowledgeTag.class.getName());
				link.setChildId(postId);
				link.setChildType(Post.class.getName());
				link.setDatetime(postDao.read(Post.class, postId).getDatetime());
				linkDao.create(link);
			}
		}
	}

	@Transactional
	public List<KnowledgeMenu> getPostMenu(Post post) {
		List<KnowledgeMenu> menuList = new ArrayList<KnowledgeMenu>();
		
		List<KnowledgeLink> linkList = linkDao.queryAll("KnowledgeLink.getParentLink",
				new Object[] {KnowledgeMenu.class.getName(), post.getId(), Post.class.getName()});
		for(KnowledgeLink link : linkList) {
			KnowledgeMenu menu;
			try {
				menu = menuDao.read(KnowledgeMenu.class, link.getParentId());
				menuList.add(menu);
			} catch (Exception e) {
				continue;
			}
		}
		
		return menuList;
	}

	@Transactional
	public List<KnowledgeTag> getPostTag(Post post) {
		List<KnowledgeTag> tagList = new ArrayList<KnowledgeTag>();
		
		List<KnowledgeLink> linkList = linkDao.queryAll("KnowledgeLink.getParentLink",
				new Object[] {KnowledgeTag.class.getName(), post.getId(), Post.class.getName()});
		for(KnowledgeLink link : linkList) {
			KnowledgeTag tag;
			try {
				tag = tagDao.read(KnowledgeTag.class, link.getParentId());
				tagList.add(tag);
			} catch (Exception e) {
				continue;
			}
		}
		return tagList;
	}

	@Transactional
	public List<KnowledgeMenu> getKnowledgeMenuList() {
		List<KnowledgeMenu> menuList = menuDao.queryAll("KnowledgeMenu.getAllMenu", new Object[]{});
		return menuList;
	}

	@Transactional
	public List<KnowledgeTag> getKnowledgeTagList() {
		List<KnowledgeTag> tagList = tagDao.queryAll("KnowledgeTag.getAllTag", new Object[]{});
		return tagList;
	}

	@Transactional
	public void removePostMenu(String id) {
		List<KnowledgeLink> linkList = linkDao.queryAll("KnowledgeLink.getParentLink",
				new Object[] {KnowledgeMenu.class.getName(), id, Post.class.getName()});
		for(KnowledgeLink link : linkList) {
			linkDao.delete(link);
		}
	}

	@Transactional
	public void removePostTag(String id) {
		List<KnowledgeLink> linkList = linkDao.queryAll("KnowledgeLink.getParentLink",
				new Object[] {KnowledgeTag.class.getName(), id, Post.class.getName()});
		for(KnowledgeLink link : linkList) {
			linkDao.delete(link);
		}
		removeUnusedTag();
	}
	
	@Transactional
	public void removeUnusedTag() {
		List<KnowledgeTag> tagList = getKnowledgeTagList();
		for(KnowledgeTag tag: tagList) {
			int count = this.getKnowledgeCount("%", tag.getId(), null);
			if(count == 0) {
				tagDao.delete(tag);
			}
		}
	}
	
	@Transactional
	public void linkFile(String fileItemId, String parentObj, String parentId) {
		String pType = "";
		if(parentObj.equals("process"))
			pType = ProcessInstance.class.getName();
		KnowledgeLink link = linkDao.queryOne("KnowledgeLink.getLink",
				new Object[] {parentId, pType, fileItemId, FileItem.class.getName()});
		if(link == null) {
			link = new KnowledgeLink();
			link.setParentId(parentId);
			link.setParentType(pType);
			link.setChildId(fileItemId);
			link.setChildType(FileItem.class.getName());
			link.setDatetime(fileItemDao.read(FileItem.class, fileItemId).getCreatedTime());
			linkDao.create(link);
		}
	}

	@Transactional
	public void removeMenu(String menuId) {
		KnowledgeMenu menu = menuDao.read(KnowledgeMenu.class, menuId);
		menuDao.delete(menu);
	}

	@Transactional
	public KnowledgeMenu createMenu(String menuName) {
		KnowledgeMenu menu = new KnowledgeMenu();
		menu.setName(menuName);
		menuDao.create(menu);	
		return menu;
	}

	@Transactional
	public KnowledgeMenu changeMenuName(String menuId, String menuName) {
		KnowledgeMenu menu = menuDao.read(KnowledgeMenu.class, menuId);
		menu.setName(menuName);
		menuDao.update(menu);
		return menu;
	}

	@Transactional
	public KnowledgeMenu getKnowledgeMenu(String knowledgeMenuId) {
		return menuDao.read(KnowledgeMenu.class, knowledgeMenuId);
	}
	
	@Transactional
	public Post getPostById(String id){
		return postDao.read(Post.class, id);
	}

	@Transactional
	public Post createPost(Post post){
		post = postDao.create(post);
		return post;
	}
	
	@Transactional
	public Post editPost(Post post){
		if ("0".equals(post.getId())){
			post.setDatetime(new Date());
			post = postDao.create(post);
			return post;
		}else{
			post = postDao.update(post);
			return post;
		}	
	}
	
	@Transactional
	public int removePost(String id){
		Post post = postDao.read(Post.class, id);
		
		String regex = "/file/download/(\\w{32})";
		Pattern p = Pattern.compile(regex);
		Matcher macher = p.matcher(post.getContent());
		while (macher.find()) {
			String fileId = macher.group(1).trim();
			FileItem f = fileItemDao.read(FileItem.class, fileId);
			File downloadFile=new File(StourWAYConfiguration.getProperties("uploadPath") + fileId);
			downloadFile.delete();
			fileItemDao.delete(f);
		}
		
		postDao.delete(post);
		return 0;
	}
	
	@Transactional
	public FileItem getFile(String id){
		return fileItemDao.read(FileItem.class, id);
	}
	
	@Transactional
	public FileItem createFile(FileItem fileItem){
		return fileItemDao.create(fileItem);
	}
	
	@Transactional
	public FileItem updateFile(FileItem fileItem){
		return fileItemDao.update(fileItem);
	}
	
	@Transactional
	public void deleteFile(String id){
		FileItem fileItem=fileItemDao.read(FileItem.class, id);
		fileItemDao.delete(fileItem);
	}
}
