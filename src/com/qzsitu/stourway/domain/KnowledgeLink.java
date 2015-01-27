package com.qzsitu.stourway.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@NamedQueries({
	@NamedQuery(name = "KnowledgeLink.getLink",
			query = "from KnowledgeLink where parentId = ? and  parentType = ? and childId = ? and childType = ?"),
	@NamedQuery(name = "KnowledgeLink.getParentLink",
			query = "from KnowledgeLink where parentType = ? and childId = ? and childType = ?"),
	@NamedQuery(name = "KnowledgeLink.getChildLink",
			query = "from KnowledgeLink where parentId = ? and  parentType = ? and childType = ?"),
	@NamedQuery(name = "KnowledgeLink.getLinkListByMenuTag",
			query = " from KnowledgeLink where ((ParentType='com.qzsitu.stourway.domain.KnowledgeTag'"+
					" AND ParentId like :tagId) Or 'all' = :tagId) AND childId IN (SELECT childId FROM KnowledgeLink"+
					" WHERE (ParentType='com.qzsitu.stourway.domain.KnowledgeMenu' AND" + 
					" (ParentId like :menuId Or 'all' = :menuId) And ParentId in (:menuList))) group by childId order by datetime desc"),
	@NamedQuery(name = "KnowledgeLink.getKnowledgeCount",
			query = "Select childId from KnowledgeLink where ((ParentType='com.qzsitu.stourway.domain.KnowledgeTag'"+
					" AND ParentId like :tagId) Or 'all' = :tagId) AND childId IN (SELECT childId FROM KnowledgeLink"+
					" WHERE (ParentType='com.qzsitu.stourway.domain.KnowledgeMenu' AND" + 
					" (ParentId like :menuId Or 'all' = :menuId) And ParentId in (:menuList)))  group by childId")
})
@Table(name = "stsd_knowledge_link")
public class KnowledgeLink {
	String id;
	String parentType;
	String parentId;
	String childType;
	String childId;
	Date datetime;
	
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
	public String getParentType() {
		return parentType;
	}
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	@Basic
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	@Basic
	public String getChildType() {
		return childType;
	}
	public void setChildType(String childType) {
		this.childType = childType;
	}
	@Basic
	public String getChildId() {
		return childId;
	}
	public void setChildId(String childId) {
		this.childId = childId;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
}
