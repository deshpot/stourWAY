package com.qzsitu.stourway.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@NamedQueries({
	@NamedQuery(name = "KnowledgeMenu.getMenuByParent",
			query = "from KnowledgeMenu where parentMenuId like ?"),
	@NamedQuery(name = "KnowledgeMenu.getMenuById",
			query = "from KnowledgeMenu where id = ?"),
	@NamedQuery(name = "KnowledgeMenu.getMenuByName",
			query = "from KnowledgeMenu where name = ?"),
	@NamedQuery(name = "KnowledgeMenu.getAllMenu",
			query = "from KnowledgeMenu"),
})
@Table(name = "stsd_knowledge_menu")
public class KnowledgeMenu {
	String id;
	String name;
	String parentMenuId;
	String status;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Basic
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Basic
	public String getParentMenuId() {
		return parentMenuId;
	}
	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}
	@Basic
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
