package com.qzsitu.stourway.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "stsd_customization_xmod")
public class CustomizationXMod {
	String id;
	String modName;
	String modDescription;
	Boolean enabled;
	List<CustomizationXModFunction> functionList;
	
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModName() {
		return modName;
	}
	public void setModName(String modName) {
		this.modName = modName;
	}
	public String getModDescription() {
		return modDescription;
	}
	public void setModDescription(String modDescription) {
		this.modDescription = modDescription;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	@OneToMany(orphanRemoval=true)
	@Cascade(value = {CascadeType.ALL})
	@Fetch(FetchMode.JOIN) 
	public List<CustomizationXModFunction> getFunctionList() {
		return functionList;
	}
	public void setFunctionList(List<CustomizationXModFunction> functionList) {
		this.functionList = functionList;
	}
}
