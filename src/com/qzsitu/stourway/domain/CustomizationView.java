package com.qzsitu.stourway.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

//暂不启用
//@Entity
//@Table(name = "STSD_Customization_View")
public class CustomizationView {
	String id;
	String viewName;
	String topMenuListName;
	List<CustomizationViewMenu> topMenuList;
	String middleMenuListName;
	List<CustomizationViewMenu> middleMenuList;
	String bottomMenuListName;
	List<CustomizationViewMenu> bottomMenuList;
	Boolean active;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public String getTopMenuListName() {
		return topMenuListName;
	}
	public void setTopMenuListName(String topMenuListName) {
		this.topMenuListName = topMenuListName;
	}
	public List<CustomizationViewMenu> getTopMenuList() {
		return topMenuList;
	}
	public void setTopMenuList(List<CustomizationViewMenu> topMenuList) {
		this.topMenuList = topMenuList;
	}
	public String getMiddleMenuListName() {
		return middleMenuListName;
	}
	public void setMiddleMenuListName(String middleMenuListName) {
		this.middleMenuListName = middleMenuListName;
	}
	public List<CustomizationViewMenu> getMiddleMenuList() {
		return middleMenuList;
	}
	public void setMiddleMenuList(List<CustomizationViewMenu> middleMenuList) {
		this.middleMenuList = middleMenuList;
	}
	public String getBottomMenuListName() {
		return bottomMenuListName;
	}
	public void setBottomMenuListName(String bottomMenuListName) {
		this.bottomMenuListName = bottomMenuListName;
	}
	@OneToMany(orphanRemoval=true)
	@Cascade(value = {CascadeType.ALL})
	@Fetch(FetchMode.JOIN) 
	public List<CustomizationViewMenu> getBottomMenuList() {
		return bottomMenuList;
	}
	public void setBottomMenuList(List<CustomizationViewMenu> bottomMenuList) {
		this.bottomMenuList = bottomMenuList;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
}
