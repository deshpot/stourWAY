package com.qzsitu.stourway.xmod.azdproject;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.qzsitu.stourway.xmod.system.XModBusinessDomain;

@Entity
@NamedQueries({
	@NamedQuery(name = "AzdProject.getAzdProjectByProcessId", query ="from AzdProject where processId=?"),
	@NamedQuery(name = "AzdProject.getAzdProjectList", query ="from AzdProject order by startTime desc"),
})
@Table(name = "stsd_xmod_azdproject")
public class AzdProject implements XModBusinessDomain{
	String id;
	String processId;
	String description;
	String initiator;
	Date startTime;
	Date endTime;
	String status;
	
	
	String projectName;
	String buyerCompany;
	String buyerContact;
	String businessManager;
	String projectManager;
	String financialManager;
	String conceptSolutionManager;
	String detailedSolutionManager;
	String conceptDesignManager;
	String detailedDesignManager;
	
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
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	@Basic
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	@Lob
	@Column(columnDefinition="longtext")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Basic
	public String getBuyerCompany() {
		return buyerCompany;
	}
	public void setBuyerCompany(String buyerCompany) {
		this.buyerCompany = buyerCompany;
	}
	@Basic
	public String getBuyerContact() {
		return buyerContact;
	}
	public void setBuyerContact(String buyerContact) {
		this.buyerContact = buyerContact;
	}
	@Basic
	public String getInitiator() {
		return initiator;
	}
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	@Basic
	public String getBusinessManager() {
		return businessManager;
	}
	public void setBusinessManager(String businessManager) {
		this.businessManager = businessManager;
	}
	@Basic
	public String getProjectManager() {
		return projectManager;
	}
	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}
	@Basic
	public String getFinancialManager() {
		return financialManager;
	}
	public void setFinancialManager(String financialManager) {
		this.financialManager = financialManager;
	}
	@Basic
	public String getConceptSolutionManager() {
		return conceptSolutionManager;
	}
	public void setConceptSolutionManager(String conceptSolutionManager) {
		this.conceptSolutionManager = conceptSolutionManager;
	}
	@Basic
	public String getDetailedSolutionManager() {
		return detailedSolutionManager;
	}
	public void setDetailedSolutionManager(String detailedSolutionManager) {
		this.detailedSolutionManager = detailedSolutionManager;
	}
	@Basic
	public String getConceptDesignManager() {
		return conceptDesignManager;
	}
	public void setConceptDesignManager(String conceptDesignManager) {
		this.conceptDesignManager = conceptDesignManager;
	}
	@Basic
	public String getDetailedDesignManager() {
		return detailedDesignManager;
	}
	public void setDetailedDesignManager(String detailedDesignManager) {
		this.detailedDesignManager = detailedDesignManager;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Basic
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
