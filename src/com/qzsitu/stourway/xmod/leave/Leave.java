package com.qzsitu.stourway.xmod.leave;

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

@Entity

@NamedQueries({
	@NamedQuery(name = "Leave.getLeaveByExecutionId", query ="from Leave where processId=?"),
})
@Table(name="stsd_xmod_leave")
public class Leave {
	String Description;
	String id;
	String processId;
	String initiator;
	String reason;
	String confirmBy;
	Date leaveDate;
	Date returnDate;
	float workDays;
	
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
	public String getInitiator() {
		return initiator;
	}
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	@Basic
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	@Basic
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	@Basic
	public String getConfirmBy() {
		return confirmBy;
	}
	public void setConfirmBy(String confirmBy) {
		this.confirmBy = confirmBy;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	@Basic
	public float getWorkDays() {
		return workDays;
	}
	public void setWorkDays(float workDays) {
		this.workDays = workDays;
	}
	
}
