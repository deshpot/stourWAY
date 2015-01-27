package com.qzsitu.stourway.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

@Entity
@NamedQueries({
	@NamedQuery(name = "Group.getGroupByParent", query = "from Group where parentGroup = ?"),
	@NamedQuery(name = "Group.getGroupByGroupName",query="from Group where name like :name"),
	@NamedQuery(name = "Group.getRoleList",query="from Group where type = 'role'"),
	@NamedQuery(name = "Group.getGroupList",query="from Group where type <> 'role'"),
	@NamedQuery(name = "Group.getGroupCount", query ="Select count(*) from Group where type <> 'role' "),
	@NamedQuery(name = "Group.getAll",query="from Group"),
})
@Table(name = "stsd_group")
@JsonIgnoreProperties(value={"userList"}) 
public class Group {
	String id;
	String name;
	String parentGroup;
	String description;
	String type;
	List<User> userList = new ArrayList<User>();
	
	public static final String GROUP_TYPE_ROLE="role";
	public static final String GROUP_TYPE_GROUP="group";
	public static final String GROUP_ROOT = "";
	
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
	public String getParentGroup() {
		return parentGroup;
	}
	public void setParentGroup(String parentGroup) {
		this.parentGroup = parentGroup;
	}
	
	@Basic
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Basic
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
	        mappedBy = "groupList",targetEntity = User.class)
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
}
