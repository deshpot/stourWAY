package com.qzsitu.stourway.domain;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
	@NamedQuery(name = "User.getUserList",query="from User where id!='stourway' order by id desc"),
	@NamedQuery(name = "User.getUserCount", query ="Select count(*) from User where id!='stourway'")
})
@Table(name = "stsd_user")
public class User {
	String id;
	String password;
	String name;
	String email;
	String telephone;
	String Mobilephone;
	List<Group> groupList = new ArrayList<Group>();
	@Id
	@Basic
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Basic
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Basic
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Basic
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Basic
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	@Basic
	public String getMobilephone() {
		return Mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		Mobilephone = mobilephone;
	}
	@ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER)
	@JoinTable(name="stsd_groupuserlink",joinColumns= @JoinColumn(name="userId"),inverseJoinColumns=@JoinColumn(name="groupId"))
	public List<Group> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}
}
